// =============================================================
// PÁGINA DE MATRÍCULAS — Matricula.jsx
// =============================================================
// Registro administrativo del MS-GestionMatricula (puerto 8086, RF-17).
// Vista tipo tabla: cada fila es una matrícula con su estado y tipo
// de alumno como badges. Crear/editar via modal; eliminar con
// confirmación. Filtros locales por estado, tipo y búsqueda por RUT.
// =============================================================
import { useCallback, useEffect, useMemo, useState } from 'react'
import { Table, Badge, Button, Alert, Spinner, Form, Row, Col, Tabs, Tab } from 'react-bootstrap'
import { toast } from 'react-toastify'
import { useAuth } from '../../hooks/useAuth'
import { limpiarRut, rutValido } from '../../validators/fieldValidators'
import {
  getMatriculas,
  crearMatricula,
  actualizarMatricula,
  eliminarMatricula,
  crearSolicitudMatricula,
  getSolicitudesMatricula,
  getSolicitudesPorApoderado,
  aprobarSolicitudMatricula,
  rechazarSolicitudMatricula,
} from '../../services/matriculaService'
import { getCursos } from '../../services/academicoService'
import MatriculaForm from '../../components/Matricula/MatriculaForm'
import styles from './Matricula.module.css'

const ROLES_GESTION = ['ROLE_DIRECTIVO']

const ESTADO_BADGE = {
  ACTIVA: 'success',
  SUSPENDIDA: 'warning',
  RETIRADA: 'secondary',
}

const TIPO_BADGE = {
  NUEVO: 'primary',
  ANTIGUO: 'info',
  REPITENTE: 'dark',
}

const ESTADO_SOLICITUD_BADGE = {
  PENDIENTE: 'warning',
  APROBADA: 'success',
  RECHAZADA: 'danger',
}

function formatDate(value) {
  if (!value) return '—'
  return new Date(`${value}T00:00:00`).toLocaleDateString('es-CL')
}

export default function Matricula() {
  const { usuario, hasRole } = useAuth()
  const userRut = usuario?.usuRut ?? ''
  const canManage = hasRole(ROLES_GESTION)
  const esApoderado = hasRole('ROLE_APODERADO')

  const [matriculas, setMatriculas] = useState([])
  const [loading, setLoading] = useState(true)
  const [loadError, setLoadError] = useState('')

  const [showForm, setShowForm] = useState(false)
  const [editTarget, setEditTarget] = useState(null)
  const [saving, setSaving] = useState(false)

  const [filtroEstado, setFiltroEstado] = useState('')
  const [filtroTipo, setFiltroTipo] = useState('')
  const [busquedaRut, setBusquedaRut] = useState('')

  // ── Solicitudes de matrícula (self-service Apoderado) ───────
  const [cursos, setCursos] = useState([])
  const [solicitudes, setSolicitudes] = useState([])
  const [loadingSolicitudes, setLoadingSolicitudes] = useState(true)
  const [savingSolicitud, setSavingSolicitud] = useState(false)
  const [procesandoId, setProcesandoId] = useState(null)
  const [cursoElegido, setCursoElegido] = useState({})
  const [solForm, setSolForm] = useState({ alumnoRut: '', cursoId: '', tipoAlumno: 'NUEVO', observaciones: '' })

  useEffect(() => {
    getCursos()
      .then((r) => setCursos(Array.isArray(r.data) ? r.data : []))
      .catch(() => setCursos([]))
  }, [])

  const cursoLabel = (id) => {
    const c = cursos.find((x) => x.idCur === Number(id))
    return c ? `${c.nivel?.nivNum ?? '—'}°${c.curLetraSeccion} (${c.curAnioEscolar})` : id ?? '—'
  }

  const loadSolicitudes = useCallback(async () => {
    if (!canManage && !esApoderado) return
    setLoadingSolicitudes(true)
    try {
      const res = canManage ? await getSolicitudesMatricula() : await getSolicitudesPorApoderado(userRut)
      setSolicitudes(Array.isArray(res.data) ? res.data : [])
    } catch (error) {
      console.error(error)
      toast.error('No se pudieron cargar las solicitudes de matrícula')
    } finally {
      setLoadingSolicitudes(false)
    }
  }, [canManage, esApoderado, userRut])

  useEffect(() => {
    loadSolicitudes()
  }, [loadSolicitudes])

  const handleSolicitarMatricula = async (event) => {
    event.preventDefault()
    if (!rutValido(solForm.alumnoRut)) {
      toast.error('Ingresa un RUT de alumno válido')
      return
    }
    setSavingSolicitud(true)
    try {
      await crearSolicitudMatricula({
        alumnoRut: limpiarRut(solForm.alumnoRut),
        apoderadoRut: limpiarRut(userRut),
        cursoId: solForm.cursoId ? Number(solForm.cursoId) : null,
        tipoAlumno: solForm.tipoAlumno,
        observaciones: solForm.observaciones || null,
      })
      toast.success('Solicitud enviada')
      setSolForm({ alumnoRut: '', cursoId: '', tipoAlumno: 'NUEVO', observaciones: '' })
      await loadSolicitudes()
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data || 'No se pudo enviar la solicitud')
    } finally {
      setSavingSolicitud(false)
    }
  }

  const handleAprobarSolicitud = async (solicitud) => {
    setProcesandoId(solicitud.idSolicitud)
    try {
      await aprobarSolicitudMatricula(solicitud.idSolicitud, {
        funcionarioUsuRut: Number(userRut),
        cursoId: cursoElegido[solicitud.idSolicitud]
          ? Number(cursoElegido[solicitud.idSolicitud])
          : solicitud.cursoId,
      })
      toast.success('Solicitud aprobada, matrícula creada')
      await Promise.all([loadSolicitudes(), load()])
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data || 'No se pudo aprobar la solicitud')
    } finally {
      setProcesandoId(null)
    }
  }

  const handleRechazarSolicitud = async (solicitud) => {
    const motivo = window.prompt('Motivo del rechazo:')
    if (motivo === null) return
    setProcesandoId(solicitud.idSolicitud)
    try {
      await rechazarSolicitudMatricula(solicitud.idSolicitud, { motivoRechazo: motivo })
      toast.success('Solicitud rechazada')
      await loadSolicitudes()
    } catch (error) {
      console.error(error)
      toast.error('No se pudo rechazar la solicitud')
    } finally {
      setProcesandoId(null)
    }
  }

  const load = useCallback(async () => {
    setLoading(true)
    setLoadError('')
    try {
      const res = await getMatriculas()
      setMatriculas(Array.isArray(res.data) ? res.data : [])
    } catch (error) {
      console.error(error)
      const message = error.response?.data?.message || 'No se pudieron cargar las matrículas'
      setLoadError(message)
      toast.error(message)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    load()
  }, [load])

  // Apoderado solo ve matrículas de sus propios hijos (el resto de roles ve todas)
  const propias = useMemo(
    () => (esApoderado ? matriculas.filter((m) => String(m.apoderadoRut ?? '') === String(userRut)) : matriculas),
    [matriculas, esApoderado, userRut]
  )

  const filtradas = useMemo(() => {
    const rutQuery = busquedaRut.trim()
    return propias.filter((m) => {
      if (filtroEstado && (m.matriculaEstado || '') !== filtroEstado) return false
      if (filtroTipo && (m.tipoAlumno || '') !== filtroTipo) return false
      if (rutQuery && !String(m.alumnoRut ?? '').includes(rutQuery) && !String(m.apoderadoRut ?? '').includes(rutQuery))
        return false
      return true
    })
  }, [propias, filtroEstado, filtroTipo, busquedaRut])

  const stats = useMemo(
    () => ({
      total: propias.length,
      activas: propias.filter((m) => m.matriculaEstado === 'ACTIVA').length,
    }),
    [propias]
  )

  const handleSave = async (data) => {
    setSaving(true)
    const payload = {
      alumnoRut: limpiarRut(data.alumnoRut),
      apoderadoRut: limpiarRut(data.apoderadoRut),
      cursoId: data.cursoId ? Number(data.cursoId) : null,
      tipoAlumno: data.tipoAlumno,
      funcionarioUsuRut: limpiarRut(data.funcionarioUsuRut),
    }

    try {
      if (editTarget) {
        await actualizarMatricula(editTarget.idMatricula, {
          ...payload,
          matriculaEstado: data.matriculaEstado,
        })
        toast.success('Matrícula actualizada')
      } else {
        await crearMatricula(payload)
        toast.success('Matrícula registrada')
      }
      setShowForm(false)
      setEditTarget(null)
      await load()
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || error.response?.data || 'No se pudo guardar la matrícula')
    } finally {
      setSaving(false)
    }
  }

  const handleDelete = async (m) => {
    if (!window.confirm(`¿Eliminar la matrícula #${m.idMatricula} del alumno ${m.alumnoRut}?`)) return
    try {
      await eliminarMatricula(m.idMatricula)
      toast.success('Matrícula eliminada')
      await load()
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || 'No se pudo eliminar la matrícula')
    }
  }

  const openCreate = () => {
    setEditTarget(null)
    setShowForm(true)
  }

  const openEdit = (m) => {
    setEditTarget(m)
    setShowForm(true)
  }

  return (
    <div className={styles.page}>
      <main className={styles.shell}>
        <header className={styles.pageHeader}>
          <div>
            <h1 className={styles.title}>Registro de Matrículas</h1>
            <p className={styles.subtitle}>
              {stats.total} matrículas registradas · {stats.activas} activas — Año académico {new Date().getFullYear()}
            </p>
          </div>
          {canManage && (
            <Button className={styles.btnGranate} onClick={openCreate}>
              + Registrar matrícula
            </Button>
          )}
        </header>

        <Tabs defaultActiveKey="matriculas" className="mb-3">
        <Tab eventKey="matriculas" title="Matrículas">
        {/* ── Filtros locales ── */}
        <Row className={`g-2 ${styles.filterBar}`}>
          <Col sm={4} md={3}>
            <Form.Control
              size="sm"
              placeholder="Buscar por RUT alumno/apoderado"
              value={busquedaRut}
              onChange={(e) => setBusquedaRut(e.target.value)}
            />
          </Col>
          <Col sm={3} md={2}>
            <Form.Select size="sm" value={filtroEstado} onChange={(e) => setFiltroEstado(e.target.value)}>
              <option value="">Estado: todos</option>
              <option value="ACTIVA">Activa</option>
              <option value="SUSPENDIDA">Suspendida</option>
              <option value="RETIRADA">Retirada</option>
            </Form.Select>
          </Col>
          <Col sm={3} md={2}>
            <Form.Select size="sm" value={filtroTipo} onChange={(e) => setFiltroTipo(e.target.value)}>
              <option value="">Tipo: todos</option>
              <option value="NUEVO">Nuevo</option>
              <option value="ANTIGUO">Antiguo</option>
              <option value="REPITENTE">Repitente</option>
            </Form.Select>
          </Col>
          <Col sm="auto">
            <Button
              size="sm"
              variant="outline-secondary"
              onClick={() => {
                setBusquedaRut('')
                setFiltroEstado('')
                setFiltroTipo('')
              }}
            >
              Limpiar
            </Button>
          </Col>
        </Row>

        {/* ── Tabla ── */}
        {loading ? (
          <div className={styles.emptyState}>
            <Spinner animation="border" size="sm" className="me-2" />
            Cargando matrículas...
          </div>
        ) : loadError ? (
          <Alert variant="danger">{loadError}</Alert>
        ) : filtradas.length === 0 ? (
          <div className={styles.emptyState}>
            {matriculas.length === 0 ? 'No hay matrículas registradas.' : 'Ninguna matrícula coincide con los filtros.'}
          </div>
        ) : (
          <div className={styles.tableWrap}>
            <Table hover responsive className={styles.table}>
              <thead className={styles.tableHead}>
                <tr>
                  <th>#</th>
                  <th>Alumno</th>
                  <th>Apoderado</th>
                  <th>Curso</th>
                  <th>Tipo</th>
                  <th>Estado</th>
                  <th>Fecha</th>
                  <th>Año</th>
                  {canManage && <th className="text-end">Acciones</th>}
                </tr>
              </thead>
              <tbody>
                {filtradas.map((m) => (
                  <tr key={m.idMatricula}>
                    <td>{m.idMatricula}</td>
                    <td>{m.alumnoRut}</td>
                    <td>{m.apoderadoRut}</td>
                    <td>{m.cursoId ?? '—'}</td>
                    <td>
                      <Badge bg={TIPO_BADGE[m.tipoAlumno] || 'secondary'}>{m.tipoAlumno || 'N/D'}</Badge>
                    </td>
                    <td>
                      <Badge bg={ESTADO_BADGE[m.matriculaEstado] || 'secondary'}>
                        {m.matriculaEstado || 'N/D'}
                      </Badge>
                    </td>
                    <td>{formatDate(m.matriculaFecha)}</td>
                    <td>{m.matriculaAnioAcademico ?? '—'}</td>
                    {canManage && (
                      <td className="text-end">
                        <Button size="sm" variant="outline-secondary" className="me-2" onClick={() => openEdit(m)}>
                          Editar
                        </Button>
                        <Button size="sm" variant="outline-danger" onClick={() => handleDelete(m)}>
                          Eliminar
                        </Button>
                      </td>
                    )}
                  </tr>
                ))}
              </tbody>
            </Table>
          </div>
        )}
        </Tab>

        {(canManage || esApoderado) && (
          <Tab eventKey="solicitudes" title="Solicitudes de matrícula">
            {esApoderado && (
              <Form onSubmit={handleSolicitarMatricula} className={`mb-4 p-3 border rounded ${styles.filterBar}`}>
                <Row className="g-2 align-items-end">
                  <Col sm={3}>
                    <Form.Label className="mb-1">RUT del alumno</Form.Label>
                    <Form.Control
                      size="sm"
                      placeholder="12345678-5"
                      value={solForm.alumnoRut}
                      onChange={(e) => setSolForm((f) => ({ ...f, alumnoRut: e.target.value }))}
                      required
                    />
                  </Col>
                  <Col sm={3}>
                    <Form.Label className="mb-1">Curso deseado</Form.Label>
                    <Form.Select
                      size="sm"
                      value={solForm.cursoId}
                      onChange={(e) => setSolForm((f) => ({ ...f, cursoId: e.target.value }))}
                    >
                      <option value="">Sin preferencia</option>
                      {cursos.map((c) => (
                        <option key={c.idCur} value={c.idCur}>
                          {c.nivel?.nivNum ?? '—'}°{c.curLetraSeccion} ({c.curAnioEscolar})
                        </option>
                      ))}
                    </Form.Select>
                  </Col>
                  <Col sm={2}>
                    <Form.Label className="mb-1">Tipo</Form.Label>
                    <Form.Select
                      size="sm"
                      value={solForm.tipoAlumno}
                      onChange={(e) => setSolForm((f) => ({ ...f, tipoAlumno: e.target.value }))}
                    >
                      <option value="NUEVO">Nuevo</option>
                      <option value="ANTIGUO">Antiguo</option>
                      <option value="REPITENTE">Repitente</option>
                    </Form.Select>
                  </Col>
                  <Col sm={3}>
                    <Form.Label className="mb-1">Observaciones</Form.Label>
                    <Form.Control
                      size="sm"
                      placeholder="Opcional"
                      value={solForm.observaciones}
                      onChange={(e) => setSolForm((f) => ({ ...f, observaciones: e.target.value }))}
                    />
                  </Col>
                  <Col sm="auto">
                    <Button size="sm" className={styles.btnGranate} type="submit" disabled={savingSolicitud}>
                      {savingSolicitud ? 'Enviando...' : 'Enviar solicitud'}
                    </Button>
                  </Col>
                </Row>
              </Form>
            )}

            {loadingSolicitudes ? (
              <div className={styles.emptyState}>
                <Spinner animation="border" size="sm" className="me-2" />
                Cargando solicitudes...
              </div>
            ) : solicitudes.length === 0 ? (
              <div className={styles.emptyState}>
                {esApoderado ? 'No has enviado solicitudes de matrícula.' : 'No hay solicitudes de matrícula.'}
              </div>
            ) : (
              <div className={styles.tableWrap}>
                <Table hover responsive className={styles.table}>
                  <thead className={styles.tableHead}>
                    <tr>
                      <th>#</th>
                      <th>Alumno</th>
                      {canManage && <th>Apoderado</th>}
                      <th>Curso deseado</th>
                      <th>Tipo</th>
                      <th>Fecha</th>
                      <th>Estado</th>
                      {canManage && <th className="text-end">Acciones</th>}
                      {esApoderado && <th>Motivo rechazo</th>}
                    </tr>
                  </thead>
                  <tbody>
                    {solicitudes.map((s) => (
                      <tr key={s.idSolicitud}>
                        <td>{s.idSolicitud}</td>
                        <td>{s.alumnoRut}</td>
                        {canManage && <td>{s.apoderadoRut}</td>}
                        <td>
                          {canManage && s.estado === 'PENDIENTE' ? (
                            <Form.Select
                              size="sm"
                              value={cursoElegido[s.idSolicitud] ?? s.cursoId ?? ''}
                              onChange={(e) =>
                                setCursoElegido((prev) => ({ ...prev, [s.idSolicitud]: e.target.value }))
                              }
                            >
                              <option value="">Sin curso</option>
                              {cursos.map((c) => (
                                <option key={c.idCur} value={c.idCur}>
                                  {c.nivel?.nivNum ?? '—'}°{c.curLetraSeccion} ({c.curAnioEscolar})
                                </option>
                              ))}
                            </Form.Select>
                          ) : (
                            cursoLabel(s.cursoId)
                          )}
                        </td>
                        <td>
                          <Badge bg={TIPO_BADGE[s.tipoAlumno] || 'secondary'}>{s.tipoAlumno || 'N/D'}</Badge>
                        </td>
                        <td>{formatDate(s.fechaSolicitud)}</td>
                        <td>
                          <Badge bg={ESTADO_SOLICITUD_BADGE[s.estado] || 'secondary'}>{s.estado}</Badge>
                        </td>
                        {canManage && (
                          <td className="text-end">
                            {s.estado === 'PENDIENTE' && (
                              <>
                                <Button
                                  size="sm"
                                  variant="outline-success"
                                  className="me-2"
                                  disabled={procesandoId === s.idSolicitud}
                                  onClick={() => handleAprobarSolicitud(s)}
                                >
                                  Aprobar
                                </Button>
                                <Button
                                  size="sm"
                                  variant="outline-danger"
                                  disabled={procesandoId === s.idSolicitud}
                                  onClick={() => handleRechazarSolicitud(s)}
                                >
                                  Rechazar
                                </Button>
                              </>
                            )}
                          </td>
                        )}
                        {esApoderado && <td>{s.motivoRechazo || '—'}</td>}
                      </tr>
                    ))}
                  </tbody>
                </Table>
              </div>
            )}
          </Tab>
        )}
        </Tabs>
      </main>

      <MatriculaForm
        show={showForm}
        matricula={editTarget}
        defaultRut={userRut}
        saving={saving}
        onSave={handleSave}
        onClose={() => {
          setShowForm(false)
          setEditTarget(null)
        }}
      />
    </div>
  )
}
