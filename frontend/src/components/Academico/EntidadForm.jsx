// =============================================================
// FORMULARIO GENÉRICO ACADÉMICO — EntidadForm.jsx
// =============================================================
// Modal único para crear/editar cualquiera de las 7 entidades
// del MS-GestionAcademica, alimentado por entidadesConfig.jsx.
// Campos con `soloCrear` desaparecen al editar (el backend no
// los acepta en el PUT).
// =============================================================
import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import PropTypes from 'prop-types'
import { Modal, Form, Row, Col, Button, Spinner } from 'react-bootstrap'
import { rutRules } from '../../validators/fieldValidators'
import styles from '../../styles/Academico.module.css'

export default function EntidadForm({ show, config, item, saving, onSave, onClose }) {
  const editing = !!item

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm()

  const [entityOptions, setEntityOptions] = useState({})
  const [loadingOptions, setLoadingOptions] = useState({})

  useEffect(() => {
    if (!show || !config) return
    if (editing) {
      const values = {}
      config.campos.forEach((c) => {
        values[c.name] = c.getValue(item)
      })
      reset(values)
    } else {
      reset(config.defaults)
    }
  }, [show, editing, item, config, reset])

  useEffect(() => {
    if (!show || !config) return
    const campos = config.campos.filter((c) => c.type === 'entity-select')
    campos.forEach((campo) => {
      setLoadingOptions((prev) => ({ ...prev, [campo.name]: true }))
      campo
        .loadOptions()
        .then((options) => {
          setEntityOptions((prev) => ({ ...prev, [campo.name]: options }))
        })
        .finally(() => {
          setLoadingOptions((prev) => ({ ...prev, [campo.name]: false }))
        })
    })
  }, [show, config])

  if (!config) return null

  const camposVisibles = config.campos.filter(
    (c) => !(editing && c.soloCrear) && !(!editing && c.soloEditar)
  )

  const renderControl = (campo) => {
    const rules = campo.type === 'rut' ? rutRules : campo.rules
    const common = { isInvalid: !!errors[campo.name], ...register(campo.name, rules) }
    switch (campo.type) {
      case 'rut':
        return <Form.Control placeholder="12345678 o 12345678-5" {...common} />
      case 'number':
        return <Form.Control type="number" {...common} />
      case 'decimal':
        return <Form.Control type="number" step="0.1" {...common} />
      case 'date':
        return <Form.Control type="date" {...common} />
      case 'textarea':
        return <Form.Control as="textarea" rows={3} maxLength={campo.maxLength} {...common} />
      case 'select':
        return (
          <Form.Select {...common}>
            {campo.options.map((o) => (
              <option key={o} value={o}>
                {o}
              </option>
            ))}
          </Form.Select>
        )
      case 'entity-select':
        return (
          <Form.Select {...common} disabled={!!loadingOptions[campo.name]}>
            <option value="">
              {loadingOptions[campo.name] ? 'Cargando...' : 'Selecciona una opción'}
            </option>
            {(entityOptions[campo.name] ?? []).map((o) => (
              <option key={campo.optionValue(o)} value={campo.optionValue(o)}>
                {campo.optionLabel(o)}
              </option>
            ))}
          </Form.Select>
        )
      default:
        return <Form.Control maxLength={campo.maxLength} {...common} />
    }
  }

  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton className={styles.modalHeader}>
        <Modal.Title className={styles.modalTitle}>
          {editing ? `Editar ${config.singular} #${item[config.idKey]}` : `Nueva ${config.singular}`}
        </Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit(onSave)} noValidate>
        <Modal.Body>
          <Row className="g-3">
            {camposVisibles.map((campo) => (
              <Col md={campo.width} key={campo.name}>
                <Form.Group controlId={campo.name}>
                  <Form.Label>{campo.label}</Form.Label>
                  {renderControl(campo)}
                  {errors[campo.name]?.message && (
                    <Form.Control.Feedback type="invalid">{errors[campo.name].message}</Form.Control.Feedback>
                  )}
                </Form.Group>
              </Col>
            ))}
          </Row>
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

EntidadForm.propTypes = {
  show: PropTypes.bool.isRequired,
  config: PropTypes.object,
  item: PropTypes.object,
  saving: PropTypes.bool,
  onSave: PropTypes.func.isRequired,
  onClose: PropTypes.func.isRequired,
}
