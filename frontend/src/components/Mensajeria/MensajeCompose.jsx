// =============================================================
// REDACTAR MENSAJE — MensajeCompose.jsx
// =============================================================
// Modal de composición para el MS-Mensajeria. El remitente sale
// de la sesión activa; el backend setea fecha de envío y estado
// de lectura (no se envían desde el cliente).
// =============================================================
import { useForm } from 'react-hook-form'
import PropTypes from 'prop-types'
import { Modal, Form, Button, Spinner } from 'react-bootstrap'
import styles from '../../pages/Mensajeria/Mensajeria.module.css'

export default function MensajeCompose({ show, sending, onSend, onClose }) {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm({ defaultValues: { destinatarioRut: '', asunto: '', contenido: '' } })

  const submit = async (data) => {
    const ok = await onSend(data)
    if (ok) reset()
  }

  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton className={styles.composeHeader}>
        <Modal.Title className={styles.composeTitle}>Redactar mensaje</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit(submit)} noValidate>
        <Modal.Body>
          <Form.Group className="mb-3" controlId="destinatarioRut">
            <Form.Label>RUT destinatario *</Form.Label>
            <Form.Control
              type="number"
              placeholder="Sin puntos ni dígito verificador"
              isInvalid={!!errors.destinatarioRut}
              {...register('destinatarioRut', { required: 'El destinatario es obligatorio' })}
            />
            <Form.Control.Feedback type="invalid">{errors.destinatarioRut?.message}</Form.Control.Feedback>
          </Form.Group>

          <Form.Group className="mb-3" controlId="asunto">
            <Form.Label>Asunto *</Form.Label>
            <Form.Control
              maxLength={200}
              placeholder="Asunto del mensaje"
              isInvalid={!!errors.asunto}
              {...register('asunto', { required: 'El asunto es obligatorio' })}
            />
            <Form.Control.Feedback type="invalid">{errors.asunto?.message}</Form.Control.Feedback>
          </Form.Group>

          <Form.Group controlId="contenido">
            <Form.Label>Mensaje *</Form.Label>
            <Form.Control
              as="textarea"
              rows={5}
              maxLength={2000}
              placeholder="Escribe tu mensaje..."
              isInvalid={!!errors.contenido}
              {...register('contenido', { required: 'El contenido es obligatorio' })}
            />
            <Form.Control.Feedback type="invalid">{errors.contenido?.message}</Form.Control.Feedback>
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="outline-secondary" onClick={onClose} disabled={sending}>
            Cancelar
          </Button>
          <Button type="submit" className={styles.btnGranate} disabled={sending}>
            {sending ? (
              <>
                <Spinner as="span" size="sm" animation="border" className="me-2" />
                Enviando...
              </>
            ) : (
              'Enviar mensaje'
            )}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  )
}

MensajeCompose.propTypes = {
  show: PropTypes.bool.isRequired,
  sending: PropTypes.bool,
  onSend: PropTypes.func.isRequired,
  onClose: PropTypes.func.isRequired,
}
