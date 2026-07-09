// =============================================================
// FORMULARIO "AGENDAR REUNIÓN" — AgendarReunionForm.jsx
// =============================================================
// Formulario liviano para agendar (no para dejar constancia de lo
// tratado — eso es la bitácora, se llena después con "Rellenar
// bitácora"/"Completar acta"). Solo pide lo necesario para citar:
//
//   - Individual → correo del apoderado (resuelve RUT+hijos),
//     alumno, motivo.
//   - General    → curso, tipo de reunión.
//
// El emisor (docente/inspector/directivo) es siempre el usuario
// de la sesión activa — nunca se pide.
// =============================================================
import { useState } from 'react'
import { useForm } from 'react-hook-form'
import PropTypes from 'prop-types'
import { Card, Form, Row, Col, Button, Spinner } from 'react-bootstrap'
import { toast } from 'react-toastify'
import { buscarApoderadoPorEmail } from '../../services/authService'
import { getMatriculas } from '../../services/matriculaService'
import { formatNivel } from '../Academico/entidadesConfig'
import styles from '../../styles/Reuniones.module.css'

const TITULOS = {
  individual: 'Agendar entrevista individual',
  general: 'Agendar reunión de curso',
}

export default function AgendarReunionForm({ tipo, cursos = [], saving, onSubmit, onCancel, prefillMotivo = '' }) {
  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm({
    defaultValues: {
      bitReuFec: '',
      email: '',
      alumnoRut: '',
      bitReuIndMotivReu: prefillMotivo,
      cursoId: '',
      bitReuGenTipReu: 'Ordinaria',
    },
  })

  const [buscando, setBuscando] = useState(false)
  const [apoderadoResuelto, setApoderadoResuelto] = useState(null)
  const [hijos, setHijos] = useState([])

  const handleBuscarApoderado = async (email) => {
    if (!email) return
    setBuscando(true)
    setApoderadoResuelto(null)
    setHijos([])
    try {
      const res = await buscarApoderadoPorEmail(email)
      const apoderado = res.data
      setApoderadoResuelto(apoderado)
      const matriculasRes = await getMatriculas()
      const propias = (matriculasRes.data || []).filter(
        (m) => String(m.apoderadoRut) === String(apoderado.usuRut)
      )
      setHijos(propias)
      if (propias.length === 1) {
        setValue('alumnoRut', String(propias[0].alumnoRut))
      }
    } catch {
      toast.error('No se encontró un apoderado con ese correo')
    } finally {
      setBuscando(false)
    }
  }

  const onFormSubmit = (data) => {
    if (tipo === 'individual' && !apoderadoResuelto) {
      toast.error('Busca y confirma el apoderado por su correo antes de continuar')
      return
    }
    onSubmit({ ...data, apoderadoUsuRut: apoderadoResuelto?.usuRut })
  }

  return (
    <Card className={styles.formCard}>
      <Card.Header className={styles.formHeader}>{TITULOS[tipo]}</Card.Header>
      <Card.Body>
        <Form onSubmit={handleSubmit(onFormSubmit)} noValidate>
          <Row className="g-3">
            <Col md={4}>
              <Form.Group controlId="bitReuFec">
                <Form.Label>Fecha *</Form.Label>
                <Form.Control
                  type="date"
                  isInvalid={!!errors.bitReuFec}
                  {...register('bitReuFec', { required: 'La fecha es obligatoria' })}
                />
                <Form.Control.Feedback type="invalid">{errors.bitReuFec?.message}</Form.Control.Feedback>
              </Form.Group>
            </Col>

            {tipo === 'individual' && (
              <>
                <Col md={5}>
                  <Form.Group controlId="email">
                    <Form.Label>Correo del apoderado *</Form.Label>
                    <Form.Control
                      type="email"
                      placeholder="apoderado@correo.com"
                      isInvalid={!!errors.email}
                      {...register('email', {
                        required: 'El correo es obligatorio',
                        onBlur: (e) => handleBuscarApoderado(e.target.value),
                      })}
                    />
                    <Form.Control.Feedback type="invalid">{errors.email?.message}</Form.Control.Feedback>
                  </Form.Group>
                </Col>
                <Col md={3} className="d-flex align-items-end pb-2">
                  {buscando ? (
                    <Spinner animation="border" size="sm" />
                  ) : apoderadoResuelto ? (
                    <span className="text-success small">
                      {apoderadoResuelto.usuPNombre} {apoderadoResuelto.usuApePat}
                    </span>
                  ) : (
                    <span className="text-muted small">Sin resolver</span>
                  )}
                </Col>
              </>
            )}

            {tipo === 'individual' && apoderadoResuelto && (
              <Col md={12}>
                <Form.Group controlId="alumnoRut">
                  <Form.Label>Alumno *</Form.Label>
                  <Form.Select
                    isInvalid={!!errors.alumnoRut}
                    {...register('alumnoRut', { required: 'Selecciona el alumno' })}
                  >
                    <option value="">Selecciona un alumno</option>
                    {hijos.map((h) => (
                      <option key={h.alumnoRut} value={h.alumnoRut}>
                        RUT {h.alumnoRut}
                      </option>
                    ))}
                  </Form.Select>
                  <Form.Control.Feedback type="invalid">{errors.alumnoRut?.message}</Form.Control.Feedback>
                  {!buscando && hijos.length === 0 && (
                    <Form.Text className="text-danger">Este apoderado no tiene matrículas registradas.</Form.Text>
                  )}
                </Form.Group>
              </Col>
            )}

            {tipo === 'individual' && (
              <Col md={12}>
                <Form.Group controlId="bitReuIndMotivReu">
                  <Form.Label>Motivo *</Form.Label>
                  <Form.Control
                    maxLength={100}
                    placeholder="Ej: Bajo rendimiento académico"
                    isInvalid={!!errors.bitReuIndMotivReu}
                    {...register('bitReuIndMotivReu', { required: 'El motivo es obligatorio' })}
                  />
                  <Form.Control.Feedback type="invalid">{errors.bitReuIndMotivReu?.message}</Form.Control.Feedback>
                </Form.Group>
              </Col>
            )}

            {tipo === 'general' && (
              <>
                <Col md={4}>
                  <Form.Group controlId="cursoId">
                    <Form.Label>Curso *</Form.Label>
                    <Form.Select
                      isInvalid={!!errors.cursoId}
                      {...register('cursoId', { required: 'Selecciona el curso' })}
                    >
                      <option value="">Selecciona un curso</option>
                      {cursos.map((c) => (
                        <option key={c.idCur} value={c.idCur}>
                          {formatNivel(c.nivel)} {c.curLetraSeccion} ({c.curAnioEscolar})
                        </option>
                      ))}
                    </Form.Select>
                    <Form.Control.Feedback type="invalid">{errors.cursoId?.message}</Form.Control.Feedback>
                  </Form.Group>
                </Col>
                <Col md={4}>
                  <Form.Group controlId="bitReuGenTipReu">
                    <Form.Label>Tipo de reunión *</Form.Label>
                    <Form.Select {...register('bitReuGenTipReu', { required: true })}>
                      <option value="Ordinaria">Ordinaria</option>
                      <option value="Extraordinaria">Extraordinaria</option>
                    </Form.Select>
                  </Form.Group>
                </Col>
              </>
            )}
          </Row>

          <div className="d-flex gap-2 justify-content-end mt-4">
            <Button variant="outline-secondary" type="button" onClick={onCancel} disabled={saving}>
              Cancelar
            </Button>
            <Button type="submit" className={styles.btnGranate} disabled={saving}>
              {saving ? (
                <>
                  <Spinner as="span" size="sm" animation="border" className="me-2" />
                  Agendando...
                </>
              ) : (
                'Agendar reunión'
              )}
            </Button>
          </div>
        </Form>
      </Card.Body>
    </Card>
  )
}

AgendarReunionForm.propTypes = {
  tipo: PropTypes.oneOf(['individual', 'general']).isRequired,
  cursos: PropTypes.array,
  saving: PropTypes.bool,
  onSubmit: PropTypes.func.isRequired,
  onCancel: PropTypes.func.isRequired,
  prefillMotivo: PropTypes.string,
}
