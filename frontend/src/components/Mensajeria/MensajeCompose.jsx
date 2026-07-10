// =============================================================
// REDACTAR MENSAJE — MensajeCompose.jsx
// =============================================================
// Modal de composición para el MS-Mensajeria. El remitente sale
// de la sesión activa; el backend setea fecha de envío y estado
// de lectura (no se envían desde el cliente).
//
// El destinatario se busca por correo: se carga una vez la lista
// completa de usuarios (GET /usuarios, MS-Autenticacion) y, al
// dejar de tipear, se resuelve el correo a un usuario (nombre +
// RUT) mostrado como chip. El backend sigue esperando RUT, así
// que el RUT resuelto es lo que realmente se envía.
// =============================================================
import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import PropTypes from 'prop-types'
import { Modal, Form, Button, Spinner } from 'react-bootstrap'
import { getUsuarios } from '../../services/authService'
import { emailRules } from '../../validators/fieldValidators'
import styles from '../../styles/Mensajeria.module.css'

export default function MensajeCompose({ show, sending, onSend, onClose }) {
  const [usuarios, setUsuarios] = useState([])
  const [destinatario, setDestinatario] = useState(null) // usuario resuelto por correo
  const [busquedaEstado, setBusquedaEstado] = useState('idle') // idle | buscando | no-encontrado

  // Carga usuarios para resolver el correo al abrir el modal (una vez)
  useEffect(() => {
    if (!show || usuarios.length > 0) return
    getUsuarios()
      .then((res) => setUsuarios(Array.isArray(res.data) ? res.data : []))
      .catch(() => {}) // sin resolución de correo si falla; se puede reintentar
  }, [show, usuarios.length])

  const {
    register,
    handleSubmit,
    reset,
    watch,
    formState: { errors },
  } = useForm({ defaultValues: { destinatarioEmail: '', asunto: '', contenido: '' } })

  const emailTyped = watch('destinatarioEmail')

  // Al dejar de tipear, busca coincidencia exacta de correo en la lista ya cargada
  useEffect(() => {
    if (destinatario) return
    const valor = (emailTyped || '').trim().toLowerCase()
    if (!valor) {
      setBusquedaEstado('idle')
      return
    }
    setBusquedaEstado('buscando')
    const timeout = setTimeout(() => {
      const encontrado = usuarios.find((u) => String(u.usuEmail || '').toLowerCase() === valor)
      if (encontrado) {
        setDestinatario(encontrado)
        setBusquedaEstado('idle')
      } else {
        setBusquedaEstado('no-encontrado')
      }
    }, 500)
    return () => clearTimeout(timeout)
  }, [emailTyped, usuarios, destinatario])

  const quitarDestinatario = () => {
    setDestinatario(null)
    setBusquedaEstado('idle')
  }

  const submit = async (data) => {
    if (!destinatario) return
    const ok = await onSend({ ...data, destinatarioRut: destinatario.usuRut })
    if (ok) {
      reset()
      quitarDestinatario()
    }
  }

  const handleClose = () => {
    reset()
    quitarDestinatario()
    onClose()
  }

  return (
    <Modal show={show} onHide={handleClose} centered>
      <Modal.Header closeButton className={styles.composeHeader}>
        <Modal.Title className={styles.composeTitle}>Redactar mensaje</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit(submit)} noValidate>
        <Modal.Body>
          <Form.Group className="mb-3" controlId="destinatarioEmail">
            <Form.Label className={styles.composeLabel}>Correo destinatario</Form.Label>

            {destinatario ? (
              <div className={styles.destinatarioChip}>
                <span>{`${destinatario.usuPNombre || ''} ${destinatario.usuApePat || ''}`.trim() || destinatario.usuEmail}</span>
                <button type="button" onClick={quitarDestinatario} aria-label="Cambiar destinatario">
                  ×
                </button>
              </div>
            ) : (
              <>
                <Form.Control
                  type="email"
                  placeholder="correo@colegio.cl"
                  isInvalid={!!errors.destinatarioEmail || busquedaEstado === 'no-encontrado'}
                  {...register('destinatarioEmail', emailRules)}
                />
                {busquedaEstado === 'buscando' && (
                  <Form.Text className="text-muted">Buscando usuario...</Form.Text>
                )}
                {busquedaEstado === 'no-encontrado' && (
                  <Form.Control.Feedback type="invalid">
                    No se encontró ningún usuario con ese correo.
                  </Form.Control.Feedback>
                )}
                {errors.destinatarioEmail && (
                  <Form.Control.Feedback type="invalid">{errors.destinatarioEmail.message}</Form.Control.Feedback>
                )}
              </>
            )}
          </Form.Group>

          <Form.Group className="mb-3" controlId="asunto">
            <Form.Label className={styles.composeLabel}>Asunto</Form.Label>
            <Form.Control
              maxLength={200}
              placeholder="Asunto del mensaje"
              isInvalid={!!errors.asunto}
              {...register('asunto', { required: 'El asunto es obligatorio' })}
            />
            <Form.Control.Feedback type="invalid">{errors.asunto?.message}</Form.Control.Feedback>
          </Form.Group>

          <Form.Group controlId="contenido">
            <Form.Label className={styles.composeLabel}>Mensaje</Form.Label>
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
          <Button variant="outline-secondary" onClick={handleClose} disabled={sending}>
            Cancelar
          </Button>
          <Button type="submit" className={styles.btnGranate} disabled={sending || !destinatario}>
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
