// =============================================================
// FORMULARIO DE MATRÍCULA — MatriculaForm.jsx
// =============================================================
// Modal para registrar o actualizar matrículas (RF-17).
// En creación el backend setea fecha, año académico y estado
// ACTIVA automáticamente (@PrePersist); el estado solo se puede
// cambiar al editar. El backend valida que alumno, apoderado y
// funcionario existan en MS-Autenticacion.
// =============================================================
import { useEffect } from 'react'
import { useForm } from 'react-hook-form'
import PropTypes from 'prop-types'
import { Modal, Form, Row, Col, Button, Spinner } from 'react-bootstrap'
import { rutRules } from '../../validators/fieldValidators'
import styles from '../../pages/Matricula/Matricula.module.css'

const emptyValues = {
  alumnoRut: '',
  apoderadoRut: '',
  cursoId: '',
  tipoAlumno: 'NUEVO',
  funcionarioUsuRut: '',
  matriculaEstado: 'ACTIVA',
}

export default function MatriculaForm({ show, matricula, defaultRut, saving, onSave, onClose }) {
  const editing = !!matricula

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm({ defaultValues: emptyValues })

  // Al abrir el modal, carga los datos de la matrícula (edición) o valores limpios (creación)
  useEffect(() => {
    if (!show) return
    reset(
      editing
        ? {
            alumnoRut: String(matricula.alumnoRut ?? ''),
            apoderadoRut: String(matricula.apoderadoRut ?? ''),
            cursoId: String(matricula.cursoId ?? ''),
            tipoAlumno: matricula.tipoAlumno || 'NUEVO',
            funcionarioUsuRut: String(matricula.funcionarioUsuRut ?? ''),
            matriculaEstado: matricula.matriculaEstado || 'ACTIVA',
          }
        : { ...emptyValues, funcionarioUsuRut: String(defaultRut || '') }
    )
  }, [show, editing, matricula, defaultRut, reset])

  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton className={styles.modalHeader}>
        <Modal.Title className={styles.modalTitle}>
          {editing ? `Editar matrícula #${matricula.idMatricula}` : 'Registrar matrícula'}
        </Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit(onSave)} noValidate>
        <Modal.Body>
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
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group controlId="cursoId">
                <Form.Label>ID curso</Form.Label>
                <Form.Control type="number" placeholder="Curso asignado" {...register('cursoId')} />
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
              <Form.Group controlId="funcionarioUsuRut">
                <Form.Label>RUT funcionario *</Form.Label>
                <Form.Control
                  placeholder="Quien registra"
                  isInvalid={!!errors.funcionarioUsuRut}
                  {...register('funcionarioUsuRut', rutRules)}
                />
                <Form.Control.Feedback type="invalid">{errors.funcionarioUsuRut?.message}</Form.Control.Feedback>
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
              La fecha, el año académico y el estado inicial (ACTIVA) se asignan automáticamente.
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
  saving: PropTypes.bool,
  onSave: PropTypes.func.isRequired,
  onClose: PropTypes.func.isRequired,
}
