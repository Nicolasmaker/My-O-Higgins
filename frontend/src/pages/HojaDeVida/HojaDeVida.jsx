// =============================================================
// PÁGINA HOJA DE VIDA — HojaDeVida.jsx
// =============================================================
// Expediente del estudiante en MS-HojaDeVida (puerto 8084).
// Vista master-detail:
//
//   - Izquierda: listado de hojas de vida (una por estudiante).
//   - Derecha: detalle de la hoja seleccionada con Accordion de
//     tres secciones — antecedentes académicos, de apoderado y
//     médicos — cada una con su propio CRUD via modal.
//
// Los endpoints de antecedentes devuelven todos los registros;
// acá se filtran client-side por el idHojaVida seleccionado.
// =============================================================
import { useCallback, useEffect, useMemo, useState } from 'react'
import { Row, Col, ListGroup, Accordion, Badge, Button, Alert, Spinner, Table, Form } from 'react-bootstrap'
import { toast } from 'react-toastify'
import { useAuth } from '../../hooks/useAuth'
import { limpiarRut } from '../../validators/fieldValidators'
import {
  getTodasHojasDeVida,
  crearHojaDeVida,
  actualizarHojaDeVida,
  eliminarHojaDeVida,
  getAntecedentesAcademicos,
  crearAntecedenteAcademico,
  actualizarAntecedenteAcademico,
  eliminarAntecedenteAcademico,
  getAntecedentesApoderado,
  crearAntecedenteApoderado,
  actualizarAntecedenteApoderado,
  eliminarAntecedenteApoderado,
  getAntecedentesMedicos,
  crearAntecedenteMedico,
  actualizarAntecedenteMedico,
  eliminarAntecedenteMedico,
  getDocumentosPorHoja,
  subirDocumentoHojaVida,
  descargarDocumentoHojaVida,
  eliminarDocumentoHojaVida,
} from '../../services/hojaDeVidaService'
import { getAnotacionesByHojaVida } from '../../services/anotacionesService'
import { getImpartirByDocente, getCursos, getAsistenciaByEstudiante } from '../../services/academicoService'
import { formatNivel } from '../../components/Academico/entidadesConfig'
import { formatRut } from '../../utils/formatRut'
import { getMatriculas } from '../../services/matriculaService'
import { getEstudiante } from '../../services/authService'
import { generarHojaVidaPdf } from '../../utils/hojaVidaPdf'
import HojaVidaForm from '../../components/HojaDeVida/HojaVidaForm'
import AntecedenteForm from '../../components/HojaDeVida/AntecedenteForm'
import styles from '../../styles/HojaDeVida.module.css'

// Solo Directivo gestiona la ficha (crear/editar/eliminar hoja + antecedentes).
// Docente/Inspector consultan (con distinto alcance) pero registran vía Anotaciones, no acá.
const ROLES_GESTION = ['ROLE_DIRECTIVO']

// API por tipo de antecedente (crear / actualizar / eliminar)
const ANTECEDENTE_API = {
  academico: { crear: crearAntecedenteAcademico, actualizar: actualizarAntecedenteAcademico, eliminar: eliminarAntecedenteAcademico, idKey: 'idAntAcad' },
  apoderado: { crear: crearAntecedenteApoderado, actualizar: actualizarAntecedenteApoderado, eliminar: eliminarAntecedenteApoderado, idKey: 'idAntApo' },
  medico: { crear: crearAntecedenteMedico, actualizar: actualizarAntecedenteMedico, eliminar: eliminarAntecedenteMedico, idKey: 'idAntMed' },
}

export default function HojaDeVida() {
  const { usuario, hasRole } = useAuth()
  const canManage = hasRole(ROLES_GESTION)
  const esDirectivo = hasRole('ROLE_DIRECTIVO')
  const esInspector = hasRole('ROLE_INSPECTOR')
  const esDocente = hasRole('ROLE_DOCENTE')
  const esApoderado = hasRole('ROLE_APODERADO')
  const esEstudiante = hasRole('ROLE_ESTUDIANTE')
  const userRut = usuario?.usuRut

  const [hojas, setHojas] = useState([])
  const [academicos, setAcademicos] = useState([])
  const [apoderados, setApoderados] = useState([])
  const [medicos, setMedicos] = useState([])
  const [loading, setLoading] = useState(true)
  const [loadError, setLoadError] = useState('')

  const [selectedId, setSelectedId] = useState(null)
  const [busqueda, setBusqueda] = useState('')

  // Matrículas + cursos: para filtrar por rol y para resolver "curso" en la búsqueda de Directivo.
  const [matriculas, setMatriculas] = useState([])
  const [cursos, setCursos] = useState([])
  useEffect(() => {
    getMatriculas().then((r) => setMatriculas(Array.isArray(r.data) ? r.data : [])).catch(() => setMatriculas([]))
    getCursos().then((r) => setCursos(Array.isArray(r.data) ? r.data : [])).catch(() => setCursos([]))
  }, [])

  // Docente: sus asignaciones (Impartir) para saber qué cursos dicta.
  const [impartirDocente, setImpartirDocente] = useState([])
  useEffect(() => {
    if (!esDocente || !userRut) return
    getImpartirByDocente(userRut)
      .then((r) => setImpartirDocente(Array.isArray(r.data) ? r.data : []))
      .catch(() => setImpartirDocente([]))
  }, [esDocente, userRut])
  const idsCursoDocente = useMemo(
    () => new Set(impartirDocente.map((i) => i.curso?.idCur).filter(Boolean)),
    [impartirDocente]
  )

  const cursoLabelPorId = useMemo(() => {
    const m = new Map()
    cursos.forEach((c) => m.set(c.idCur, `${formatNivel(c.nivel)} ${c.curLetraSeccion ?? ''}`))
    return m
  }, [cursos])
  const cursoLabelPorRut = useMemo(() => {
    const m = new Map()
    matriculas.forEach((mt) => {
      if (mt.alumnoRut != null && mt.cursoId != null) {
        m.set(String(mt.alumnoRut), cursoLabelPorId.get(mt.cursoId) ?? '')
      }
    })
    return m
  }, [matriculas, cursoLabelPorId])

  // Visibilidad de filas según rol (antes: cualquiera veía todas las hojas).
  const hojasVisibles = useMemo(() => {
    if (esDirectivo || esInspector) return hojas
    if (esDocente) {
      const rutsPermitidos = new Set(
        matriculas.filter((m) => idsCursoDocente.has(m.cursoId)).map((m) => String(m.alumnoRut))
      )
      return hojas.filter((h) => rutsPermitidos.has(String(h.estudianteUsuRut)))
    }
    if (esApoderado) {
      const rutsHijos = new Set(
        matriculas.filter((m) => String(m.apoderadoRut) === String(userRut)).map((m) => String(m.alumnoRut))
      )
      return hojas.filter((h) => rutsHijos.has(String(h.estudianteUsuRut)))
    }
    if (esEstudiante) {
      return hojas.filter((h) => String(h.estudianteUsuRut) === String(userRut))
    }
    return hojas
  }, [hojas, esDirectivo, esInspector, esDocente, esApoderado, esEstudiante, matriculas, idsCursoDocente, userRut])

  // Nombre y DV del estudiante por RUT, resuelto bajo demanda para las hojas visibles (búsqueda por nombre).
  const [nombresPorRut, setNombresPorRut] = useState({})
  useEffect(() => {
    const rutsFaltantes = [
      ...new Set(
        hojasVisibles.map((h) => String(h.estudianteUsuRut)).filter((rut) => rut && !(rut in nombresPorRut))
      ),
    ]
    if (rutsFaltantes.length === 0) return
    Promise.all(
      rutsFaltantes.map((rut) =>
        getEstudiante(rut)
          .then((r) => [rut, { nombre: `${r.data?.usuPNombre ?? ''} ${r.data?.usuApePat ?? ''}`.trim(), dv: r.data?.usuDvRut ?? null }])
          .catch(() => [rut, { nombre: '', dv: null }])
      )
    ).then((pares) => {
      setNombresPorRut((prev) => {
        const next = { ...prev }
        pares.forEach(([rut, datos]) => {
          next[rut] = datos
        })
        return next
      })
    })
  }, [hojasVisibles, nombresPorRut])

  // Modales
  const [showHojaForm, setShowHojaForm] = useState(false)
  const [hojaEdit, setHojaEdit] = useState(null)
  const [antTipo, setAntTipo] = useState(null) // 'academico' | 'apoderado' | 'medico' | null
  const [antEdit, setAntEdit] = useState(null)
  const [saving, setSaving] = useState(false)

  const loadAll = useCallback(async () => {
    setLoading(true)
    setLoadError('')
    try {
      const [hjRes, acRes, apRes, mdRes] = await Promise.all([
        getTodasHojasDeVida(),
        getAntecedentesAcademicos(),
        getAntecedentesApoderado(),
        getAntecedentesMedicos(),
      ])
      setHojas(Array.isArray(hjRes.data) ? hjRes.data : [])
      setAcademicos(Array.isArray(acRes.data) ? acRes.data : [])
      setApoderados(Array.isArray(apRes.data) ? apRes.data : [])
      setMedicos(Array.isArray(mdRes.data) ? mdRes.data : [])
    } catch (error) {
      console.error(error)
      const message = error.response?.data?.message || 'No se pudo cargar la información de hojas de vida'
      setLoadError(message)
      toast.error(message)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    loadAll()
  }, [loadAll])

  const hojasFiltradas = useMemo(() => {
    const q = busqueda.trim()
    if (!q) return hojasVisibles
    const qLower = q.toLowerCase()
    return hojasVisibles.filter((h) => {
      const rut = String(h.estudianteUsuRut ?? '')
      if (rut.includes(q) || String(h.idHojaVida ?? '').includes(q)) return true
      const curso = cursoLabelPorRut.get(rut) ?? ''
      if (curso.toLowerCase().includes(qLower)) return true
      const nombre = nombresPorRut[rut]?.nombre ?? ''
      if (nombre.toLowerCase().includes(qLower)) return true
      return false
    })
  }, [hojasVisibles, busqueda, cursoLabelPorRut, nombresPorRut])

  const selected = hojas.find((h) => h.idHojaVida === selectedId) || null

  // Historial de matrículas del estudiante seleccionado (consulta cruzada de solo lectura,
  // no se duplica dato — reusa `matriculas` ya cargado arriba para los filtros de rol).
  const matriculasDelEstudianteSeleccionado = useMemo(() => {
    if (!selected) return []
    return matriculas
      .filter((m) => String(m.alumnoRut) === String(selected.estudianteUsuRut))
      .sort((a, b) => (b.matriculaAnioAcademico ?? 0) - (a.matriculaAnioAcademico ?? 0))
  }, [matriculas, selected])

  const ESTADO_BADGE = { Incorporado: 'success', Retirado: 'secondary', Suspendido: 'warning' }

  const antecedentesDeHoja = useMemo(
    () => ({
      academico: academicos.filter((a) => a.idHojaVida === selectedId),
      apoderado: apoderados.filter((a) => a.idHojaVida === selectedId),
      medico: medicos.filter((a) => a.idHojaVida === selectedId),
    }),
    [academicos, apoderados, medicos, selectedId]
  )

  // Anotaciones del estudiante, mostradas de solo lectura dentro de su hoja de vida.
  const [anotacionesHoja, setAnotacionesHoja] = useState([])
  const [loadingAnotaciones, setLoadingAnotaciones] = useState(false)
  useEffect(() => {
    if (!selectedId) {
      setAnotacionesHoja([])
      return
    }
    setLoadingAnotaciones(true)
    getAnotacionesByHojaVida(selectedId)
      .then((r) => setAnotacionesHoja(Array.isArray(r.data) ? r.data : []))
      .catch(() => setAnotacionesHoja([]))
      .finally(() => setLoadingAnotaciones(false))
  }, [selectedId])

  // Asistencia del estudiante, de solo lectura (consulta cruzada a MS-GestionAcademica —
  // no se duplica el dato en HojaDeVida, se lee directo del origen). Se registra desde
  // Académico > "Pasar Lista" (Docente); acá solo se refleja.
  const [asistenciaHoja, setAsistenciaHoja] = useState([])
  const [loadingAsistencia, setLoadingAsistencia] = useState(false)
  useEffect(() => {
    if (!selected?.estudianteUsuRut) {
      setAsistenciaHoja([])
      return
    }
    setLoadingAsistencia(true)
    getAsistenciaByEstudiante(selected.estudianteUsuRut)
      .then((r) => setAsistenciaHoja(Array.isArray(r.data) ? r.data : []))
      .catch(() => setAsistenciaHoja([]))
      .finally(() => setLoadingAsistencia(false))
  }, [selected?.estudianteUsuRut])

  // Documentos oficiales adjuntos de la hoja seleccionada.
  const [documentosHoja, setDocumentosHoja] = useState([])
  const [loadingDocumentos, setLoadingDocumentos] = useState(false)
  const [subiendoDocumento, setSubiendoDocumento] = useState(false)

  const cargarDocumentos = useCallback(() => {
    if (!selectedId) {
      setDocumentosHoja([])
      return
    }
    setLoadingDocumentos(true)
    getDocumentosPorHoja(selectedId)
      .then((r) => setDocumentosHoja(Array.isArray(r.data) ? r.data : []))
      .catch(() => setDocumentosHoja([]))
      .finally(() => setLoadingDocumentos(false))
  }, [selectedId])

  useEffect(() => {
    cargarDocumentos()
  }, [cargarDocumentos])

  const EXTENSIONES_PERMITIDAS = ['pdf', 'jpg', 'jpeg', 'png', 'doc', 'docx']
  const TAMANIO_MAXIMO_BYTES = 5 * 1024 * 1024

  const formatearTamanio = (bytes) => {
    if (bytes == null) return '—'
    if (bytes < 1024) return `${bytes} B`
    if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`
    return `${(bytes / (1024 * 1024)).toFixed(1)} MB`
  }

  const handleSubirDocumento = async (event) => {
    const file = event.target.files?.[0]
    event.target.value = ''
    if (!file || !selectedId) return

    const extension = file.name.includes('.') ? file.name.split('.').pop().toLowerCase() : ''
    if (!EXTENSIONES_PERMITIDAS.includes(extension)) {
      toast.error(`Tipo de archivo no permitido. Solo se aceptan: ${EXTENSIONES_PERMITIDAS.join(', ')}`)
      return
    }
    if (file.size > TAMANIO_MAXIMO_BYTES) {
      toast.error('El archivo supera el tamaño máximo permitido (5MB).')
      return
    }

    setSubiendoDocumento(true)
    const formData = new FormData()
    formData.append('file', file)
    try {
      await subirDocumentoHojaVida(selectedId, formData)
      toast.success('Documento subido')
      cargarDocumentos()
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.mensaje || 'No se pudo subir el documento')
    } finally {
      setSubiendoDocumento(false)
    }
  }

  const handleDescargarDocumento = async (documento) => {
    try {
      const res = await descargarDocumentoHojaVida(documento.idDocumento)
      const url = URL.createObjectURL(new Blob([res.data], { type: documento.tipoContenido }))
      const a = document.createElement('a')
      a.href = url
      a.download = documento.nombreArchivo
      a.click()
      URL.revokeObjectURL(url)
    } catch (error) {
      console.error(error)
      toast.error('No se pudo descargar el documento')
    }
  }

  const handleEliminarDocumento = async (documento) => {
    if (!window.confirm(`¿Eliminar el documento "${documento.nombreArchivo}"?`)) return
    try {
      await eliminarDocumentoHojaVida(documento.idDocumento)
      toast.success('Documento eliminado')
      cargarDocumentos()
    } catch (error) {
      console.error(error)
      toast.error('No se pudo eliminar el documento')
    }
  }

  // ── CRUD hoja de vida ───────────────────────────────────────
  const handleSaveHoja = async (data) => {
    setSaving(true)
    const payload = {
      estudianteUsuRut: limpiarRut(data.estudianteUsuRut),
      matriculaId: Number(data.matriculaId),
      estado: data.estado || null,
    }
    try {
      if (hojaEdit) {
        await actualizarHojaDeVida(hojaEdit.idHojaVida, payload)
        toast.success('Hoja de vida actualizada')
      } else {
        await crearHojaDeVida(payload)
        toast.success('Hoja de vida creada')
      }
      setShowHojaForm(false)
      setHojaEdit(null)
      await loadAll()
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || 'No se pudo guardar la hoja de vida')
    } finally {
      setSaving(false)
    }
  }

  const handleDeleteHoja = async (hoja) => {
    if (!window.confirm(`¿Eliminar la hoja de vida #${hoja.idHojaVida} y todos sus antecedentes?`)) return
    try {
      await eliminarHojaDeVida(hoja.idHojaVida)
      toast.success('Hoja de vida eliminada')
      if (selectedId === hoja.idHojaVida) setSelectedId(null)
      await loadAll()
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || 'No se pudo eliminar la hoja de vida')
    }
  }

  // ── CRUD antecedentes (genérico por tipo) ───────────────────
  const handleSaveAntecedente = async (data) => {
    const api = ANTECEDENTE_API[antTipo]
    if (!api || !selectedId) return
    setSaving(true)
    const payload = { ...data, idHojaVida: selectedId }
    if (antTipo === 'academico') {
      payload.anioEscolar = Number(data.anioEscolar)
      payload.promedioGeneralActual = Number(data.promedioGeneralActual)
    }
    try {
      if (antEdit) {
        await api.actualizar(antEdit[api.idKey], payload)
        toast.success('Antecedente actualizado')
      } else {
        await api.crear(payload)
        toast.success('Antecedente registrado')
      }
      setAntTipo(null)
      setAntEdit(null)
      await loadAll()
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || 'No se pudo guardar el antecedente')
    } finally {
      setSaving(false)
    }
  }

  const handleDeleteAntecedente = async (tipo, item) => {
    const api = ANTECEDENTE_API[tipo]
    if (!window.confirm('¿Eliminar este antecedente?')) return
    try {
      await api.eliminar(item[api.idKey])
      toast.success('Antecedente eliminado')
      await loadAll()
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || 'No se pudo eliminar el antecedente')
    }
  }

  const openAntecedente = (tipo, item = null) => {
    setAntTipo(tipo)
    setAntEdit(item)
  }

  const handleDescargarPdf = () => {
    if (!selected) return
    const datosEstudiante = nombresPorRut[String(selected.estudianteUsuRut)]
    generarHojaVidaPdf({
      hoja: selected,
      nombreEstudiante: datosEstudiante?.nombre,
      rutFormateado: formatRut(selected.estudianteUsuRut, datosEstudiante?.dv),
      anotaciones: anotacionesHoja,
      asistencia: asistenciaHoja,
      documentos: documentosHoja,
      matriculas: matriculasDelEstudianteSeleccionado,
      academicos: antecedentesDeHoja.academico,
      apoderados: antecedentesDeHoja.apoderado,
      medicos: antecedentesDeHoja.medico,
    })
  }

  const sectionHeader = (titulo, count) => (
    <span className={styles.sectionHeader}>
      {titulo} <Badge bg="secondary">{count}</Badge>
    </span>
  )

  const addButton = (tipo) =>
    canManage && (
      <div className="d-flex justify-content-end mb-2">
        <Button size="sm" variant="outline-secondary" onClick={() => openAntecedente(tipo)}>
          + Agregar
        </Button>
      </div>
    )

  const accionesCell = (tipo, item) =>
    canManage && (
      <td className="text-end">
        <Button size="sm" variant="outline-secondary" className="me-1" onClick={() => openAntecedente(tipo, item)}>
          Editar
        </Button>
        <Button size="sm" variant="outline-danger" onClick={() => handleDeleteAntecedente(tipo, item)}>
          Eliminar
        </Button>
      </td>
    )

  return (
    <div className={styles.page}>
      <main className={styles.shell}>
        <header className={styles.pageHeader}>
          <div>
            <h1 className={styles.title}>Hoja de Vida del Estudiante</h1>
            <p className={styles.subtitle}>
              Expediente con antecedentes académicos, de apoderado y médicos
            </p>
          </div>
          {canManage && (
            <Button
              className={styles.btnGranate}
              onClick={() => {
                setHojaEdit(null)
                setShowHojaForm(true)
              }}
            >
              + Nueva hoja de vida
            </Button>
          )}
        </header>

        {loading ? (
          <div className={styles.emptyState}>
            <Spinner animation="border" size="sm" className="me-2" />
            Cargando hojas de vida...
          </div>
        ) : loadError ? (
          <Alert variant="danger">{loadError}</Alert>
        ) : (
          <Row className="g-3">
            {/* ── Lista de hojas de vida ── */}
            <Col md={4} lg={3}>
              <input
                className={`form-control form-control-sm ${styles.searchInput}`}
                placeholder="Buscar por RUT, nombre, curso o N° de hoja"
                value={busqueda}
                onChange={(e) => setBusqueda(e.target.value)}
              />
              {hojasFiltradas.length === 0 ? (
                <div className={styles.emptyState}>
                  {hojasVisibles.length === 0 ? 'No hay hojas de vida registradas.' : 'Sin coincidencias.'}
                </div>
              ) : (
                <ListGroup className={styles.hojaList}>
                  {hojasFiltradas.map((h) => {
                    const rut = String(h.estudianteUsuRut ?? '')
                    const datosEstudiante = nombresPorRut[rut]
                    const rutFormateado = formatRut(h.estudianteUsuRut, datosEstudiante?.dv)
                    const curso = cursoLabelPorRut.get(rut)
                    return (
                      <ListGroup.Item
                        key={h.idHojaVida}
                        action
                        active={h.idHojaVida === selectedId}
                        onClick={() => setSelectedId(h.idHojaVida)}
                        className={styles.hojaItem}
                      >
                        <strong>Hoja #{h.idHojaVida}</strong>
                        <span className={styles.hojaItemMeta}>
                          {datosEstudiante?.nombre ? `${datosEstudiante.nombre} · RUT ${rutFormateado}` : `Estudiante: ${rutFormateado}`}
                        </span>
                        <span className={styles.hojaItemMeta}>
                          {curso ? `Curso: ${curso}` : `Matrícula: ${h.matriculaId}`}
                        </span>
                      </ListGroup.Item>
                    )
                  })}
                </ListGroup>
              )}
            </Col>

            {/* ── Detalle de la hoja seleccionada ── */}
            <Col md={8} lg={9}>
              {!selected ? (
                <div className={styles.emptyState}>Selecciona una hoja de vida para ver su expediente.</div>
              ) : (
                <div className={styles.detailPane}>
                  <header className={styles.detailHeader}>
                    <div>
                      <h2 className={styles.detailTitle}>
                        Hoja de vida #{selected.idHojaVida}{' '}
                        {selected.estado && (
                          <Badge bg={ESTADO_BADGE[selected.estado] || 'secondary'}>{selected.estado}</Badge>
                        )}
                      </h2>
                      <span className={styles.detailMeta}>
                        Estudiante RUT {formatRut(selected.estudianteUsuRut, nombresPorRut[String(selected.estudianteUsuRut)]?.dv)} · Matrícula #{selected.matriculaId}
                      </span>
                    </div>
                    <div className="d-flex gap-2">
                      <Button size="sm" className={styles.btnGranate} onClick={handleDescargarPdf}>
                        Descargar PDF
                      </Button>
                      {canManage && (
                        <>
                          <Button
                            size="sm"
                            variant="outline-secondary"
                            onClick={() => {
                              setHojaEdit(selected)
                              setShowHojaForm(true)
                            }}
                          >
                            Editar
                          </Button>
                          <Button size="sm" variant="outline-danger" onClick={() => handleDeleteHoja(selected)}>
                            Eliminar
                          </Button>
                        </>
                      )}
                    </div>
                  </header>

                  <Accordion defaultActiveKey="academico" alwaysOpen className={styles.accordion}>
                    {/* ── Anotaciones (solo lectura, vinculadas por idHojaVida) ── */}
                    <Accordion.Item eventKey="anotaciones">
                      <Accordion.Header>
                        {sectionHeader('Anotaciones', anotacionesHoja.length)}
                      </Accordion.Header>
                      <Accordion.Body>
                        {loadingAnotaciones ? (
                          <Spinner animation="border" size="sm" />
                        ) : anotacionesHoja.length === 0 ? (
                          <p className={styles.sectionEmpty}>Sin anotaciones registradas.</p>
                        ) : (
                          <Table size="sm" hover responsive className={styles.sectionTable}>
                            <thead>
                              <tr>
                                <th>Fecha</th>
                                <th>Tipo</th>
                                <th>Descripción</th>
                              </tr>
                            </thead>
                            <tbody>
                              {anotacionesHoja.map((a) => (
                                <tr key={a.idAnot}>
                                  <td>{a.anotFec ? new Date(a.anotFec).toLocaleDateString('es-CL') : '—'}</td>
                                  <td>
                                    <Badge bg={a.anotTip === 'Positiva' ? 'success' : 'danger'}>{a.anotTip}</Badge>
                                  </td>
                                  <td>{a.anotDes}</td>
                                </tr>
                              ))}
                            </tbody>
                          </Table>
                        )}
                      </Accordion.Body>
                    </Accordion.Item>

                    {/* ── Asistencia (solo lectura, consulta cruzada a MS-GestionAcademica —
                        se registra desde Académico > Pasar Lista) ── */}
                    <Accordion.Item eventKey="asistencia">
                      <Accordion.Header>
                        {sectionHeader('Asistencia', asistenciaHoja.length)}
                      </Accordion.Header>
                      <Accordion.Body>
                        {loadingAsistencia ? (
                          <Spinner animation="border" size="sm" />
                        ) : asistenciaHoja.length === 0 ? (
                          <p className={styles.sectionEmpty}>Sin asistencia registrada.</p>
                        ) : (
                          <Table size="sm" hover responsive className={styles.sectionTable}>
                            <thead>
                              <tr>
                                <th>Fecha</th>
                                <th>Asignatura</th>
                                <th>Curso</th>
                                <th>Estado</th>
                              </tr>
                            </thead>
                            <tbody>
                              {asistenciaHoja
                                .slice()
                                .sort((a, b) => (a.asisFecha < b.asisFecha ? 1 : -1))
                                .map((a) => (
                                  <tr key={a.idAsis}>
                                    <td>{a.asisFecha ? new Date(`${a.asisFecha}T00:00:00`).toLocaleDateString('es-CL') : '—'}</td>
                                    <td>{a.impartir?.asignatura?.asiNombre ?? '—'}</td>
                                    <td>{a.impartir?.curso ? `${formatNivel(a.impartir.curso.nivel)} ${a.impartir.curso.curLetraSeccion}` : '—'}</td>
                                    <td>
                                      <Badge bg={a.asisEstado === 'PRESENTE' ? 'success' : a.asisEstado === 'JUSTIFICADO' ? 'secondary' : a.asisEstado === 'ATRASADO' ? 'warning' : 'danger'}>
                                        {a.asisEstado}
                                      </Badge>
                                    </td>
                                  </tr>
                                ))}
                            </tbody>
                          </Table>
                        )}
                      </Accordion.Body>
                    </Accordion.Item>

                    {/* ── Documentos oficiales adjuntos ── */}
                    <Accordion.Item eventKey="documentos">
                      <Accordion.Header>
                        {sectionHeader('Documentos oficiales', documentosHoja.length)}
                      </Accordion.Header>
                      <Accordion.Body>
                        {canManage && (
                          <div className="d-flex justify-content-end mb-2">
                            <Form.Label
                              className={`btn btn-sm btn-outline-secondary mb-0 ${subiendoDocumento ? 'disabled' : ''}`}
                            >
                              {subiendoDocumento ? 'Subiendo...' : '+ Subir documento'}
                              <input
                                type="file"
                                hidden
                                disabled={subiendoDocumento}
                                onChange={handleSubirDocumento}
                                accept=".pdf,.jpg,.jpeg,.png,.doc,.docx"
                              />
                            </Form.Label>
                          </div>
                        )}
                        {loadingDocumentos ? (
                          <Spinner animation="border" size="sm" />
                        ) : documentosHoja.length === 0 ? (
                          <p className={styles.sectionEmpty}>Sin documentos adjuntos.</p>
                        ) : (
                          <Table size="sm" hover responsive className={styles.sectionTable}>
                            <thead>
                              <tr>
                                <th>Nombre</th>
                                <th>Tamaño</th>
                                <th>Fecha</th>
                                <th />
                              </tr>
                            </thead>
                            <tbody>
                              {documentosHoja.map((d) => (
                                <tr key={d.idDocumento}>
                                  <td>{d.nombreArchivo}</td>
                                  <td>{formatearTamanio(d.tamanioBytes)}</td>
                                  <td>
                                    {d.fechaSubida ? new Date(d.fechaSubida).toLocaleDateString('es-CL') : '—'}
                                  </td>
                                  <td className="text-end">
                                    <Button
                                      size="sm"
                                      variant="outline-secondary"
                                      className="me-1"
                                      onClick={() => handleDescargarDocumento(d)}
                                    >
                                      Descargar
                                    </Button>
                                    {canManage && (
                                      <Button
                                        size="sm"
                                        variant="outline-danger"
                                        onClick={() => handleEliminarDocumento(d)}
                                      >
                                        Eliminar
                                      </Button>
                                    )}
                                  </td>
                                </tr>
                              ))}
                            </tbody>
                          </Table>
                        )}
                      </Accordion.Body>
                    </Accordion.Item>

                    {/* ── Historial de matrículas (solo lectura, consulta cruzada a MS-GestionMatricula) ── */}
                    <Accordion.Item eventKey="historial-matricula">
                      <Accordion.Header>
                        {sectionHeader('Historial de matrículas', matriculasDelEstudianteSeleccionado.length)}
                      </Accordion.Header>
                      <Accordion.Body>
                        {matriculasDelEstudianteSeleccionado.length === 0 ? (
                          <p className={styles.sectionEmpty}>Sin matrículas registradas.</p>
                        ) : (
                          <Table size="sm" hover responsive className={styles.sectionTable}>
                            <thead>
                              <tr>
                                <th>Año</th>
                                <th>Estado</th>
                                <th>Tipo alumno</th>
                                <th>Fecha</th>
                              </tr>
                            </thead>
                            <tbody>
                              {matriculasDelEstudianteSeleccionado.map((m) => (
                                <tr key={m.idMatricula}>
                                  <td>{m.matriculaAnioAcademico}</td>
                                  <td>{m.matriculaEstado}</td>
                                  <td>{m.tipoAlumno}</td>
                                  <td>{m.matriculaFecha ? new Date(`${m.matriculaFecha}T00:00:00`).toLocaleDateString('es-CL') : '—'}</td>
                                </tr>
                              ))}
                            </tbody>
                          </Table>
                        )}
                      </Accordion.Body>
                    </Accordion.Item>

                    {/* ── Académicos ── */}
                    <Accordion.Item eventKey="academico">
                      <Accordion.Header>
                        {sectionHeader('Antecedentes académicos', antecedentesDeHoja.academico.length)}
                      </Accordion.Header>
                      <Accordion.Body>
                        {addButton('academico')}
                        {antecedentesDeHoja.academico.length === 0 ? (
                          <p className={styles.sectionEmpty}>Sin antecedentes académicos.</p>
                        ) : (
                          <Table size="sm" hover responsive className={styles.sectionTable}>
                            <thead>
                              <tr>
                                <th>Año escolar</th>
                                <th>Promedio</th>
                                <th>Aprobado</th>
                                {canManage && <th />}
                              </tr>
                            </thead>
                            <tbody>
                              {antecedentesDeHoja.academico.map((a) => (
                                <tr key={a.idAntAcad}>
                                  <td>{a.anioEscolar}</td>
                                  <td>{a.promedioGeneralActual?.toFixed?.(1) ?? a.promedioGeneralActual}</td>
                                  <td>
                                    <Badge bg={a.situacionFinalAprobacion === 'S' ? 'success' : 'danger'}>
                                      {a.situacionFinalAprobacion === 'S' ? 'Sí' : 'No'}
                                    </Badge>
                                  </td>
                                  {accionesCell('academico', a)}
                                </tr>
                              ))}
                            </tbody>
                          </Table>
                        )}
                      </Accordion.Body>
                    </Accordion.Item>

                    {/* ── Apoderado ── */}
                    <Accordion.Item eventKey="apoderado">
                      <Accordion.Header>
                        {sectionHeader('Antecedentes de apoderado', antecedentesDeHoja.apoderado.length)}
                      </Accordion.Header>
                      <Accordion.Body>
                        {addButton('apoderado')}
                        {antecedentesDeHoja.apoderado.length === 0 ? (
                          <p className={styles.sectionEmpty}>Sin antecedentes de apoderado.</p>
                        ) : (
                          <Table size="sm" hover responsive className={styles.sectionTable}>
                            <thead>
                              <tr>
                                <th>Nombre</th>
                                <th>Profesión</th>
                                <th>Teléfono</th>
                                <th>Lugar de trabajo</th>
                                <th>Disponibilidad</th>
                                {canManage && <th />}
                              </tr>
                            </thead>
                            <tbody>
                              {antecedentesDeHoja.apoderado.map((a) => (
                                <tr key={a.idAntApo}>
                                  <td>{a.nombre}</td>
                                  <td>{a.profesion}</td>
                                  <td>{a.telefono}</td>
                                  <td>{a.lugarTrabajo}</td>
                                  <td>
                                    <Badge bg={a.disponibilidadHoraria === 'S' ? 'success' : 'secondary'}>
                                      {a.disponibilidadHoraria === 'S' ? 'Sí' : 'No'}
                                    </Badge>
                                  </td>
                                  {accionesCell('apoderado', a)}
                                </tr>
                              ))}
                            </tbody>
                          </Table>
                        )}
                      </Accordion.Body>
                    </Accordion.Item>

                    {/* ── Médicos ── */}
                    <Accordion.Item eventKey="medico">
                      <Accordion.Header>
                        {sectionHeader('Antecedentes médicos', antecedentesDeHoja.medico.length)}
                      </Accordion.Header>
                      <Accordion.Body>
                        {addButton('medico')}
                        {antecedentesDeHoja.medico.length === 0 ? (
                          <p className={styles.sectionEmpty}>Sin antecedentes médicos.</p>
                        ) : (
                          antecedentesDeHoja.medico.map((a) => (
                            <div key={a.idAntMed} className={styles.medicoCard}>
                              <div className={styles.medicoTop}>
                                <Badge bg="danger">{a.tipoSangre}</Badge>
                                {canManage && (
                                  <div>
                                    <Button
                                      size="sm"
                                      variant="outline-secondary"
                                      className="me-1"
                                      onClick={() => openAntecedente('medico', a)}
                                    >
                                      Editar
                                    </Button>
                                    <Button
                                      size="sm"
                                      variant="outline-danger"
                                      onClick={() => handleDeleteAntecedente('medico', a)}
                                    >
                                      Eliminar
                                    </Button>
                                  </div>
                                )}
                              </div>
                              <dl className={styles.medicoGrid}>
                                <dt>Alergias</dt>
                                <dd>{a.alergias}</dd>
                                <dt>Medicamentos</dt>
                                <dd>{a.medicamentos}</dd>
                                <dt>Condiciones médicas</dt>
                                <dd>{a.condicionesMedicas}</dd>
                                {a.observaciones && (
                                  <>
                                    <dt>Observaciones</dt>
                                    <dd>{a.observaciones}</dd>
                                  </>
                                )}
                              </dl>
                            </div>
                          ))
                        )}
                      </Accordion.Body>
                    </Accordion.Item>
                  </Accordion>
                </div>
              )}
            </Col>
          </Row>
        )}
      </main>

      <HojaVidaForm
        show={showHojaForm}
        hoja={hojaEdit}
        saving={saving}
        onSave={handleSaveHoja}
        onClose={() => {
          setShowHojaForm(false)
          setHojaEdit(null)
        }}
      />

      <AntecedenteForm
        show={!!antTipo}
        tipo={antTipo || 'academico'}
        antecedente={antEdit}
        saving={saving}
        onSave={handleSaveAntecedente}
        onClose={() => {
          setAntTipo(null)
          setAntEdit(null)
        }}
      />
    </div>
  )
}
