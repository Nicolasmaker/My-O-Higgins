// =============================================================
// REDACTAR MENSAJE — MensajeCompose.jsx
// =============================================================
// Modal de composición para el MS-Mensajeria. El remitente sale
// de la sesión activa; el backend setea fecha de envío y estado
// de lectura (no se envían desde el cliente).
//
// El destinatario se autocompleta con un <datalist> alimentado
// desde GET /usuarios (MS-Autenticacion). Si ese MS está caído
// el campo sigue funcionando como input manual.
// =============================================================
import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import PropTypes from 'prop-types'
import { Modal, Form, Button, Spinner } from 'react-bootstrap'
import { getUsuarios } from '../../services/authService'
import { rutRules } from '../../validators/fieldValidators'
import styles from '../../pages/Mensajeria/Mensajeria.module.css'

export default function MensajeCompose({ show, sending, onSend, onClose }) {
  const [usuarios, setUsuarios] = useState([])

  // Carga usuarios para el autocompletado al abrir el modal (una vez)
  useEffect(() => {
    if (!show || usuarios.length > 0) return
    getUsuarios()
      .then((res) => setUsuarios(Array.isArray(res.data) ? res.data : []))
      .catch(() => {}) // sin autocompletado si falla; el input manual sigue operativo
  }, [show, usuarios.length])

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
              list="usuarios-sistema"
              placeholder="Escribe RUT o busca por nombre"
              isInvalid={!!errors.destinatarioRut}
              {...register('destinatarioRut', rutRules)}
            />
            <datalist id="usuarios-sistema">
              {usuarios.map((u) => (
                <option key={u.usuRut} value={u.usuRut}>
                  {`${u.usuPNombre || ''} ${u.usuApePat || ''}`.trim()}
                </option>
              ))}
            </datalist>
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
