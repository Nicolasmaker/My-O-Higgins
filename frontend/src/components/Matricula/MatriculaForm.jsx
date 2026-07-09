// =============================================================
// FORMULARIO DE MATRÍCULA — MatriculaForm.jsx
// =============================================================
// Modal para registrar o actualizar matrículas (RF-17).
// En creación el backend setea fecha, año académico y estado
// ACTIVA automáticamente (@PrePersist). El funcionario que registra
// es el usuario logueado (no se pide; va oculto). Al teclear el RUT
// del alumno/apoderado se muestra a qué correo pertenece.
// =============================================================
import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import PropTypes from 'prop-types'
import { Modal, Form, Row, Col, Button, Spinner } from 'react-bootstrap'
import { rutRules, rutValido, limpiarRut } from '../../validators/fieldValidators'
import { getEstudiante, getApoderado } from '../../services/authService'
import { formatNivel } from '../Academico/entidadesConfig'
import styles from '../../pages/Matricula/Matricula.module.css'

const emptyValues = {
  alumnoRut: '',
  apoderadoRut: '',
  cursoId: '',
  tipoAlumno: 'NUEVO',
  parentesco: '',
  funcionarioUsuRut: '',
  matriculaEstado: 'ACTIVA',
}

// Busca el correo de una persona por RUT en Autenticacion (estudiante o apoderado).
// Devuelve el email o null. Hook reutilizable para alumno y apoderado.
function useEmailPorRut(rutValue, fetcher) {
  const [info, setInfo] = useState(null) // { email } | { noExiste:true } | null
  useEffect(() => {
    const valor = (rutValue || '').trim()
    if (!valor || !rutValido(valor)) {
      setInfo(null)
      return
    }
    let cancelado = false
    const t = setTimeout(() => {
      fetcher(limpiarRut(valor))
        .then((res) => {
          if (cancelado) return
          const d = res.data || {}
          setInfo({ email: d.usuEmail ?? d.email ?? d.correo ?? null })
        })
        .catch(() => !cancelado && setInfo({ noExiste: true }))
    }, 500)
    return () => {
      cancelado = true
      clearTimeout(t)
    }
  }, [rutValue, fetcher])
  return info
}

function MensajeRut({ info, tipo }) {
  if (!info) return null
  if (info.noExiste) {
    return <small className="text-warning">No se encontró un {tipo} con ese RUT.</small>
  }
  return (
    <small className="text-muted d-block" style={{ fontSize: '0.75rem', lineHeight: 1.3 }}>
      {tipo === 'alumno' ? 'Alumno' : 'Apoderado'}: {info.email || 'sin correo registrado'}
    </small>
  )
}

MensajeRut.propTypes = { info: PropTypes.object, tipo: PropTypes.string.isRequired }

export default function MatriculaForm({ show, matricula, defaultRut, cursos = [], matriculas = [], saving, onSave, onClose }) {
  const editing = !!matricula

  const {
    register,
    handleSubmit,
    reset,
    watch,
    formState: { errors },
  } = useForm({ defaultValues: emptyValues })

  useEffect(() => {
    if (!show) return
    reset(
      editing
        ? {
            alumnoRut: String(matricula.alumnoRut ?? ''),
            apoderadoRut: String(matricula.apoderadoRut ?? ''),
            cursoId: String(matricula.cursoId ?? ''),
            tipoAlumno: matricula.tipoAlumno || 'NUEVO',
            parentesco: matricula.parentesco || '',
            funcionarioUsuRut: String(matricula.funcionarioUsuRut ?? defaultRut ?? ''),
            matriculaEstado: matricula.matriculaEstado || 'ACTIVA',
          }
        : { ...emptyValues, funcionarioUsuRut: String(defaultRut || '') }
    )
  }, [show, editing, matricula, defaultRut, reset])

  const alumnoInfo = useEmailPorRut(watch('alumnoRut'), getEstudiante)
  const apoderadoInfo = useEmailPorRut(watch('apoderadoRut'), getApoderado)

  // Cupos disponibles por curso (mismo criterio que el wizard).
  const cupoLabel = (c) => {
    const tope = c.cupos ?? c.sala?.salaCapacidad ?? null
    if (tope === null) return 'sin cupo definido'
    const activas = matriculas.filter((m) => m.cursoId === c.idCur && m.matriculaEstado === 'ACTIVA').length
    const disp = tope - activas
    return disp <= 0 ? 'SIN CUPOS' : `${disp} cupos`
  }

  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton className={styles.modalHeader}>
        <Modal.Title className={styles.modalTitle}>
          {editing ? `Editar matrícula #${matricula.idMatricula}` : 'Registrar matrícula'}
        </Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit(onSave)} noValidate>
        <Modal.Body>
          {/* El funcionario que registra es el usuario logueado: va oculto, no se pide. */}
          <input type="hidden" {...register('funcionarioUsuRut')} />
          <Row className="g-3">
            <Col md={6}>
              <Form.Group controlId="alumnoRut">
                <Form.Label>RUT alumno *</Form.Label>
                <Form.Control
                  placeholder="12345678 o 12345678-5"
                  isInvalid={!!errors.alumnoRut}
                  {...register('alumnoRut', rutRules)}
                />
                <Form.Control.Feedback type="invalid">{errors.alumnoRut?.message}</Form.Control.Feedback>
                <MensajeRut info={alumnoInfo} tipo="alumno" />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group controlId="apoderadoRut">
                <Form.Label>RUT apoderado *</Form.Label>
                <Form.Control
                  placeholder="12345678 o 12345678-5"
                  isInvalid={!!errors.apoderadoRut}
                  {...register('apoderadoRut', rutRules)}
                />
                <Form.Control.Feedback type="invalid">{errors.apoderadoRut?.message}</Form.Control.Feedback>
                <MensajeRut info={apoderadoInfo} tipo="apoderado" />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group controlId="cursoId">
                <Form.Label>Curso</Form.Label>
                <Form.Select {...register('cursoId')}>
                  <option value="">Selecciona un curso</option>
                  {cursos.map((c) => (
                    <option key={c.idCur} value={c.idCur}>
                      {formatNivel(c.nivel)} {c.curLetraSeccion} ({c.curAnioEscolar}) — {cupoLabel(c)}
                    </option>
                  ))}
                </Form.Select>
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group controlId="tipoAlumno">
                <Form.Label>Tipo de alumno *</Form.Label>
                <Form.Select {...register('tipoAlumno', { required: true })}>
                  <option value="NUEVO">Nuevo</option>
                  <option value="ANTIGUO">Antiguo</option>
                  <option value="REPITENTE">Repitente</option>
                </Form.Select>
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group controlId="parentesco">
                <Form.Label>Parentesco con el alumno *</Form.Label>
                <Form.Control
                  placeholder="Padre, Madre, Tío, Tutor Legal..."
                  isInvalid={!!errors.parentesco}
                  {...register('parentesco', { required: 'Obligatorio' })}
                />
                <Form.Control.Feedback type="invalid">{errors.parentesco?.message}</Form.Control.Feedback>
              </Form.Group>
            </Col>
            {editing && (
              <Col md={6}>
                <Form.Group controlId="matriculaEstado">
                  <Form.Label>Estado</Form.Label>
                  <Form.Select {...register('matriculaEstado')}>
                    <option value="ACTIVA">Activa</option>
                    <option value="SUSPENDIDA">Suspendida</option>
                    <option value="RETIRADA">Retirada</option>
                  </Form.Select>
                </Form.Group>
              </Col>
            )}
          </Row>
          {!editing && (
            <p className={styles.formHint}>
              La fecha, el año académico y el estado inicial (ACTIVA) se asignan automáticamente. La matrícula
              queda registrada a tu nombre como funcionario.
            </p>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="outline-secondary" onClick={onClose} disabled={saving}>
            Cancelar
          </Button>
          <Button type="submit" className={styles.btnGranate} disabled={saving}>
            {saving ? (
              <>
                <Spinner as="span" size="sm" animation="border" className="me-2" />
                Guardando...
              </>
            ) : editing ? (
              'Guardar cambios'
            ) : (
              'Registrar matrícula'
            )}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  )
}

MatriculaForm.propTypes = {
  show: PropTypes.bool.isRequired,
  matricula: PropTypes.object,
  defaultRut: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  cursos: PropTypes.array,
  matriculas: PropTypes.array,
  saving: PropTypes.bool,
  onSave: PropTypes.func.isRequired,
  onClose: PropTypes.func.isRequired,
}
