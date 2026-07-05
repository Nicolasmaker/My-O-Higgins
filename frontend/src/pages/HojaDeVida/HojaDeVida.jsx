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
import { Row, Col, ListGroup, Accordion, Badge, Button, Alert, Spinner, Table } from 'react-bootstrap'
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
} from '../../services/hojaDeVidaService'
import HojaVidaForm from '../../components/HojaDeVida/HojaVidaForm'
import AntecedenteForm from '../../components/HojaDeVida/AntecedenteForm'
import styles from './HojaDeVida.module.css'

const ROLES_GESTION = ['ROLE_DOCENTE', 'ROLE_INSPECTOR', 'ROLE_DIRECTIVO']

// API por tipo de antecedente (crear / actualizar / eliminar)
const ANTECEDENTE_API = {
  academico: { crear: crearAntecedenteAcademico, actualizar: actualizarAntecedenteAcademico, eliminar: eliminarAntecedenteAcademico, idKey: 'idAntAcad' },
  apoderado: { crear: crearAntecedenteApoderado, actualizar: actualizarAntecedenteApoderado, eliminar: eliminarAntecedenteApoderado, idKey: 'idAntApo' },
  medico: { crear: crearAntecedenteMedico, actualizar: actualizarAntecedenteMedico, eliminar: eliminarAntecedenteMedico, idKey: 'idAntMed' },
}

export default function HojaDeVida() {
  const { hasRole } = useAuth()
  const canManage = hasRole(ROLES_GESTION)

  const [hojas, setHojas] = useState([])
  const [academicos, setAcademicos] = useState([])
  const [apoderados, setApoderados] = useState([])
  const [medicos, setMedicos] = useState([])
  const [loading, setLoading] = useState(true)
  const [loadError, setLoadError] = useState('')

  const [selectedId, setSelectedId] = useState(null)
  const [busqueda, setBusqueda] = useState('')

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
    if (!q) return hojas
    return hojas.filter(
      (h) => String(h.estudianteUsuRut ?? '').includes(q) || String(h.idHojaVida ?? '').includes(q)
    )
  }, [hojas, busqueda])

  const selected = hojas.find((h) => h.idHojaVida === selectedId) || null

  const antecedentesDeHoja = useMemo(
    () => ({
      academico: academicos.filter((a) => a.idHojaVida === selectedId),
      apoderado: apoderados.filter((a) => a.idHojaVida === selectedId),
      medico: medicos.filter((a) => a.idHojaVida === selectedId),
    }),
    [academicos, apoderados, medicos, selectedId]
  )

  // ── CRUD hoja de vida ───────────────────────────────────────
  const handleSaveHoja = async (data) => {
    setSaving(true)
    const payload = {
      estudianteUsuRut: limpiarRut(data.estudianteUsuRut),
      matriculaId: Number(data.matriculaId),
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
                placeholder="Buscar por RUT o N° de hoja"
                value={busqueda}
                onChange={(e) => setBusqueda(e.target.value)}
              />
              {hojasFiltradas.length === 0 ? (
                <div className={styles.emptyState}>
                  {hojas.length === 0 ? 'No hay hojas de vida registradas.' : 'Sin coincidencias.'}
                </div>
              ) : (
                <ListGroup className={styles.hojaList}>
                  {hojasFiltradas.map((h) => (
                    <ListGroup.Item
                      key={h.idHojaVida}
                      action
                      active={h.idHojaVida === selectedId}
                      onClick={() => setSelectedId(h.idHojaVida)}
                      className={styles.hojaItem}
                    >
                      <strong>Hoja #{h.idHojaVida}</strong>
                      <span className={styles.hojaItemMeta}>Estudiante: {h.estudianteUsuRut}</span>
                      <span className={styles.hojaItemMeta}>Matrícula: {h.matriculaId}</span>
                    </ListGroup.Item>
                  ))}
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
                      <h2 className={styles.detailTitle}>Hoja de vida #{selected.idHojaVida}</h2>
                      <span className={styles.detailMeta}>
                        Estudiante RUT {selected.estudianteUsuRut} · Matrícula #{selected.matriculaId}
                      </span>
                    </div>
                    {canManage && (
                      <div className="d-flex gap-2">
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
                      </div>
                    )}
                  </header>

                  <Accordion defaultActiveKey="academico" alwaysOpen className={styles.accordion}>
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
