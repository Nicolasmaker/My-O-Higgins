// =============================================================
// PÁGINA GESTIÓN ACADÉMICA — Academico.jsx
// =============================================================
// Panel del MS-GestionAcademica (puerto 8087). Administra las 7
// entidades académicas desde una sola vista:
//
//   Cursos · Asignaturas · Niveles · Salas · Evaluaciones ·
//   Bitácoras de asignatura · Notas
//
// Nav lateral de secciones + tabla genérica + modal genérico,
// todo alimentado por entidadesConfig.jsx. Cada sección carga
// su listado al seleccionarse (lazy, no las 7 de una vez).
// =============================================================
import { useCallback, useEffect, useMemo, useState } from 'react'
import PropTypes from 'prop-types'
import { Row, Col, Nav, Table, Badge, Button, Alert, Spinner, Form } from 'react-bootstrap'
import { toast } from 'react-toastify'
import { useAuth } from '../../hooks/useAuth'
import { getNotasByEstudiante } from '../../services/academicoService'
import Navbar from '../../components/Navbar/Navbar'
import Footer from '../../components/Footer/Footer'
import EntidadForm from '../../components/Academico/EntidadForm'
import { ENTIDADES } from '../../components/Academico/entidadesConfig'
import styles from '../../styles/Academico.module.css'

const ROLES_GESTION = ['ROLE_DOCENTE', 'ROLE_INSPECTOR', 'ROLE_DIRECTIVO']
const SECCIONES = Object.keys(ENTIDADES)

// ── Vista simplificada para estudiantes: solo sus notas ──────
// Usa GET /notas/estudiante/{rut}; sin acciones de gestión.
function MisNotas({ rut }) {
  const [notas, setNotas] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    if (!rut) return
    getNotasByEstudiante(rut)
      .then((res) => setNotas(Array.isArray(res.data) ? res.data : []))
      .catch((e) => {
        console.error(e)
        setError(e.response?.data?.message || 'No se pudieron cargar tus notas')
      })
      .finally(() => setLoading(false))
  }, [rut])

  const promedio = useMemo(() => {
    if (notas.length === 0) return null
    const suma = notas.reduce((acc, n) => acc + Number(n.notCalif || 0), 0)
    return (suma / notas.length).toFixed(1)
  }, [notas])

  if (loading) {
    return (
      <div className={styles.emptyState}>
        <Spinner animation="border" size="sm" className="me-2" />
        Cargando tus notas...
      </div>
    )
  }
  if (error) return <Alert variant="danger">{error}</Alert>
  if (notas.length === 0) return <div className={styles.emptyState}>Aún no tienes notas registradas.</div>

  return (
    <>
      <div className={styles.sectionToolbar}>
        <h2 className={styles.sectionTitle}>Mis notas</h2>
        <span>
          Promedio general:{' '}
          <Badge bg={promedio >= 4 ? 'success' : 'danger'}>{promedio}</Badge>
        </span>
      </div>
      <div className={styles.tableWrap}>
        <Table hover responsive className={styles.table}>
          <thead className={styles.tableHead}>
            <tr>
              <th>Evaluación</th>
              <th>Calificación</th>
              <th>Fecha</th>
            </tr>
          </thead>
          <tbody>
            {notas.map((n) => (
              <tr key={n.idNot}>
                <td>{n.evaluacion?.evaNom ?? '—'}</td>
                <td>
                  <Badge bg={n.notCalif >= 4 ? 'success' : 'danger'}>{Number(n.notCalif).toFixed(1)}</Badge>
                </td>
                <td>
                  {n.notFechaRegistrada
                    ? new Date(`${n.notFechaRegistrada}T00:00:00`).toLocaleDateString('es-CL')
                    : '—'}
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>
    </>
  )
}

MisNotas.propTypes = {
  rut: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
}

export default function Academico() {
  const { usuario, hasRole } = useAuth()
  const canManage = hasRole(ROLES_GESTION)
  const esEstudiante = hasRole('ROLE_ESTUDIANTE')

  const [seccion, setSeccion] = useState('curso')
  const [items, setItems] = useState([])
  const [loading, setLoading] = useState(true)
  const [loadError, setLoadError] = useState('')
  const [busqueda, setBusqueda] = useState('')

  const [showForm, setShowForm] = useState(false)
  const [editItem, setEditItem] = useState(null)
  const [saving, setSaving] = useState(false)

  const config = ENTIDADES[seccion]

  const load = useCallback(async () => {
    setLoading(true)
    setLoadError('')
    try {
      const res = await config.api.list()
      setItems(Array.isArray(res.data) ? res.data : [])
    } catch (error) {
      console.error(error)
      const message = error.response?.data?.message || `No se pudieron cargar ${config.titulo.toLowerCase()}`
      setLoadError(message)
      toast.error(message)
      setItems([])
    } finally {
      setLoading(false)
    }
  }, [config])

  useEffect(() => {
    load()
  }, [load])

  const cambiarSeccion = (key) => {
    setSeccion(key)
    setBusqueda('')
    setEditItem(null)
    setShowForm(false)
  }

  // Filtro local: busca el texto en todas las celdas visibles de la fila
  const filtrados = useMemo(() => {
    const q = busqueda.trim().toLowerCase()
    if (!q) return items
    return items.filter((item) =>
      config.columnas.some((col) => {
        const rendered = col.render(item)
        const texto = typeof rendered === 'object' ? JSON.stringify(item) : String(rendered ?? '')
        return texto.toLowerCase().includes(q)
      })
    )
  }, [items, busqueda, config])

  const handleSave = async (data) => {
    setSaving(true)
    try {
      if (editItem) {
        await config.api.actualizar(editItem[config.idKey], config.payload(data, true))
        toast.success('Registro actualizado')
      } else {
        await config.api.crear(config.payload(data, false))
        toast.success('Registro creado')
      }
      setShowForm(false)
      setEditItem(null)
      await load()
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || error.response?.data || 'No se pudo guardar')
    } finally {
      setSaving(false)
    }
  }

  const handleDelete = async (item) => {
    if (!window.confirm(`¿Eliminar ${config.singular} #${item[config.idKey]}?`)) return
    try {
      await config.api.eliminar(item[config.idKey])
      toast.success('Registro eliminado')
      await load()
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || 'No se pudo eliminar (puede tener registros asociados)')
    }
  }

  // Estudiantes ven solo su libreta de notas, sin panel de gestión
  if (esEstudiante) {
    return (
      <div className={styles.page}>
        <Navbar />
        <main className={styles.shell}>
          <header className={styles.pageHeader}>
            <div>
              <h1 className={styles.title}>Mi Libreta de Notas</h1>
              <p className={styles.subtitle}>Calificaciones registradas en el año académico</p>
            </div>
          </header>
          <MisNotas rut={usuario?.usuRut} />
        </main>
        <Footer />
      </div>
    )
  }

  return (
    <div className={styles.page}>
      <Navbar />

      <main className={styles.shell}>
        <header className={styles.pageHeader}>
          <div>
            <p className={styles.eyebrow}>MS-GestionAcademica</p>
            <h1 className={styles.title}>Gestión Académica</h1>
            <p className={styles.subtitle}>
              Cursos, asignaturas, evaluaciones, notas y bitácoras de clase
            </p>
          </div>
          {canManage && (
            <Button
              size="lg"
              className={styles.btnGranate}
              onClick={() => {
                setEditItem(null)
                setShowForm(true)
              }}
            >
              {config.nuevoLabel}
            </Button>
          )}
        </header>

        <Row className="g-3">
          {/* ── Nav lateral de secciones ── */}
          <Col md={3} lg={2}>
            <Nav variant="pills" className={`flex-column ${styles.sideNav}`} activeKey={seccion} onSelect={cambiarSeccion}>
              {SECCIONES.map((key) => (
                <Nav.Item key={key}>
                  <Nav.Link eventKey={key} className={styles.sideNavLink}>
                    {ENTIDADES[key].titulo}
                  </Nav.Link>
                </Nav.Item>
              ))}
            </Nav>
          </Col>

          {/* ── Tabla de la sección activa ── */}
          <Col md={9} lg={10}>
            <div className={styles.sectionToolbar}>
              <h2 className={styles.sectionTitle}>{config.titulo}</h2>
              <Form.Control
                size="sm"
                className={styles.searchInput}
                placeholder={`Buscar en ${config.titulo.toLowerCase()}...`}
                value={busqueda}
                onChange={(e) => setBusqueda(e.target.value)}
              />
            </div>

            {loading ? (
              <div className={styles.emptyState}>
                <Spinner animation="border" size="sm" className="me-2" />
                Cargando {config.titulo.toLowerCase()}...
              </div>
            ) : loadError ? (
              <Alert variant="danger">{loadError}</Alert>
            ) : filtrados.length === 0 ? (
              <div className={styles.emptyState}>
                {items.length === 0
                  ? `No hay ${config.titulo.toLowerCase()} registradas.`
                  : 'Sin coincidencias para la búsqueda.'}
              </div>
            ) : (
              <div className={styles.tableWrap}>
                <Table hover responsive className={styles.table}>
                  <thead className={styles.tableHead}>
                    <tr>
                      {config.columnas.map((col) => (
                        <th key={col.label}>{col.label}</th>
                      ))}
                      {canManage && <th className="text-end">Acciones</th>}
                    </tr>
                  </thead>
                  <tbody>
                    {filtrados.map((item) => (
                      <tr key={item[config.idKey]}>
                        {config.columnas.map((col) => (
                          <td key={col.label}>{col.render(item)}</td>
                        ))}
                        {canManage && (
                          <td className="text-end">
                            <Button
                              variant="outline-secondary"
                              className="me-2"
                              onClick={() => {
                                setEditItem(item)
                                setShowForm(true)
                              }}
                            >
                              Editar
                            </Button>
                            <Button variant="outline-danger" onClick={() => handleDelete(item)}>
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
          </Col>
        </Row>
      </main>

      <EntidadForm
        show={showForm}
        config={config}
        item={editItem}
        saving={saving}
        onSave={handleSave}
        onClose={() => {
          setShowForm(false)
          setEditItem(null)
        }}
      />

    </div>
  )
}
