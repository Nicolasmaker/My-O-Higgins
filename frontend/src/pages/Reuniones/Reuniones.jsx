// =============================================================
// PÁGINA DE REUNIONES — Reuniones.jsx
// =============================================================
// Interfaz del MS-GestionReuniones (puerto 8081). Conceptualmente
// solo hay 2 tipos de reunión (Individual y General-de-curso);
// "BitReunionApoderado" es infraestructura interna compartida por
// ambas, no una categoría navegable propia.
//
//   - Individual → 1-a-1 con un apoderado. Flujo: agendar (PENDIENTE)
//     → el apoderado acepta/rechaza → si ACEPTADA, se habilita
//     "Rellenar bitácora" (compromisos/temas tratados/obs).
//   - General    → reunión de un curso completo con sus apoderados.
//     Sin flujo de aceptación (aviso, no invitación); se puede
//     "Completar acta" (comunicado/acuerdos/obs) en cualquier momento.
//
// El formulario de agendar es liviano (solo lo necesario para citar,
// no bitácora) — se abre con "+ Nueva reunión" y crea sobre la
// pestaña activa.
// =============================================================
import { useCallback, useEffect, useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { Tabs, Tab, Row, Col, Alert, Spinner, Button } from 'react-bootstrap'
import { toast } from 'react-toastify'
import { useAuth } from '../../hooks/useAuth'
import { limpiarRut } from '../../validators/fieldValidators'
import {
  getReunionesIndividuales,
  crearReunionIndividual,
  actualizarFirmasIndividual,
  actualizarIndividual,
  getReunionesGenerales,
  crearReunionGeneral,
  actualizarGeneral,
  actualizarReunionApoderado,
  confirmarReunionApoderado,
} from '../../services/reunionesService'
import { getCursos, getImpartirByDocente } from '../../services/academicoService'
import { formatNivel } from '../../components/Academico/entidadesConfig'
import AgendarReunionForm from '../../components/Reuniones/AgendarReunionForm'
import ReunionIndividualCard from '../../components/Reuniones/ReunionIndividualCard'
import ReunionGeneralCard from '../../components/Reuniones/ReunionGeneralCard'
import styles from './Reuniones.module.css'

function formatDate(value) {
  if (!value) return 'Sin fecha'
  return new Date(`${value}T00:00:00`).toLocaleDateString('es-CL', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
  })
}

export default function Reuniones() {
  const { usuario, hasRole } = useAuth()
  const location = useLocation()
  const navigate = useNavigate()

  const [individuales, setIndividuales] = useState([])
  const [generales, setGenerales] = useState([])
  const [loading, setLoading] = useState(true)
  const [loadError, setLoadError] = useState('')

  const [activeTab, setActiveTab] = useState('individual')
  const [showForm, setShowForm] = useState(false)
  const [saving, setSaving] = useState(false)
  // Prefill al llegar desde "Vincular citación" en Anotaciones (crea una Reunión Individual)
  const [prefillIndividual, setPrefillIndividual] = useState(null)

  useEffect(() => {
    const prefill = location.state?.prefillIndividual
    if (prefill) {
      setPrefillIndividual(prefill)
      setActiveTab('individual')
      setShowForm(true)
      // Limpia el state de navegación para que un refresh o "atrás" no reabra el prefill.
      navigate(location.pathname, { replace: true, state: {} })
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [location.state])

  const userRut = usuario?.usuRut ?? ''
  const esDirectivo = hasRole('ROLE_DIRECTIVO')
  const esDocente = hasRole('ROLE_DOCENTE')
  const esInspector = hasRole('ROLE_INSPECTOR')
  const esApoderado = hasRole('ROLE_APODERADO')
  const esApoderadoOEstudiante = hasRole(['ROLE_APODERADO', 'ROLE_ESTUDIANTE'])
  const puedeCrearGeneral = esDirectivo || esDocente
  const puedeCrearIndividual = esDirectivo || esDocente || esInspector

  // Inspector no agenda "General" (no suele tener jefatura); Docente y Directivo sí.
  const puedeCrearEnTab = (tab) => (tab === 'general' ? puedeCrearGeneral : puedeCrearIndividual)

  // Cursos: para el dropdown de "General" y para mostrar el nombre del curso en cada acta.
  const [cursos, setCursos] = useState([])
  useEffect(() => {
    getCursos()
      .then((r) => setCursos(Array.isArray(r.data) ? r.data : []))
      .catch(() => setCursos([]))
  }, [])
  const cursoLabelPorId = (id) => {
    const c = cursos.find((x) => x.idCur === Number(id))
    return c ? `${formatNivel(c.nivel)} ${c.curLetraSeccion} (${c.curAnioEscolar})` : null
  }

  // Docente: acota el dropdown de curso (General) a los cursos donde dicta clases.
  const [impartirDocente, setImpartirDocente] = useState([])
  useEffect(() => {
    if (!esDocente || !userRut) return
    getImpartirByDocente(userRut)
      .then((r) => setImpartirDocente(Array.isArray(r.data) ? r.data : []))
      .catch(() => setImpartirDocente([]))
  }, [esDocente, userRut])
  const cursosParaForm = esDocente
    ? Array.from(new Map(impartirDocente.map((i) => i.curso).filter(Boolean).map((c) => [c.idCur, c])).values())
    : cursos

  const loadAll = useCallback(async () => {
    setLoading(true)
    setLoadError('')
    try {
      const [indRes, genRes] = await Promise.all([getReunionesIndividuales(), getReunionesGenerales()])
      setIndividuales(Array.isArray(indRes.data) ? indRes.data : [])
      setGenerales(Array.isArray(genRes.data) ? genRes.data : [])
    } catch (error) {
      console.error(error)
      const message = error.response?.data?.message || 'No se pudieron cargar las reuniones'
      setLoadError(message)
      toast.error(message)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    loadAll()
  }, [loadAll])

  // Confidencialidad de entrevistas individuales: Directivo ve todas; Docente/Inspector
  // solo las que ellos agendaron; Apoderado ve solo las suyas; Estudiante no accede.
  let individualesVisibles = []
  if (esDirectivo) {
    individualesVisibles = individuales
  } else if (puedeCrearIndividual) {
    individualesVisibles = individuales.filter(
      (r) => String(r.bitReunionApoderado?.docenteUsuRut) === String(userRut)
    )
  } else if (esApoderado) {
    individualesVisibles = individuales.filter(
      (r) => String(r.bitReunionApoderado?.apoderadoUsuRut) === String(userRut)
    )
  }

  const counts = {
    individual: individualesVisibles.length,
    general: generales.length,
  }

  // ── Agendar (crea sobre la pestaña activa) ──────────────────
  const handleAgendar = async (data) => {
    setSaving(true)
    try {
      if (activeTab === 'individual') {
        await crearReunionIndividual({
          bitReuFec: data.bitReuFec,
          docenteUsuRut: limpiarRut(userRut),
          apoderadoUsuRut: data.apoderadoUsuRut,
          alumnoRut: Number(data.alumnoRut),
          bitReuIndMotivReu: data.bitReuIndMotivReu,
          idAnotacion: prefillIndividual?.idAnotacion ?? null,
        })
        setPrefillIndividual(null)
      } else {
        await crearReunionGeneral({
          bitReuFec: data.bitReuFec,
          docenteUsuRut: limpiarRut(userRut),
          cursoId: Number(data.cursoId),
          bitReuGenTipReu: data.bitReuGenTipReu,
        })
      }
      toast.success('Reunión agendada')
      setShowForm(false)
      await loadAll()
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || error.response?.data || 'No se pudo agendar la reunión')
    } finally {
      setSaving(false)
    }
  }

  // ── Rellenar bitácora de una entrevista individual (solo tras aceptar) ──
  const handleRellenarBitacora = async (reunion, { bitReuCompromisos, bitReuObs, bitReuIndTemTrat }) => {
    try {
      await Promise.all([
        actualizarReunionApoderado(reunion.bitReunionApoderado.idBitReu, { bitReuCompromisos, bitReuObs }),
        actualizarIndividual(reunion.idBitReuInd, { bitReuIndTemTrat }),
      ])
      toast.success('Bitácora guardada')
      await loadAll()
      return true
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || 'No se pudo guardar la bitácora')
      return false
    }
  }

  // ── Completar acta de una reunión general ───────────────────
  const handleCompletarActa = async (idBitReuGen, payload) => {
    try {
      await actualizarGeneral(idBitReuGen, payload)
      toast.success('Acta guardada')
      await loadAll()
      return true
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || 'No se pudo guardar el acta')
      return false
    }
  }

  // ── Aceptar/rechazar una citación (solo el apoderado dueño) ─
  const handleConfirmar = async (id, estadoConfirmacion) => {
    try {
      await confirmarReunionApoderado(id, { estadoConfirmacion })
      toast.success(estadoConfirmacion === 'ACEPTADA' ? 'Reunión aceptada' : 'Reunión rechazada')
      await loadAll()
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || 'No se pudo actualizar la confirmación')
    }
  }

  // ── Actualizar firmas de entrevista individual ──────────────
  const handleFirmar = async (id, payload) => {
    try {
      await actualizarFirmasIndividual(id, payload)
      toast.success('Firma registrada')
      await loadAll()
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || 'No se pudo registrar la firma')
    }
  }

  const renderGrid = (items, getKey, renderCard, emptyMessage) => {
    if (loading) {
      return (
        <div className={styles.emptyState}>
          <Spinner animation="border" size="sm" className="me-2" />
          Cargando reuniones...
        </div>
      )
    }
    if (loadError) return <Alert variant="danger">{loadError}</Alert>
    if (items.length === 0) return <div className={styles.emptyState}>{emptyMessage}</div>
    return (
      <Row xs={1} md={2} className="g-3">
        {items.map((item) => (
          <Col key={getKey(item)}>{renderCard(item)}</Col>
        ))}
      </Row>
    )
  }

  return (
    <div className={styles.page}>
      <main className={styles.shell}>
        {/* ── Encabezado compacto ── */}
        <header className={styles.pageHeader}>
          <div>
            <h1 className={styles.title}>Reuniones</h1>
          </div>
          <div className={styles.headerActions}>
            {usuario && (
              <span className={styles.userChip}>
                {`${usuario.usuPNombre || ''} ${usuario.usuApePat || ''}`.trim() || 'Sesión activa'}
                {usuario.rolNombre ? ` · ${usuario.rolNombre.replace('ROLE_', '')}` : ''}
              </span>
            )}
            {puedeCrearEnTab(activeTab) && (
              <Button className={styles.btnGranate} onClick={() => setShowForm((v) => !v)}>
                {showForm ? 'Cerrar formulario' : '+ Nueva reunión'}
              </Button>
            )}
          </div>
        </header>

        {/* ── Formulario liviano de agendar (crea sobre la pestaña activa) ── */}
        {showForm && puedeCrearEnTab(activeTab) && (
          <AgendarReunionForm
            key={activeTab}
            tipo={activeTab}
            cursos={cursosParaForm}
            saving={saving}
            onSubmit={handleAgendar}
            onCancel={() => {
              setShowForm(false)
              setPrefillIndividual(null)
            }}
            prefillMotivo={activeTab === 'individual' ? prefillIndividual?.motivoSugerido ?? '' : ''}
          />
        )}

        {/* ── Pestañas: solo Individual y General ── */}
        <Tabs
          activeKey={activeTab}
          onSelect={(key) => setActiveTab(key)}
          className={styles.tabs}
          transition={false}
        >
          <Tab eventKey="individual" title={`Individual (${counts.individual})`}>
            {renderGrid(
              individualesVisibles,
              (item) => item.idBitReuInd,
              (item) => (
                <ReunionIndividualCard
                  reunion={item}
                  formatDate={formatDate}
                  onFirmar={handleFirmar}
                  onConfirmar={handleConfirmar}
                  onRellenarBitacora={handleRellenarBitacora}
                  canEdit={esDirectivo || String(item.bitReunionApoderado?.docenteUsuRut) === String(userRut)}
                  puedeConfirmar={
                    esApoderado &&
                    String(item.bitReunionApoderado?.apoderadoUsuRut) === String(userRut) &&
                    item.bitReunionApoderado?.estadoConfirmacion === 'PENDIENTE'
                  }
                  puedeFirmarFuncionario={
                    esDirectivo || String(item.bitReunionApoderado?.docenteUsuRut) === String(userRut)
                  }
                  puedeFirmarApoderado={
                    esApoderado && String(item.bitReunionApoderado?.apoderadoUsuRut) === String(userRut)
                  }
                />
              ),
              esApoderadoOEstudiante
                ? 'No tienes acceso a esta información.'
                : 'No hay entrevistas individuales registradas.'
            )}
          </Tab>

          <Tab eventKey="general" title={`General (${counts.general})`}>
            {renderGrid(
              generales,
              (item) => item.bitReuGen,
              (item) => (
                <ReunionGeneralCard
                  reunion={item}
                  formatDate={formatDate}
                  cursoLabel={cursoLabelPorId(item.cursoId)}
                  onCompletarActa={handleCompletarActa}
                  canManage={esDirectivo || String(item.bitReunionApoderado?.docenteUsuRut) === String(userRut)}
                />
              ),
              'No hay reuniones de curso registradas.'
            )}
          </Tab>
        </Tabs>
      </main>
    </div>
  )
}
