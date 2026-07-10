// =============================================================
// FORMULARIO HOJA DE VIDA — HojaVidaForm.jsx
// =============================================================
// Modal para crear/editar la hoja de vida base de un estudiante.
// Solo dos campos: RUT del estudiante y ID de su matrícula
// (referencias planas a MS-Autenticacion y MS-GestionMatricula).
// =============================================================
import { useEffect } from 'react'
import { useForm } from 'react-hook-form'
import PropTypes from 'prop-types'
import { Modal, Form, Button, Spinner } from 'react-bootstrap'
import { rutRules } from '../../validators/fieldValidators'
import styles from '../../styles/HojaDeVida.module.css'

export default function HojaVidaForm({ show, hoja, saving, onSave, onClose }) {
  const editing = !!hoja

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm()

  useEffect(() => {
    if (!show) return
    reset({
      estudianteUsuRut: editing ? String(hoja.estudianteUsuRut ?? '') : '',
      matriculaId: editing ? String(hoja.matriculaId ?? '') : '',
      estado: editing ? hoja.estado ?? '' : '',
    })
  }, [show, editing, hoja, reset])

  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton className={styles.modalHeader}>
        <Modal.Title className={styles.modalTitle}>
          {editing ? `Editar hoja de vida #${hoja.idHojaVida}` : 'Nueva hoja de vida'}
        </Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit(onSave)} noValidate>
        <Modal.Body>
          <Form.Group className="mb-3" controlId="estudianteUsuRut">
            <Form.Label>RUT estudiante *</Form.Label>
            <Form.Control
              placeholder="12345678 o 12345678-5"
              isInvalid={!!errors.estudianteUsuRut}
              {...register('estudianteUsuRut', rutRules)}
            />
            <Form.Control.Feedback type="invalid">{errors.estudianteUsuRut?.message}</Form.Control.Feedback>
          </Form.Group>

          <Form.Group controlId="matriculaId">
            <Form.Label>ID matrícula *</Form.Label>
            <Form.Control
              type="number"
              placeholder="Matrícula asociada"
              isInvalid={!!errors.matriculaId}
              {...register('matriculaId', { required: 'La matrícula es obligatoria' })}
            />
            <Form.Control.Feedback type="invalid">{errors.matriculaId?.message}</Form.Control.Feedback>
          </Form.Group>

          <Form.Group className="mt-3" controlId="estado">
            <Form.Label>Estado</Form.Label>
            <Form.Select {...register('estado')}>
              <option value="">Sin definir</option>
              <option value="Incorporado">Incorporado</option>
              <option value="Retirado">Retirado</option>
              <option value="Suspendido">Suspendido</option>
            </Form.Select>
          </Form.Group>
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
            ) : (
              'Guardar'
            )}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  )
}

HojaVidaForm.propTypes = {
  show: PropTypes.bool.isRequired,
  hoja: PropTypes.object,
  saving: PropTypes.bool,
  onSave: PropTypes.func.isRequired,
  onClose: PropTypes.func.isRequired,
}
