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
import { getNotasByEstudiante, getCursoById, getImpartirByDocente, getAsignaturas, getCursos } from '../../services/academicoService'
import { getMatriculas } from '../../services/matriculaService'
import { getEstudiante } from '../../services/authService'
import EntidadForm from '../../components/Academico/EntidadForm'
import { ENTIDADES } from '../../components/Academico/entidadesConfig'
import styles from '../../styles/Academico.module.css'

// Directivo: acceso completo (rw) a las 8 secciones.
// Docente: rw solo en Notas/Bitácoras; lectura filtrada a lo suyo en
// Asignatura/Curso/Sala (vía Impartir); sin acceso a Nivel/Evaluación/Asignaciones.
const PERMISOS_DOCENTE = {
  notas: 'rw',
  bitacora: 'rw',
  evaluacion: 'rw-propio',
  asignatura: 'ro-propio',
  curso: 'ro-propio',
  sala: 'ro-propio',
}
// Directivo: rw en todas las secciones, salvo Bitácora donde puede ver/editar/eliminar
// pero no crear (las bitácoras de clase son responsabilidad del docente).
const PERMISOS_DIRECTIVO_OVERRIDE = {
  bitacora: 'rw-sin-crear',
}
const SECCIONES = Object.keys(ENTIDADES)

// ── Vista simplificada para estudiantes y apoderados: modo lectura ──────
function VistaEstudianteApoderado({ usuario, isApoderado }) {
  const [hijos, setHijos] = useState([])
  const [selectedRut, setSelectedRut] = useState('')
  const [seccion, setSeccion] = useState('notas')
  
  // datos del estudiante seleccionado
  const [notas, setNotas] = useState([])
  const [evaluaciones, setEvaluaciones] = useState([])
  const [asignaturas, setAsignaturas] = useState([])
  const [curso, setCurso] = useState(null)
  
  const [loading, setLoading] = useState(true)

  // 1. Cargar hijos si es apoderado
  useEffect(() => {
    if (isApoderado) {
       getMatriculas().then(async res => {
         const matriculasHijos = (res.data || []).filter(m => String(m.apoderadoRut) === String(usuario.usuRut))
         const ruts = [...new Set(matriculasHijos.map(m => m.alumnoRut))]
         
         const hijosData = await Promise.all(ruts.map(async (rut) => {
           try {
             const estRes = await getEstudiante(rut);
             const data = estRes.data;
             return { rut, nombre: `${data.usuPNombre} ${data.usuApePat}` };
           } catch (e) {
             return { rut, nombre: `Estudiante RUT: ${rut}` };
           }
         }));
         
         setHijos(hijosData)
         if (hijosData.length > 0) setSelectedRut(hijosData[0].rut)
         else setLoading(false)
       }).catch(console.error)
    } else {
       setSelectedRut(usuario.usuRut)
    }
  }, [isApoderado, usuario.usuRut])

  // 2. Cargar datos del rut seleccionado
  useEffect(() => {
    if (!selectedRut) return
    setLoading(true)
    
    Promise.all([
      getNotasByEstudiante(selectedRut).catch(() => ({ data: [] })),
      getEstudiante(selectedRut).catch(() => ({ data: null }))
    ]).then(async ([resNotas, resEst]) => {
      const notasData = resNotas.data || []
      setNotas(notasData)
      
      const evsMap = new Map()
      const asigMap = new Map()
      notasData.forEach(n => {
        if (n.evaluacion) {
          evsMap.set(n.evaluacion.idEva, n.evaluacion)
          if (n.evaluacion.asignatura) {
             asigMap.set(n.evaluacion.asignatura.idAsi, n.evaluacion.asignatura)
          }
        }
      })
      setEvaluaciones(Array.from(evsMap.values()))
      setAsignaturas(Array.from(asigMap.values()))
      
      const estudiante = resEst.data
      if (estudiante?.cursoId) {
         try {
           const resCurso = await getCursoById(estudiante.cursoId)
           setCurso(resCurso.data)
         } catch(e) { console.error(e) }
      } else {
         setCurso(null)
      }
      setLoading(false)
    })
  }, [selectedRut])

  if (loading && !selectedRut) return <div className={styles.emptyState}><Spinner animation="border"/></div>
  if (isApoderado && hijos.length === 0 && !loading) return <Alert variant="info">No tienes estudiantes asociados a tu RUT.</Alert>

  const renderContent = () => {
    if (seccion === 'notas') {
       const promedio = notas.length > 0 
          ? (notas.reduce((acc, n) => acc + Number(n.notCalif || 0), 0) / notas.length).toFixed(1)
          : null

       return (
         <>
           <div className="mb-3">
             {promedio && <strong>Promedio general: <Badge bg={promedio >= 4 ? 'success' : 'danger'}>{promedio}</Badge></strong>}
           </div>
           <Table hover responsive className={styles.table}>
             <thead className={styles.tableHead}>
               <tr><th>Evaluación</th><th>Calificación</th><th>Fecha</th></tr>
             </thead>
             <tbody>
               {notas.length === 0 ? <tr><td colSpan="3">No hay notas registradas.</td></tr> : 
                 notas.map(n => (
                   <tr key={n.idNot}>
                     <td>{n.evaluacion?.evaNom ?? '—'}</td>
                     <td><Badge bg={n.notCalif >= 4 ? 'success' : 'danger'}>{Number(n.notCalif).toFixed(1)}</Badge></td>
                     <td>{n.notFechaRegistrada ? new Date(`${n.notFechaRegistrada}T00:00:00`).toLocaleDateString('es-CL') : '—'}</td>
                   </tr>
                 ))
               }
             </tbody>
           </Table>
         </>
       )
    }
    if (seccion === 'evaluacion') {
       return (
         <Table hover responsive className={styles.table}>
           <thead className={styles.tableHead}>
             <tr><th>Nombre</th><th>Asignatura</th><th>Tipo</th><th>Fecha</th></tr>
           </thead>
           <tbody>
             {evaluaciones.length === 0 ? <tr><td colSpan="4">No hay evaluaciones vinculadas a notas.</td></tr> :
               evaluaciones.map(e => (
                 <tr key={e.idEva}>
                   <td>{e.evaNom}</td>
                   <td>{e.asignatura?.asiNombre ?? '—'}</td>
                   <td><Badge bg="secondary">{e.evaTipo}</Badge></td>
                   <td>{e.evaFecha ? new Date(`${e.evaFecha}T00:00:00`).toLocaleDateString('es-CL') : '—'}</td>
                 </tr>
               ))
             }
           </tbody>
         </Table>
       )
    }
    if (seccion === 'asignatura') {
       return (
         <Table hover responsive className={styles.table}>
           <thead className={styles.tableHead}>
             <tr><th>Nombre</th><th>Descripción</th></tr>
           </thead>
           <tbody>
             {asignaturas.length === 0 ? <tr><td colSpan="2">No hay asignaturas vinculadas a notas.</td></tr> :
               asignaturas.map(a => (
                 <tr key={a.idAsi}>
                   <td>{a.asiNombre}</td>
                   <td>{a.asiDescripcion}</td>
                 </tr>
               ))
             }
           </tbody>
         </Table>
       )
    }
    if (seccion === 'curso') {
       if (!curso) return <div className="mt-3">El estudiante no tiene un curso asignado o no se pudo cargar.</div>
       return (
         <Table hover responsive className={styles.table}>
           <thead className={styles.tableHead}>
             <tr><th>Nivel</th><th>Sección</th><th>Año</th><th>Sala</th><th>Capacidad</th></tr>
           </thead>
           <tbody>
             <tr>
               <td>{curso.nivel?.nivNum ?? '—'}</td>
               <td>{curso.curLetraSeccion}</td>
               <td>{curso.curAnioEscolar}</td>
               <td>{curso.sala?.salLetra ?? '—'}</td>
               <td>{curso.sala?.salaCapacidad ?? '—'}</td>
             </tr>
           </tbody>
         </Table>
       )
    }
  }

  return (
    <>
      {isApoderado && (
        <div className="mb-4 p-3 bg-white rounded shadow-sm border">
          <h5 className="mb-3">Seleccionar estudiante:</h5>
          <div className="d-flex flex-wrap gap-4">
            {hijos.map(hijo => (
              <Form.Check 
                key={hijo.rut}
                type="radio"
                id={`radio-${hijo.rut}`}
                label={hijo.nombre}
                name="hijoSelect"
                checked={String(selectedRut) === String(hijo.rut)}
                onChange={() => setSelectedRut(hijo.rut)}
                inline
                className={`fs-6 ${styles.radioGranate}`}
              />
            ))}
          </div>
        </div>
      )}
      
      <Row className="g-4">
        <Col md={4} lg={3}>
          <Nav variant="pills" className={`flex-column ${styles.sideNav}`} activeKey={seccion} onSelect={setSeccion}>
            <Nav.Item><Nav.Link eventKey="notas" className={styles.sideNavLink}>Notas</Nav.Link></Nav.Item>
            <Nav.Item><Nav.Link eventKey="evaluacion" className={styles.sideNavLink}>Evaluaciones</Nav.Link></Nav.Item>
            <Nav.Item><Nav.Link eventKey="asignatura" className={styles.sideNavLink}>Asignaturas</Nav.Link></Nav.Item>
            <Nav.Item><Nav.Link eventKey="curso" className={styles.sideNavLink}>Curso y Sala</Nav.Link></Nav.Item>
          </Nav>
        </Col>
        <Col md={8} lg={9}>
          <div className="bg-white rounded shadow-sm border p-4">
            <div className={styles.sectionToolbar}>
              <h2 className={styles.sectionTitle}>
                {seccion === 'notas' && 'Notas registradas'}
                {seccion === 'evaluacion' && 'Evaluaciones evaluadas'}
                {seccion === 'asignatura' && 'Asignaturas cursadas'}
                {seccion === 'curso' && 'Información de Curso'}
              </h2>
            </div>
            {loading ? <Spinner animation="border" size="sm"/> : (
              <div className={styles.tableWrap}>
                {renderContent()}
              </div>
            )}
          </div>
        </Col>
      </Row>
    </>
  )
}

VistaEstudianteApoderado.propTypes = {
  usuario: PropTypes.object.isRequired,
  isApoderado: PropTypes.bool.isRequired,
}

export default function Academico() {
  const { usuario, hasRole } = useAuth()
  const esEstudiante = hasRole('ROLE_ESTUDIANTE')
  const esApoderado = hasRole('ROLE_APODERADO')
  const esDocente = hasRole('ROLE_DOCENTE')
  const esDirectivo = hasRole('ROLE_DIRECTIVO')

  // Secciones visibles en el nav lateral según el rol
  const seccionesVisibles = esDirectivo ? SECCIONES : SECCIONES.filter((key) => PERMISOS_DOCENTE[key])

  const [seccion, setSeccion] = useState('curso')
  const [items, setItems] = useState([])
  const [loading, setLoading] = useState(true)
  const [loadError, setLoadError] = useState('')
  const [busqueda, setBusqueda] = useState('')

  const [showForm, setShowForm] = useState(false)
  const [editItem, setEditItem] = useState(null)
  const [saving, setSaving] = useState(false)

  const config = ENTIDADES[seccion]
  const permisoSeccion = esDirectivo
    ? PERMISOS_DIRECTIVO_OVERRIDE[seccion] || 'rw'
    : PERMISOS_DOCENTE[seccion] || 'ninguno'
  const canCreate = permisoSeccion === 'rw' || permisoSeccion === 'rw-propio'
  const canEditDelete = permisoSeccion === 'rw' || permisoSeccion === 'rw-sin-crear' || permisoSeccion === 'rw-propio'
  const soloLecturaPropio = permisoSeccion === 'ro-propio'
  const soloRwPropio = permisoSeccion === 'rw-propio'

  // Docente: carga sus asignaciones (Impartir) para filtrar Asignatura/Curso/Sala a lo suyo
  const [impartirDocente, setImpartirDocente] = useState([])
  useEffect(() => {
    if (!esDocente || !usuario?.usuRut) return
    getImpartirByDocente(usuario.usuRut)
      .then((res) => setImpartirDocente(Array.isArray(res.data) ? res.data : []))
      .catch(() => setImpartirDocente([]))
  }, [esDocente, usuario?.usuRut])

  const idsAsignaturaPropias = useMemo(
    () => new Set(impartirDocente.map((i) => i.asignatura?.idAsi).filter(Boolean)),
    [impartirDocente]
  )
  const idsCursoPropios = useMemo(
    () => new Set(impartirDocente.map((i) => i.curso?.idCur).filter(Boolean)),
    [impartirDocente]
  )
  const idsSalaPropias = useMemo(
    () => new Set(impartirDocente.map((i) => i.curso?.sala?.idSal).filter(Boolean)),
    [impartirDocente]
  )

  // Bitácora y Evaluación: el dropdown de asignatura muestra solo lo que el docente dicta;
  // Directivo (sin carga propia) ve el catálogo completo.
  const configForForm = useMemo(() => {
    if (seccion !== 'bitacora' && seccion !== 'evaluacion') return config
    return {
      ...config,
      campos: config.campos.map((c) => {
        if (c.name === 'idAsignatura') {
          return {
            ...c,
            loadOptions: () =>
              esDirectivo
                ? getAsignaturas().then((r) => r.data)
                : Promise.resolve(
                    Array.from(
                      new Map(
                        impartirDocente
                          .map((i) => i.asignatura)
                          .filter(Boolean)
                          .map((a) => [a.idAsi, a])
                      ).values()
                    )
                  ),
          }
        }
        if (c.name === 'idCurso' && seccion === 'evaluacion') {
          return {
            ...c,
            loadOptions: () =>
              esDirectivo
                ? getCursos().then((r) => r.data)
                : Promise.resolve(
                    Array.from(
                      new Map(
                        impartirDocente
                          .map((i) => i.curso)
                          .filter(Boolean)
                          .map((cur) => [cur.idCur, cur])
                      ).values()
                    )
                  ),
          }
        }
        return c
      }),
    }
  }, [config, seccion, esDirectivo, impartirDocente])

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

  // Docente en modo ro-propio: recorta la lista a lo que le corresponde por Impartir.
  // Docente en modo rw-propio (Evaluación): recorta a lo que él mismo registró.
  const itemsVisibles = useMemo(() => {
    if (soloRwPropio && seccion === 'evaluacion') {
      return items.filter((i) => String(i.docenteUsuRut) === String(usuario?.usuRut))
    }
    if (!soloLecturaPropio) return items
    if (seccion === 'asignatura') return items.filter((i) => idsAsignaturaPropias.has(i.idAsi))
    if (seccion === 'curso') return items.filter((i) => idsCursoPropios.has(i.idCur))
    if (seccion === 'sala') return items.filter((i) => idsSalaPropias.has(i.idSal))
    return items
  }, [items, soloLecturaPropio, soloRwPropio, seccion, idsAsignaturaPropias, idsCursoPropios, idsSalaPropias, usuario?.usuRut])

  // Filtro local: busca el texto en todas las celdas visibles de la fila
  const filtrados = useMemo(() => {
    const q = busqueda.trim().toLowerCase()
    if (!q) return itemsVisibles
    return itemsVisibles.filter((item) =>
      config.columnas.some((col) => {
        const rendered = col.render(item)
        const texto = typeof rendered === 'object' ? JSON.stringify(item) : String(rendered ?? '')
        return texto.toLowerCase().includes(q)
      })
    )
  }, [itemsVisibles, busqueda, config])

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

  // Estudiantes y apoderados ven una vista adaptada (modo lectura)
  if (esEstudiante || esApoderado) {
    return (
      <div className={styles.page}>
        <main className={styles.shell}>
          <header className={styles.pageHeader}>
            <div>
              <p className={styles.eyebrow}>MS-GestionAcademica</p>
              <h1 className={styles.title}>
                {esEstudiante ? 'Mi Información Académica' : 'Información Académica del Estudiante'}
              </h1>
              <p className={styles.subtitle}>
                {esEstudiante ? 'Consulta tus notas, evaluaciones, asignaturas y curso' : 'Consulta las notas, evaluaciones, asignaturas y curso de tus hijos'}
              </p>
            </div>
          </header>
          <VistaEstudianteApoderado usuario={usuario} isApoderado={esApoderado} />
        </main>
      </div>
    )
  }

  return (
    <div className={styles.page}>
      <main className={styles.shell}>
        <header className={styles.pageHeader}>
          <div>
            <p className={styles.eyebrow}>MS-GestionAcademica</p>
            <h1 className={styles.title}>Gestión Académica</h1>
            <p className={styles.subtitle}>
              Cursos, asignaturas, evaluaciones, notas y bitácoras de clase
            </p>
          </div>
          {canCreate && (
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
              {seccionesVisibles.map((key) => (
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
                {itemsVisibles.length === 0
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
                      {canEditDelete && <th className="text-end">Acciones</th>}
                    </tr>
                  </thead>
                  <tbody>
                    {filtrados.map((item) => (
                      <tr key={item[config.idKey]}>
                        {config.columnas.map((col) => (
                          <td key={col.label}>{col.render(item)}</td>
                        ))}
                        {canEditDelete && (
                          <td className="text-end">
                            {!config.sinEditar && (
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
                            )}
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
        config={configForForm}
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
