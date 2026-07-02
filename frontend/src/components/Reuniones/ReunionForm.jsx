// =============================================================
// FORMULARIO DE REUNIONES — ReunionForm.jsx
// =============================================================
// Formulario dinámico para registrar reuniones en MS-GestionReuniones.
// Según el tipo activo muestra campos distintos:
//
//   - apoderado  → solo bitácora base (fecha, compromisos, obs)
//   - individual → base + motivo y temas tratados (entrevista)
//   - general    → base + tipo de reunión, comunicado y acuerdos
//
// Los campos base son comunes porque el backend siempre crea
// primero una BitReunionApoderado y luego el desglose específico.
// =============================================================
import { useForm } from 'react-hook-form'
import PropTypes from 'prop-types'
import { Card, Form, Row, Col, Button, Spinner } from 'react-bootstrap'
import { rutRules } from '../../validators/fieldValidators'
import styles from '../../pages/Reuniones/Reuniones.module.css'

const TITULOS = {
  apoderado: 'Nueva reunión con apoderado',
  individual: 'Nueva entrevista individual',
  general: 'Nueva reunión general',
}

export default function ReunionForm({ tipo, defaultRut, saving, onSubmit, onCancel }) {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    defaultValues: {
      bitReuFec: '',
      bitReuCompromisos: '',
      bitReuObs: '',
      docenteUsuRut: String(defaultRut || ''),
      bitReuIndMotivReu: '',
      bitReuIndTemTrat: '',
      bitReuGenTipReu: 'Ordinaria',
      bitReuGenComunicEmi: '',
      bitReuGenAcuerTrat: '',
      bitReuGenObs: '',
    },
  })

  return (
    <Card className={styles.formCard}>
      <Card.Header className={styles.formHeader}>{TITULOS[tipo]}</Card.Header>
      <Card.Body>
        <Form onSubmit={handleSubmit(onSubmit)} noValidate>
          {/* ── Campos base (comunes a los tres tipos) ── */}
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
            <Col md={4}>
              <Form.Group controlId="docenteUsuRut">
                <Form.Label>RUT funcionario *</Form.Label>
                <Form.Control
                  placeholder="12345678 o 12345678-5"
                  isInvalid={!!errors.docenteUsuRut}
                  {...register('docenteUsuRut', rutRules)}
                />
                <Form.Control.Feedback type="invalid">{errors.docenteUsuRut?.message}</Form.Control.Feedback>
              </Form.Group>
            </Col>
          </Row>

          <Form.Group className="mt-3" controlId="bitReuCompromisos">
            <Form.Label>Compromisos *</Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              maxLength={1000}
              placeholder="Compromisos adquiridos en la reunión"
              isInvalid={!!errors.bitReuCompromisos}
              {...register('bitReuCompromisos', { required: 'Los compromisos son obligatorios' })}
            />
            <Form.Control.Feedback type="invalid">{errors.bitReuCompromisos?.message}</Form.Control.Feedback>
          </Form.Group>

          <Form.Group className="mt-3" controlId="bitReuObs">
            <Form.Label>Observaciones</Form.Label>
            <Form.Control
              as="textarea"
              rows={2}
              maxLength={300}
              placeholder="Notas adicionales (opcional)"
              {...register('bitReuObs')}
            />
          </Form.Group>

          {/* ── Desglose entrevista individual ── */}
          {tipo === 'individual' && (
            <Row className="g-3 mt-1">
              <Col md={6}>
                <Form.Group controlId="bitReuIndMotivReu">
                  <Form.Label>Motivo de la entrevista *</Form.Label>
                  <Form.Control
                    maxLength={100}
                    placeholder="Ej: Bajo rendimiento académico"
                    isInvalid={!!errors.bitReuIndMotivReu}
                    {...register('bitReuIndMotivReu', { required: 'El motivo es obligatorio' })}
                  />
                  <Form.Control.Feedback type="invalid">{errors.bitReuIndMotivReu?.message}</Form.Control.Feedback>
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group controlId="bitReuIndTemTrat">
                  <Form.Label>Temas tratados *</Form.Label>
                  <Form.Control
                    maxLength={100}
                    placeholder="Ej: Plan de apoyo familiar"
                    isInvalid={!!errors.bitReuIndTemTrat}
                    {...register('bitReuIndTemTrat', { required: 'Los temas tratados son obligatorios' })}
                  />
                  <Form.Control.Feedback type="invalid">{errors.bitReuIndTemTrat?.message}</Form.Control.Feedback>
                </Form.Group>
              </Col>
            </Row>
          )}

          {/* ── Desglose reunión general ── */}
          {tipo === 'general' && (
            <>
              <Row className="g-3 mt-1">
                <Col md={4}>
                  <Form.Group controlId="bitReuGenTipReu">
                    <Form.Label>Tipo de reunión *</Form.Label>
                    <Form.Select {...register('bitReuGenTipReu', { required: true })}>
                      <option value="Ordinaria">Ordinaria</option>
                      <option value="Extraordinaria">Extraordinaria</option>
                    </Form.Select>
                  </Form.Group>
                </Col>
              </Row>
              <Form.Group className="mt-3" controlId="bitReuGenComunicEmi">
                <Form.Label>Comunicado emitido *</Form.Label>
                <Form.Control
                  as="textarea"
                  rows={2}
                  maxLength={200}
                  placeholder="Lo comunicado durante la reunión"
                  isInvalid={!!errors.bitReuGenComunicEmi}
                  {...register('bitReuGenComunicEmi', { required: 'El comunicado es obligatorio' })}
                />
                <Form.Control.Feedback type="invalid">{errors.bitReuGenComunicEmi?.message}</Form.Control.Feedback>
              </Form.Group>
              <Form.Group className="mt-3" controlId="bitReuGenAcuerTrat">
                <Form.Label>Acuerdos tratados *</Form.Label>
                <Form.Control
                  as="textarea"
                  rows={2}
                  maxLength={200}
                  placeholder="Acuerdos y decisiones tomadas"
                  isInvalid={!!errors.bitReuGenAcuerTrat}
                  {...register('bitReuGenAcuerTrat', { required: 'Los acuerdos son obligatorios' })}
                />
                <Form.Control.Feedback type="invalid">{errors.bitReuGenAcuerTrat?.message}</Form.Control.Feedback>
              </Form.Group>
              <Form.Group className="mt-3" controlId="bitReuGenObs">
                <Form.Label>Observaciones generales</Form.Label>
                <Form.Control as="textarea" rows={2} maxLength={300} {...register('bitReuGenObs')} />
              </Form.Group>
            </>
          )}

          <div className="d-flex gap-2 justify-content-end mt-4">
            <Button variant="outline-secondary" type="button" onClick={onCancel} disabled={saving}>
              Cancelar
            </Button>
            <Button type="submit" className={styles.btnGranate} disabled={saving}>
              {saving ? (
                <>
                  <Spinner as="span" size="sm" animation="border" className="me-2" />
                  Guardando...
                </>
              ) : (
                'Guardar reunión'
              )}
            </Button>
          </div>
        </Form>
      </Card.Body>
    </Card>
  )
}

ReunionForm.propTypes = {
  tipo: PropTypes.oneOf(['apoderado', 'individual', 'general']).isRequired,
  defaultRut: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  saving: PropTypes.bool,
  onSubmit: PropTypes.func.isRequired,
  onCancel: PropTypes.func.isRequired,
}
