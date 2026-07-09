// =============================================================
// WIZARD "NUEVA MATRÍCULA" — NuevaMatriculaWizard.jsx
// =============================================================
// Flujo guiado para el Directivo cuando llega un estudiante nuevo al
// colegio (no tiene cuenta todavía, ni él ni su apoderado):
//   Paso 1: Apoderado — busca por RUT; si no existe, lo crea.
//   Paso 2: Estudiante — crea la cuenta nueva del alumno.
//   Paso 3: Matrícula — curso, tipo de alumno, parentesco.
// Si el estudiante YA tiene cuenta (re-matrícula de un año a otro),
// no se usa este wizard: se usa "Editar"/el registro directo de
// matrícula, que ya existía.
// =============================================================
import { useEffect, useState } from 'react'
import PropTypes from 'prop-types'
import { Modal, Form, Row, Col, Button, Spinner, Alert, ProgressBar } from 'react-bootstrap'
import { useForm } from 'react-hook-form'
import { toast } from 'react-toastify'
import {
  getApoderado,
  crearApoderado,
  crearEstudiante,
  getComunas,
} from '../../services/authService'
import { crearMatricula, getMatriculas } from '../../services/matriculaService'
import { getCursos } from '../../services/academicoService'
import { formatNivel } from '../Academico/entidadesConfig'
import { rutConDvRules, rutValidoConDv, emailRules, passwordRules, limpiarRut, extraerDv } from '../../validators/fieldValidators'

const TIPO_CASA = ['Casa', 'Departamento']

function CamposPersona({ register, errors, prefijoLabel }) {
  return (
    <>
      <Row className="g-3">
        <Col md={6}>
          <Form.Group controlId="primerNombre">
            <Form.Label>Primer nombre *</Form.Label>
            <Form.Control isInvalid={!!errors.primerNombre} {...register('primerNombre', { required: 'Obligatorio' })} />
            <Form.Control.Feedback type="invalid">{errors.primerNombre?.message}</Form.Control.Feedback>
          </Form.Group>
        </Col>
        <Col md={6}>
          <Form.Group controlId="segundoNombre">
            <Form.Label>Segundo nombre</Form.Label>
            <Form.Control {...register('segundoNombre')} />
          </Form.Group>
        </Col>
        <Col md={6}>
          <Form.Group controlId="apellidoPat">
            <Form.Label>Apellido paterno *</Form.Label>
            <Form.Control isInvalid={!!errors.apellidoPat} {...register('apellidoPat', { required: 'Obligatorio' })} />
            <Form.Control.Feedback type="invalid">{errors.apellidoPat?.message}</Form.Control.Feedback>
          </Form.Group>
        </Col>
        <Col md={6}>
          <Form.Group controlId="apellidoMat">
            <Form.Label>Apellido materno *</Form.Label>
            <Form.Control isInvalid={!!errors.apellidoMat} {...register('apellidoMat', { required: 'Obligatorio' })} />
            <Form.Control.Feedback type="invalid">{errors.apellidoMat?.message}</Form.Control.Feedback>
          </Form.Group>
        </Col>
        <Col md={6}>
          <Form.Group controlId="email">
            <Form.Label>Email *</Form.Label>
            <Form.Control isInvalid={!!errors.email} {...register('email', emailRules)} />
            <Form.Control.Feedback type="invalid">{errors.email?.message}</Form.Control.Feedback>
          </Form.Group>
        </Col>
        <Col md={6}>
          <Form.Group controlId="tel">
            <Form.Label>Teléfono *</Form.Label>
            <Form.Control
              placeholder="+56912345678"
              isInvalid={!!errors.tel}
              {...register('tel', { required: 'Obligatorio — usu_tel es NOT NULL en la base de datos' })}
            />
            <Form.Control.Feedback type="invalid">{errors.tel?.message}</Form.Control.Feedback>
          </Form.Group>
        </Col>
        <Col md={6}>
          <Form.Group controlId="password">
            <Form.Label>Contraseña inicial *</Form.Label>
            <Form.Control type="password" isInvalid={!!errors.password} {...register('password', passwordRules)} />
            <Form.Control.Feedback type="invalid">{errors.password?.message}</Form.Control.Feedback>
          </Form.Group>
        </Col>
        <Col md={6}>
          <Form.Group controlId="tipoCasa">
            <Form.Label>Tipo de vivienda *</Form.Label>
            <Form.Select {...register('tipoCasa', { required: true })}>
              {TIPO_CASA.map((t) => (
                <option key={t} value={t}>{t}</option>
              ))}
            </Form.Select>
          </Form.Group>
        </Col>
        <Col md={8}>
          <Form.Group controlId="direccion">
            <Form.Label>Dirección *</Form.Label>
            <Form.Control
              placeholder={`Calle de ${prefijoLabel}`}
              isInvalid={!!errors.direccion}
              {...register('direccion', { required: 'Obligatorio — dir_nom es NOT NULL' })}
            />
            <Form.Control.Feedback type="invalid">{errors.direccion?.message}</Form.Control.Feedback>
          </Form.Group>
        </Col>
        <Col md={4}>
          <Form.Group controlId="numeroDireccion">
            <Form.Label>Número *</Form.Label>
            <Form.Control
              type="number"
              isInvalid={!!errors.numeroDireccion}
              {...register('numeroDireccion', { required: 'Obligatorio — dir_num es NOT NULL' })}
            />
            <Form.Control.Feedback type="invalid">{errors.numeroDireccion?.message}</Form.Control.Feedback>
          </Form.Group>
        </Col>
      </Row>
    </>
  )
}
CamposPersona.propTypes = {
  register: PropTypes.func.isRequired,
  errors: PropTypes.object.isRequired,
  prefijoLabel: PropTypes.string.isRequired,
}

export default function NuevaMatriculaWizard({ show, funcionarioUsuRut, onClose, onCompletado }) {
  const [paso, setPaso] = useState(1)
  const [guardando, setGuardando] = useState(false)
  const [comunas, setComunas] = useState([])
  const [cursos, setCursos] = useState([])
  const [matriculas, setMatriculas] = useState([])

  // RUT buscado/creado del apoderado
  const [apoderadoRutInput, setApoderadoRutInput] = useState('')
  const [buscandoApoderado, setBuscandoApoderado] = useState(false)
  const [apoderadoEncontrado, setApoderadoEncontrado] = useState(null) // { rut, nombre, apellido }
  const [apoderadoNoExiste, setApoderadoNoExiste] = useState(false)

  const [estudianteCreado, setEstudianteCreado] = useState(null) // { rut, nombre, apellido }

  const apoForm = useForm()
  const estForm = useForm()
  const matForm = useForm({ defaultValues: { cursoId: '', tipoAlumno: 'NUEVO', parentesco: '' } })

  useEffect(() => {
    if (!show) return
    setPaso(1)
    setApoderadoRutInput('')
    setApoderadoEncontrado(null)
    setApoderadoNoExiste(false)
    setEstudianteCreado(null)
    apoForm.reset()
    estForm.reset()
    matForm.reset({ cursoId: '', tipoAlumno: 'NUEVO', parentesco: '' })
    getComunas().then((r) => setComunas(Array.isArray(r.data) ? r.data : [])).catch(() => setComunas([]))
    getCursos().then((r) => setCursos(Array.isArray(r.data) ? r.data : [])).catch(() => setCursos([]))
    getMatriculas().then((r) => setMatriculas(Array.isArray(r.data) ? r.data : [])).catch(() => setMatriculas([]))
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [show])

  // Cupos por curso: el tope es cupos del curso si está definido, si no la capacidad
  // de la sala asignada. Disponibles = tope − matrículas ACTIVAS de ese curso.
  const cuposInfo = (curso) => {
    const tope = curso?.cupos ?? curso?.sala?.salaCapacidad ?? null
    const activas = matriculas.filter(
      (m) => m.cursoId === curso.idCur && m.matriculaEstado === 'ACTIVA'
    ).length
    const disponibles = tope === null ? null : tope - activas
    return { tope, activas, disponibles }
  }

  const buscarApoderado = async () => {
    if (!apoderadoRutInput.trim()) {
      toast.error('Ingresa el RUT del apoderado')
      return
    }
    // Exige guión + DV: usu_dv_rut es NOT NULL en Autenticacion, así que si se crea cuenta
    // nueva más abajo, el DV tiene que venir sí o sí desde este mismo input.
    if (!rutValidoConDv(apoderadoRutInput)) {
      toast.error('Ingresa el RUT completo con guión y dígito verificador (ej. 12345678-5)')
      return
    }
    setBuscandoApoderado(true)
    setApoderadoEncontrado(null)
    setApoderadoNoExiste(false)
    try {
      const rut = limpiarRut(apoderadoRutInput)
      const { data } = await getApoderado(rut)
      setApoderadoEncontrado({ rut: data.usuRut, nombre: data.usuPNombre, apellido: data.usuApePat })
    } catch (error) {
      // GlobalExceptionHandler de MS-Autenticacion mapea "no encontrado" a 400, no a 404
      // (cualquier RuntimeException cae ahí) — no hay forma de distinguir "no existe" de
      // otro 400 real, pero en este endpoint (GET por RUT) el único caso de error es ese.
      if (error.response?.status === 400) {
        setApoderadoNoExiste(true)
      } else {
        toast.error('No se pudo buscar el apoderado')
      }
    } finally {
      setBuscandoApoderado(false)
    }
  }

  const confirmarApoderadoExistente = () => setPaso(2)

  const crearApoderadoNuevo = async (data) => {
    setGuardando(true)
    try {
      const rut = limpiarRut(apoderadoRutInput)
      const dv = extraerDv(apoderadoRutInput)
      // POST /apoderados devuelve un mensaje de texto plano, no el objeto creado — usamos
      // los datos que ya tenemos localmente en vez de leerlos de la respuesta.
      await crearApoderado({
        apoRut: rut,
        apoDvRut: dv,
        apoPrimerNombre: data.primerNombre,
        apoSegundoNombre: data.segundoNombre || null,
        apoApellidoPat: data.apellidoPat,
        apoApellidoMat: data.apellidoMat,
        apoEmail: data.email,
        apoTel: data.tel || null,
        apoPassword: data.password,
        apoDireccion: data.direccion || null,
        apoNumeroDireccion: data.numeroDireccion ? Number(data.numeroDireccion) : null,
        apoTipoCasa: data.tipoCasa,
        idComuna: data.idComuna ? Number(data.idComuna) : null,
      })
      setApoderadoEncontrado({ rut, nombre: data.primerNombre, apellido: data.apellidoPat })
      toast.success('Apoderado creado')
      setPaso(2)
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || error.response?.data || 'No se pudo crear el apoderado')
    } finally {
      setGuardando(false)
    }
  }

  const crearEstudianteNuevo = async (data) => {
    setGuardando(true)
    try {
      const rut = limpiarRut(data.rutEstudiante)
      const dv = extraerDv(data.rutEstudiante)
      // POST /estudiantes devuelve un mensaje de texto plano, no el objeto creado — usamos
      // los datos que ya tenemos localmente en vez de leerlos de la respuesta.
      await crearEstudiante({
        estRut: rut,
        estDvRut: dv,
        estPrimerNombre: data.primerNombre,
        estSegundoNombre: data.segundoNombre || null,
        estApellidoPat: data.apellidoPat,
        estApellidoMat: data.apellidoMat,
        estEmail: data.email,
        estTel: data.tel || null,
        estPassword: data.password,
        estDireccion: data.direccion || null,
        estNumeroDireccion: data.numeroDireccion ? Number(data.numeroDireccion) : null,
        estTipoCasa: data.tipoCasa,
        idComuna: data.idComuna ? Number(data.idComuna) : null,
        cursoId: null,
      })
      setEstudianteCreado({ rut, nombre: data.primerNombre, apellido: data.apellidoPat })
      toast.success('Estudiante creado')
      setPaso(3)
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || error.response?.data || 'No se pudo crear el estudiante')
    } finally {
      setGuardando(false)
    }
  }

  const registrarMatricula = async (data) => {
    // Bloquea si el curso elegido ya no tiene cupos (defensa además del <option disabled>).
    const cursoElegido = cursos.find((c) => String(c.idCur) === String(data.cursoId))
    if (cursoElegido) {
      const { disponibles } = cuposInfo(cursoElegido)
      if (disponibles !== null && disponibles <= 0) {
        toast.error('El curso seleccionado no tiene cupos disponibles')
        return
      }
    }
    setGuardando(true)
    try {
      await crearMatricula({
        alumnoRut: estudianteCreado.rut,
        apoderadoRut: apoderadoEncontrado.rut,
        cursoId: data.cursoId ? Number(data.cursoId) : null,
        tipoAlumno: data.tipoAlumno,
        parentesco: data.parentesco,
        funcionarioUsuRut,
      })
      toast.success('Matrícula registrada')
      onCompletado()
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || error.response?.data || 'No se pudo registrar la matrícula')
    } finally {
      setGuardando(false)
    }
  }

  return (
    <Modal show={show} onHide={onClose} size="lg" centered backdrop="static">
      <Modal.Header closeButton>
        <Modal.Title>Nueva matrícula — estudiante sin cuenta previa</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <ProgressBar
          className="mb-4"
          now={(paso / 3) * 100}
          label={`Paso ${paso} de 3: ${paso === 1 ? 'Apoderado' : paso === 2 ? 'Estudiante' : 'Matrícula'}`}
        />

        {paso === 1 && (
          <>
            <Alert variant="secondary">
              Busca al apoderado por RUT. Si ya tiene cuenta, se reutiliza. Si no, se crea acá mismo.
            </Alert>
            <Row className="g-2 align-items-end mb-3">
              <Col md={8}>
                <Form.Label>RUT del apoderado</Form.Label>
                <Form.Control
                  placeholder="12345678-5"
                  value={apoderadoRutInput}
                  onChange={(e) => {
                    setApoderadoRutInput(e.target.value)
                    setApoderadoEncontrado(null)
                    setApoderadoNoExiste(false)
                  }}
                />
              </Col>
              <Col md={4}>
                <Button className="w-100" variant="outline-secondary" onClick={buscarApoderado} disabled={buscandoApoderado}>
                  {buscandoApoderado ? <Spinner size="sm" animation="border" /> : 'Buscar'}
                </Button>
              </Col>
            </Row>

            {apoderadoEncontrado && (
              <Alert variant="success">
                Encontrado: <strong>{apoderadoEncontrado.nombre} {apoderadoEncontrado.apellido}</strong> (RUT {apoderadoEncontrado.rut})
                <div className="mt-2">
                  <Button size="sm" className="text-white" style={{ backgroundColor: '#6B2323', borderColor: '#6B2323' }} onClick={confirmarApoderadoExistente}>
                    Continuar con este apoderado
                  </Button>
                </div>
              </Alert>
            )}

            {apoderadoNoExiste && (
              <>
                <Alert variant="warning">No existe cuenta con ese RUT — complete los datos para crearla.</Alert>
                <Form onSubmit={apoForm.handleSubmit(crearApoderadoNuevo)} noValidate>
                  <CamposPersona register={apoForm.register} errors={apoForm.formState.errors} prefijoLabel="el apoderado" />
                  <Form.Group controlId="idComunaApoderado" className="mt-3">
                    <Form.Label>Comuna *</Form.Label>
                    <Form.Select
                      isInvalid={!!apoForm.formState.errors.idComuna}
                      {...apoForm.register('idComuna', { required: 'Obligatorio — id_com es NOT NULL' })}
                    >
                      <option value="">Selecciona una comuna</option>
                      {comunas.map((c) => (
                        <option key={c.idCom} value={c.idCom}>{c.comNom}</option>
                      ))}
                    </Form.Select>
                    <Form.Control.Feedback type="invalid">{apoForm.formState.errors.idComuna?.message}</Form.Control.Feedback>
                  </Form.Group>
                  <div className="mt-3 text-end">
                    <Button type="submit" disabled={guardando} style={{ backgroundColor: '#6B2323', borderColor: '#6B2323' }}>
                      {guardando ? <Spinner size="sm" animation="border" /> : 'Crear apoderado y continuar'}
                    </Button>
                  </div>
                </Form>
              </>
            )}
          </>
        )}

        {paso === 2 && (
          <>
            <Alert variant="secondary">
              Apoderado: <strong>{apoderadoEncontrado?.nombre} {apoderadoEncontrado?.apellido}</strong>. Ahora crea la cuenta del estudiante.
            </Alert>
            <Form onSubmit={estForm.handleSubmit(crearEstudianteNuevo)} noValidate>
              <Form.Group controlId="rutEstudiante" className="mb-3">
                <Form.Label>RUT del estudiante *</Form.Label>
                <Form.Control
                  placeholder="12345678-5"
                  isInvalid={!!estForm.formState.errors.rutEstudiante}
                  {...estForm.register('rutEstudiante', rutConDvRules)}
                />
                <Form.Control.Feedback type="invalid">{estForm.formState.errors.rutEstudiante?.message}</Form.Control.Feedback>
              </Form.Group>
              <CamposPersona register={estForm.register} errors={estForm.formState.errors} prefijoLabel="el estudiante" />
              <Form.Group controlId="idComunaEstudiante" className="mt-3">
                <Form.Label>Comuna *</Form.Label>
                <Form.Select
                  isInvalid={!!estForm.formState.errors.idComuna}
                  {...estForm.register('idComuna', { required: 'Obligatorio — id_com es NOT NULL' })}
                >
                  <option value="">Selecciona una comuna</option>
                  {comunas.map((c) => (
                    <option key={c.idCom} value={c.idCom}>{c.comNom}</option>
                  ))}
                </Form.Select>
                <Form.Control.Feedback type="invalid">{estForm.formState.errors.idComuna?.message}</Form.Control.Feedback>
              </Form.Group>
              <div className="mt-3 d-flex justify-content-between">
                <Button variant="outline-secondary" onClick={() => setPaso(1)}>Atrás</Button>
                <Button type="submit" disabled={guardando} style={{ backgroundColor: '#6B2323', borderColor: '#6B2323' }}>
                  {guardando ? <Spinner size="sm" animation="border" /> : 'Crear estudiante y continuar'}
                </Button>
              </div>
            </Form>
          </>
        )}

        {paso === 3 && (
          <>
            <Alert variant="secondary">
              Estudiante: <strong>{estudianteCreado?.nombre} {estudianteCreado?.apellido}</strong> (RUT {estudianteCreado?.rut}) ·
              Apoderado: <strong>{apoderadoEncontrado?.nombre} {apoderadoEncontrado?.apellido}</strong>
            </Alert>
            <Form onSubmit={matForm.handleSubmit(registrarMatricula)} noValidate>
              <Row className="g-3">
                <Col md={6}>
                  <Form.Group controlId="cursoId">
                    <Form.Label>Curso *</Form.Label>
                    <Form.Select isInvalid={!!matForm.formState.errors.cursoId} {...matForm.register('cursoId', { required: 'Obligatorio' })}>
                      <option value="">Selecciona un curso</option>
                      {cursos.map((c) => {
                        const { disponibles } = cuposInfo(c)
                        const sinCupo = disponibles !== null && disponibles <= 0
                        const etiquetaCupo =
                          disponibles === null ? 'sin cupo definido' : `${disponibles} cupos disponibles`
                        return (
                          <option key={c.idCur} value={c.idCur} disabled={sinCupo}>
                            {formatNivel(c.nivel)} {c.curLetraSeccion} ({c.curAnioEscolar}) — {sinCupo ? 'SIN CUPOS' : etiquetaCupo}
                          </option>
                        )
                      })}
                    </Form.Select>
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group controlId="tipoAlumno">
                    <Form.Label>Tipo de alumno *</Form.Label>
                    <Form.Select {...matForm.register('tipoAlumno', { required: true })}>
                      <option value="NUEVO">Nuevo</option>
                      <option value="ANTIGUO">Antiguo</option>
                      <option value="REPITENTE">Repitente</option>
                    </Form.Select>
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group controlId="parentesco">
                    <Form.Label>Parentesco del apoderado con el alumno *</Form.Label>
                    <Form.Control
                      placeholder="Padre, Madre, Tío, Tutor Legal..."
                      isInvalid={!!matForm.formState.errors.parentesco}
                      {...matForm.register('parentesco', { required: 'Obligatorio' })}
                    />
                    <Form.Control.Feedback type="invalid">{matForm.formState.errors.parentesco?.message}</Form.Control.Feedback>
                  </Form.Group>
                </Col>
              </Row>
              <div className="mt-3 d-flex justify-content-between">
                <Button variant="outline-secondary" onClick={() => setPaso(2)}>Atrás</Button>
                <Button type="submit" disabled={guardando} style={{ backgroundColor: '#6B2323', borderColor: '#6B2323' }}>
                  {guardando ? <Spinner size="sm" animation="border" /> : 'Registrar matrícula'}
                </Button>
              </div>
            </Form>
          </>
        )}
      </Modal.Body>
    </Modal>
  )
}

NuevaMatriculaWizard.propTypes = {
  show: PropTypes.bool.isRequired,
  funcionarioUsuRut: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
  onClose: PropTypes.func.isRequired,
  onCompletado: PropTypes.func.isRequired,
}
