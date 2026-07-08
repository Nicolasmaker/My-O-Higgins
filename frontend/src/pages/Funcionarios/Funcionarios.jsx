// =============================================================
// PÁGINA CREAR FUNCIONARIOS — Funcionarios.jsx
// =============================================================
// Solo Directivo (ver ACCESO_RUTA y SecurityConfig de MS-Autenticacion:
// POST /funcionarios/** exige ROLE_DIRECTIVO). Crea cuentas nuevas de
// Docente, Inspector o Directivo — hasta ahora solo existían vía el
// seeder, sin ninguna UI.
// =============================================================
import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import { Row, Col, Form, Button, Spinner, Alert, Table, Badge } from 'react-bootstrap'
import { toast } from 'react-toastify'
import {
  crearDocente,
  crearInspector,
  crearDirectivo,
  getComunas,
  getFuncionarios,
} from '../../services/authService'
import {
  rutConDvRules,
  emailRules,
  passwordRules,
  limpiarRut,
  extraerDv,
} from '../../validators/fieldValidators'
import { formatRut } from '../../utils/formatRut'
import styles from '../Matricula/Matricula.module.css'

// Cada rol guarda su dato distintivo en un campo propio (dcteEspecialidad/insNivel/dirCargo)
// que no comparten entre sí — se resuelve el que exista en el objeto para mostrarlo en la tabla.
function campoDistintivo(f) {
  return f.dcteEspecialidad ?? f.insNivel ?? f.dirCargo ?? '—'
}

function rolLabel(f) {
  const rol = f.rol?.rolNombre ?? ''
  if (rol.includes('DOCENTE')) return 'Docente'
  if (rol.includes('INSPECTOR')) return 'Inspector'
  if (rol.includes('DIRECTIVO')) return 'Directivo'
  return rol || '—'
}

const TIPO_CASA = ['Casa', 'Departamento']

// Cada tipo de funcionario tiene su propio prefijo de campos en el backend (dcte/ins/dir) y
// un campo propio (especialidad/nivel/cargo) — el resto (nombre, dirección, etc) es idéntico.
const TIPOS = {
  DOCENTE: { label: 'Docente', prefijo: 'dcte', crear: crearDocente, campoPropio: 'dcteEspecialidad', labelPropio: 'Especialidad' },
  INSPECTOR: { label: 'Inspector', prefijo: 'ins', crear: crearInspector, campoPropio: 'insNivel', labelPropio: 'Nivel a cargo' },
  DIRECTIVO: { label: 'Directivo', prefijo: 'dir', crear: crearDirectivo, campoPropio: 'dirCargo', labelPropio: 'Cargo' },
}

export default function Funcionarios() {
  const [tipo, setTipo] = useState('DOCENTE')
  const [comunas, setComunas] = useState([])
  const [saving, setSaving] = useState(false)
  const [funcionarios, setFuncionarios] = useState([])
  const [loadingLista, setLoadingLista] = useState(true)

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm()

  useEffect(() => {
    getComunas().then((r) => setComunas(Array.isArray(r.data) ? r.data : [])).catch(() => setComunas([]))
  }, [])

  const cargarFuncionarios = () => {
    setLoadingLista(true)
    getFuncionarios()
      .then((r) => setFuncionarios(Array.isArray(r.data) ? r.data : []))
      .catch(() => setFuncionarios([]))
      .finally(() => setLoadingLista(false))
  }

  useEffect(() => {
    cargarFuncionarios()
  }, [])

  const config = TIPOS[tipo]

  const onSubmit = async (data) => {
    setSaving(true)
    try {
      const rut = limpiarRut(data.rut)
      const dv = extraerDv(data.rut)
      const payload = {
        [`${config.prefijo}Rut`]: rut,
        [`${config.prefijo}DvRut`]: dv,
        [`${config.prefijo}PrimerNombre`]: data.primerNombre,
        [`${config.prefijo}SegundoNombre`]: data.segundoNombre || null,
        [`${config.prefijo}ApellidoPat`]: data.apellidoPat,
        [`${config.prefijo}ApellidoMat`]: data.apellidoMat,
        [`${config.prefijo}Email`]: data.email,
        [`${config.prefijo}Password`]: data.password,
        [`${config.prefijo}Tel`]: data.tel,
        [`${config.prefijo}Titulo`]: data.titulo,
        [config.campoPropio]: data.campoPropio,
        [`${config.prefijo}Direccion`]: data.direccion,
        [`${config.prefijo}NumeroDireccion`]: Number(data.numeroDireccion),
        [`${config.prefijo}TipoCasa`]: data.tipoCasa,
        idComuna: Number(data.idComuna),
      }
      await config.crear(payload)
      toast.success(`${config.label} creado correctamente`)
      reset({ tipoCasa: 'Casa' })
      cargarFuncionarios()
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.mensaje || error.response?.data || `No se pudo crear el ${config.label.toLowerCase()}`)
    } finally {
      setSaving(false)
    }
  }

  return (
    <div className={styles.page}>
      <main className={styles.shell}>
        <header className={styles.pageHeader}>
          <div>
            <h1 className={styles.title}>Crear Funcionario</h1>
            <p className={styles.subtitle}>Registra cuentas nuevas de Docente, Inspector o Directivo</p>
          </div>
        </header>

        <div className={styles.tableWrap} style={{ padding: 24 }}>
          <Form.Group className="mb-4">
            <Form.Label>Tipo de funcionario</Form.Label>
            <div className="d-flex gap-3">
              {Object.entries(TIPOS).map(([key, t]) => (
                <Form.Check
                  key={key}
                  type="radio"
                  name="tipoFuncionario"
                  id={`tipo-${key}`}
                  label={t.label}
                  checked={tipo === key}
                  onChange={() => setTipo(key)}
                />
              ))}
            </div>
          </Form.Group>

          <Alert variant="secondary">
            Se crea con rol <strong>ROLE_{tipo}</strong>. La contraseña queda asignada de inmediato — comunícala al funcionario.
          </Alert>

          <Form onSubmit={handleSubmit(onSubmit)} noValidate>
            <Row className="g-3">
              <Col md={6}>
                <Form.Group controlId="rut">
                  <Form.Label>RUT *</Form.Label>
                  <Form.Control
                    placeholder="12345678-5"
                    isInvalid={!!errors.rut}
                    {...register('rut', rutConDvRules)}
                  />
                  <Form.Control.Feedback type="invalid">{errors.rut?.message}</Form.Control.Feedback>
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
                <Form.Group controlId="tel">
                  <Form.Label>Teléfono *</Form.Label>
                  <Form.Control
                    placeholder="+56912345678"
                    isInvalid={!!errors.tel}
                    {...register('tel', { required: 'Obligatorio' })}
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
                <Form.Group controlId="titulo">
                  <Form.Label>Título profesional *</Form.Label>
                  <Form.Control isInvalid={!!errors.titulo} {...register('titulo', { required: 'Obligatorio' })} />
                  <Form.Control.Feedback type="invalid">{errors.titulo?.message}</Form.Control.Feedback>
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group controlId="campoPropio">
                  <Form.Label>{config.labelPropio} *</Form.Label>
                  <Form.Control isInvalid={!!errors.campoPropio} {...register('campoPropio', { required: 'Obligatorio' })} />
                  <Form.Control.Feedback type="invalid">{errors.campoPropio?.message}</Form.Control.Feedback>
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group controlId="tipoCasa">
                  <Form.Label>Tipo de vivienda *</Form.Label>
                  <Form.Select defaultValue="Casa" {...register('tipoCasa', { required: true })}>
                    {TIPO_CASA.map((t) => (
                      <option key={t} value={t}>{t}</option>
                    ))}
                  </Form.Select>
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group controlId="idComuna">
                  <Form.Label>Comuna *</Form.Label>
                  <Form.Select isInvalid={!!errors.idComuna} {...register('idComuna', { required: 'Obligatorio' })}>
                    <option value="">Selecciona una comuna</option>
                    {comunas.map((c) => (
                      <option key={c.idCom} value={c.idCom}>{c.comNom}</option>
                    ))}
                  </Form.Select>
                  <Form.Control.Feedback type="invalid">{errors.idComuna?.message}</Form.Control.Feedback>
                </Form.Group>
              </Col>
              <Col md={8}>
                <Form.Group controlId="direccion">
                  <Form.Label>Dirección *</Form.Label>
                  <Form.Control isInvalid={!!errors.direccion} {...register('direccion', { required: 'Obligatorio' })} />
                  <Form.Control.Feedback type="invalid">{errors.direccion?.message}</Form.Control.Feedback>
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group controlId="numeroDireccion">
                  <Form.Label>Número *</Form.Label>
                  <Form.Control
                    type="number"
                    isInvalid={!!errors.numeroDireccion}
                    {...register('numeroDireccion', { required: 'Obligatorio' })}
                  />
                  <Form.Control.Feedback type="invalid">{errors.numeroDireccion?.message}</Form.Control.Feedback>
                </Form.Group>
              </Col>
            </Row>

            <div className="mt-4 text-end">
              <Button type="submit" className={styles.btnGranate} disabled={saving}>
                {saving ? (
                  <>
                    <Spinner as="span" size="sm" animation="border" className="me-2" />
                    Guardando...
                  </>
                ) : (
                  `Crear ${config.label.toLowerCase()}`
                )}
              </Button>
            </div>
          </Form>
        </div>

        <h2 className={`${styles.title} mt-5 mb-3`} style={{ fontSize: '1.25rem' }}>
          Funcionarios registrados
        </h2>
        {loadingLista ? (
          <div className={styles.emptyState}>
            <Spinner animation="border" size="sm" className="me-2" />
            Cargando funcionarios...
          </div>
        ) : funcionarios.length === 0 ? (
          <div className={styles.emptyState}>No hay funcionarios registrados.</div>
        ) : (
          <div className={styles.tableWrap}>
            <Table hover responsive className={styles.table}>
              <thead className={styles.tableHead}>
                <tr>
                  <th>RUT</th>
                  <th>Nombre</th>
                  <th>Rol</th>
                  <th>Título</th>
                  <th>Especialidad / Nivel / Cargo</th>
                  <th>Email</th>
                  <th>Teléfono</th>
                  <th>Estado</th>
                </tr>
              </thead>
              <tbody>
                {funcionarios.map((f) => (
                  <tr key={f.usuRut}>
                    <td>{formatRut(f.usuRut, f.usuDvRut)}</td>
                    <td>{f.usuPNombre} {f.usuApePat}</td>
                    <td><Badge bg="secondary">{rolLabel(f)}</Badge></td>
                    <td>{f.funTitulo ?? '—'}</td>
                    <td>{campoDistintivo(f)}</td>
                    <td>{f.usuEmail}</td>
                    <td>{f.usuTel}</td>
                    <td>
                      <Badge bg={f.usuEstadoActividad ? 'success' : 'secondary'}>
                        {f.usuEstadoActividad ? 'Activo' : 'Inactivo'}
                      </Badge>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </div>
        )}
      </main>
    </div>
  )
}
