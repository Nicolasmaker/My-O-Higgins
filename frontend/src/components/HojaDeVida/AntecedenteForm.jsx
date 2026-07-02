// =============================================================
// FORMULARIO DE ANTECEDENTES — AntecedenteForm.jsx
// =============================================================
// Modal único para crear/editar los tres tipos de antecedente
// de una hoja de vida (académicos, apoderado, médicos).
// Los campos se definen por configuración según el tipo; el
// idHojaVida viene de la hoja seleccionada en la página.
// =============================================================
import { useEffect } from 'react'
import { useForm } from 'react-hook-form'
import PropTypes from 'prop-types'
import { Modal, Form, Row, Col, Button, Spinner } from 'react-bootstrap'
import styles from '../../pages/HojaDeVida/HojaDeVida.module.css'

const TIPO_SANGRE = ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-']

// Configuración de campos por tipo de antecedente.
// type: control a renderizar · width: columnas md · rules: validación RHF
export const TIPOS_ANTECEDENTE = {
  academico: {
    titulo: 'Antecedente académico',
    campos: [
      { name: 'anioEscolar', label: 'Año escolar *', type: 'number', width: 6, rules: { required: 'El año escolar es obligatorio' } },
      { name: 'promedioGeneralActual', label: 'Promedio general *', type: 'decimal', width: 6, rules: { required: 'El promedio es obligatorio', min: { value: 1, message: 'Mínimo 1.0' }, max: { value: 7, message: 'Máximo 7.0' } } },
      { name: 'situacionFinalAprobacion', label: 'Situación final: ¿aprobado? *', type: 'siNo', width: 6, rules: { required: true } },
    ],
    defaults: { anioEscolar: new Date().getFullYear(), promedioGeneralActual: '', situacionFinalAprobacion: 'S' },
  },
  apoderado: {
    titulo: 'Antecedente de apoderado',
    campos: [
      { name: 'nombre', label: 'Nombre completo *', maxLength: 80, width: 12, rules: { required: 'El nombre es obligatorio' } },
      { name: 'profesion', label: 'Profesión *', maxLength: 30, width: 6, rules: { required: 'La profesión es obligatoria' } },
      { name: 'telefono', label: 'Teléfono *', maxLength: 10, width: 6, rules: { required: 'El teléfono es obligatorio' } },
      { name: 'direccion', label: 'Dirección *', maxLength: 100, width: 12, rules: { required: 'La dirección es obligatoria' } },
      { name: 'lugarTrabajo', label: 'Lugar de trabajo *', maxLength: 100, width: 6, rules: { required: 'El lugar de trabajo es obligatorio' } },
      { name: 'disponibilidadHoraria', label: '¿Disponibilidad horaria? *', type: 'siNo', width: 6, rules: { required: true } },
    ],
    defaults: { nombre: '', profesion: '', telefono: '', direccion: '', lugarTrabajo: '', disponibilidadHoraria: 'S' },
  },
  medico: {
    titulo: 'Antecedente médico',
    campos: [
      { name: 'tipoSangre', label: 'Tipo de sangre *', type: 'sangre', width: 6, rules: { required: true } },
      { name: 'alergias', label: 'Alergias *', maxLength: 100, width: 6, rules: { required: 'Indica alergias (o "Ninguna")' } },
      { name: 'medicamentos', label: 'Medicamentos *', maxLength: 100, width: 12, rules: { required: 'Indica medicamentos (o "Ninguno")' } },
      { name: 'condicionesMedicas', label: 'Condiciones médicas *', type: 'textarea', maxLength: 1000, width: 12, rules: { required: 'Indica condiciones médicas (o "Ninguna")' } },
      { name: 'observaciones', label: 'Observaciones', type: 'textarea', maxLength: 500, width: 12 },
    ],
    defaults: { tipoSangre: 'O+', alergias: '', medicamentos: '', condicionesMedicas: '', observaciones: '' },
  },
}

export default function AntecedenteForm({ show, tipo, antecedente, saving, onSave, onClose }) {
  const config = TIPOS_ANTECEDENTE[tipo]
  const editing = !!antecedente

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm()

  useEffect(() => {
    if (!show || !config) return
    if (editing) {
      const values = {}
      config.campos.forEach(({ name }) => {
        values[name] = antecedente[name] ?? ''
      })
      reset(values)
    } else {
      reset(config.defaults)
    }
  }, [show, editing, antecedente, config, reset])

  if (!config) return null

  const renderControl = (campo) => {
    const common = {
      isInvalid: !!errors[campo.name],
      ...register(campo.name, campo.rules),
    }
    switch (campo.type) {
      case 'number':
        return <Form.Control type="number" {...common} />
      case 'decimal':
        return <Form.Control type="number" step="0.1" {...common} />
      case 'textarea':
        return <Form.Control as="textarea" rows={3} maxLength={campo.maxLength} {...common} />
      case 'siNo':
        return (
          <Form.Select {...common}>
            <option value="S">Sí</option>
            <option value="N">No</option>
          </Form.Select>
        )
      case 'sangre':
        return (
          <Form.Select {...common}>
            {TIPO_SANGRE.map((t) => (
              <option key={t} value={t}>
                {t}
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
          {editing ? `Editar ${config.titulo.toLowerCase()}` : `Nuevo ${config.titulo.toLowerCase()}`}
        </Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit(onSave)} noValidate>
        <Modal.Body>
          <Row className="g-3">
            {config.campos.map((campo) => (
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

AntecedenteForm.propTypes = {
  show: PropTypes.bool.isRequired,
  tipo: PropTypes.oneOf(['academico', 'apoderado', 'medico']),
  antecedente: PropTypes.object,
  saving: PropTypes.bool,
  onSave: PropTypes.func.isRequired,
  onClose: PropTypes.func.isRequired,
}
