// =============================================================
// MURAL DIGITAL — MuralDigital.jsx
// =============================================================
// Reemplaza la portada pública para usuarios logueados: un tablero
// con lo que le importa a CADA rol — un estudiante ve sus próximas
// pruebas, un inspector no (no tiene curso propio ni asignaturas).
// Reusa el mismo criterio de visibilidad por audiencia que ya usa
// Calendario.jsx (evento sin cursoId/docenteUsuRut/apoderadoUsuRut/
// alumnoRut = visible para todos; si trae alguno, se filtra por rol).
// =============================================================
import { useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'
import { getEventos, getMurales } from '../../services/calendarioService'
import { getEvaluaciones } from '../../services/academicoService'
import { getMatriculas } from '../../services/matriculaService'
import styles from './MuralDigital.module.css'

function toneClass(tipo) {
  const n = String(tipo || '').toLowerCase()
  if (n.includes('institucional')) return styles.toneInstitucional
  if (n.includes('reun')) return styles.toneReunion
  if (n.includes('actividad')) return styles.toneActividad
  return styles.toneAcademico
}

function formatFecha(value) {
  if (!value) return ''
  return new Date(`${value}T00:00:00`).toLocaleDateString('es-CL', { day: '2-digit', month: 'short' })
}

export default function MuralDigital() {
  const navigate = useNavigate()
  const { usuario, hasRole } = useAuth()
  const esDirectivo = hasRole('ROLE_DIRECTIVO')
  const esDocente = hasRole('ROLE_DOCENTE')
  const esInspector = hasRole('ROLE_INSPECTOR')
  const esApoderado = hasRole('ROLE_APODERADO')
  const esEstudiante = hasRole('ROLE_ESTUDIANTE')
  const veEvaluaciones = esEstudiante || esApoderado || esDocente

  const [matriculas, setMatriculas] = useState([])
  const [eventos, setEventos] = useState([])
  const [murales, setMurales] = useState([])
  const [evaluaciones, setEvaluaciones] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([
      getMatriculas().catch(() => ({ data: [] })),
      getEventos().catch(() => ({ data: [] })),
      getMurales().catch(() => ({ data: [] })),
      veEvaluaciones ? getEvaluaciones().catch(() => ({ data: [] })) : Promise.resolve({ data: [] }),
    ]).then(([matRes, evtRes, murRes, evalRes]) => {
      setMatriculas(Array.isArray(matRes.data) ? matRes.data : [])
      setEventos(Array.isArray(evtRes.data) ? evtRes.data : [])
      setMurales(Array.isArray(murRes.data) ? murRes.data : [])
      setEvaluaciones(Array.isArray(evalRes.data) ? evalRes.data : [])
      setLoading(false)
    })
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  // Curso del estudiante / cursos de los hijos del apoderado — mismo criterio que Calendario.jsx
  const cursosHijosApoderado = useMemo(
    () =>
      new Set(
        matriculas
          .filter((m) => String(m.apoderadoRut) === String(usuario?.usuRut))
          .map((m) => m.cursoId)
          .filter((id) => id != null)
      ),
    [matriculas, usuario?.usuRut]
  )
  const cursoPropioEstudiante = useMemo(() => {
    const propia = matriculas.find((m) => String(m.alumnoRut) === String(usuario?.usuRut))
    return propia?.cursoId ?? null
  }, [matriculas, usuario?.usuRut])

  const eventoVisible = (evento) => {
    if (esDirectivo || esInspector) return true
    const sinAudiencia =
      evento.cursoId == null && evento.docenteUsuRut == null && evento.apoderadoUsuRut == null && evento.alumnoRut == null
    if (sinAudiencia) return true
    if (esDocente) return String(evento.docenteUsuRut) === String(usuario?.usuRut)
    if (esApoderado) {
      return (
        (evento.cursoId != null && cursosHijosApoderado.has(evento.cursoId)) ||
        String(evento.apoderadoUsuRut) === String(usuario?.usuRut)
      )
    }
    if (esEstudiante) {
      return (
        (evento.cursoId != null && cursoPropioEstudiante != null && Number(evento.cursoId) === Number(cursoPropioEstudiante)) ||
        String(evento.alumnoRut) === String(usuario?.usuRut)
      )
    }
    return false
  }

  const hoy = useMemo(() => {
    const d = new Date()
    d.setHours(0, 0, 0, 0)
    return d
  }, [])

  const proximosEventos = useMemo(
    () =>
      eventos
        .filter((e) => e.fechaInicio && new Date(`${e.fechaInicio}T00:00:00`) >= hoy)
        .filter(eventoVisible)
        .sort((a, b) => a.fechaInicio.localeCompare(b.fechaInicio))
        .slice(0, 6),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [eventos, hoy, cursosHijosApoderado, cursoPropioEstudiante, usuario?.usuRut, esDirectivo, esInspector, esDocente, esApoderado, esEstudiante]
  )

  // Evaluaciones futuras propias: Estudiante = su curso, Apoderado = curso de sus hijos,
  // Docente = las que él mismo creó. Inspector/Directivo no ven esta sección (sin curso propio).
  const proximasEvaluaciones = useMemo(() => {
    if (!veEvaluaciones) return []
    return evaluaciones
      .filter((e) => e.evaFecha && new Date(`${e.evaFecha}T00:00:00`) >= hoy)
      .filter((e) => {
        if (esDocente) return String(e.docenteUsuRut) === String(usuario?.usuRut)
        if (esEstudiante) return cursoPropioEstudiante != null && Number(e.curso?.idCur) === Number(cursoPropioEstudiante)
        if (esApoderado) return e.curso?.idCur != null && cursosHijosApoderado.has(e.curso.idCur)
        return false
      })
      .sort((a, b) => a.evaFecha.localeCompare(b.evaFecha))
      .slice(0, 5)
  }, [evaluaciones, hoy, veEvaluaciones, esDocente, esEstudiante, esApoderado, cursoPropioEstudiante, cursosHijosApoderado, usuario?.usuRut])

  const muralesRecientes = useMemo(
    () =>
      murales
        .slice()
        .sort((a, b) => (b.fechaPublicacion || '').localeCompare(a.fechaPublicacion || ''))
        .slice(0, 6),
    [murales]
  )

  if (loading) {
    return <div className={styles.loading}>Cargando mural digital...</div>
  }

  return (
    <div className={styles.page}>
      <div className={styles.container}>
        <header className={styles.header}>
          <p className={styles.eyebrow}>Mural Digital</p>
          <h1 className={styles.title}>Hola, {usuario?.usuPNombre || 'bienvenido'} 👋</h1>
          <p className={styles.subtitle}>Esto es lo que se viene para ti en el colegio</p>
        </header>

        {veEvaluaciones && (
          <section className={styles.section}>
            <h2 className={styles.sectionTitle}>Próximas evaluaciones</h2>
            {proximasEvaluaciones.length === 0 ? (
              <p className={styles.empty}>No tienes evaluaciones próximas registradas.</p>
            ) : (
              <div className={styles.grid}>
                {proximasEvaluaciones.map((ev) => (
                  <div key={ev.idEva} className={`${styles.card} ${styles.toneEvaluacion}`}>
                    <span className={styles.cardDate}>{formatFecha(ev.evaFecha)}</span>
                    <h3 className={styles.cardTitle}>{ev.evaNom}</h3>
                    <p className={styles.cardMeta}>{ev.asignatura?.asiNombre ?? 'Asignatura'} · {ev.evaTipo}</p>
                  </div>
                ))}
              </div>
            )}
          </section>
        )}

        <section className={styles.section}>
          <h2 className={styles.sectionTitle}>Próximos eventos</h2>
          {proximosEventos.length === 0 ? (
            <p className={styles.empty}>No hay eventos próximos por ahora.</p>
          ) : (
            <div className={styles.grid}>
              {proximosEventos.map((ev) => (
                <div key={ev.idCalEst} className={`${styles.card} ${toneClass(ev.tipoEvento)}`}>
                  <span className={styles.cardDate}>{formatFecha(ev.fechaInicio)}</span>
                  <h3 className={styles.cardTitle}>{ev.tituloEvento}</h3>
                  <p className={styles.cardMeta}>{ev.tipoEvento}</p>
                  {ev.descripcionEvento && <p className={styles.cardDesc}>{ev.descripcionEvento}</p>}
                </div>
              ))}
            </div>
          )}
          <button type="button" className={styles.linkButton} onClick={() => navigate('/calendario')}>
            Ver calendario completo →
          </button>
        </section>

        <section className={styles.section}>
          <h2 className={styles.sectionTitle}>Talleres y avisos para todos</h2>
          {muralesRecientes.length === 0 ? (
            <p className={styles.empty}>Sin avisos publicados todavía.</p>
          ) : (
            <div className={styles.grid}>
              {muralesRecientes.map((m) => (
                <div key={m.idMurDig} className={`${styles.card} ${styles.toneMural}`}>
                  <span className={styles.cardDate}>{formatFecha(m.fechaPublicacion)}</span>
                  <h3 className={styles.cardTitle}>{m.titulo}</h3>
                  <p className={styles.cardDesc}>{m.contenido}</p>
                </div>
              ))}
            </div>
          )}
        </section>
      </div>
    </div>
  )
}
