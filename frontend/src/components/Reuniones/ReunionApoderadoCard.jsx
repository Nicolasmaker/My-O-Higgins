// =============================================================
// TARJETA BITÁCORA BASE — ReunionApoderadoCard.jsx
// =============================================================
// Muestra una bitácora base de reunión con apoderado.
// Permite corregir compromisos/observaciones (RF29): el backend
// solo acepta modificar esos dos campos, nunca fecha ni autor.
// =============================================================
import { useState } from 'react'
import PropTypes from 'prop-types'
import { Card, Button, Form } from 'react-bootstrap'
import styles from '../../pages/Reuniones/Reuniones.module.css'

export default function ReunionApoderadoCard({ reunion, formatDate, onUpdate, canEdit }) {
  const [editing, setEditing] = useState(false)
  const [compromisos, setCompromisos] = useState(reunion.bitReuCompromisos || '')
  const [obs, setObs] = useState(reunion.bitReuObs || '')
  const [saving, setSaving] = useState(false)

  const handleSave = async () => {
    setSaving(true)
    const ok = await onUpdate(reunion.idBitReu, { bitReuCompromisos: compromisos, bitReuObs: obs })
    setSaving(false)
    if (ok) setEditing(false)
  }

  return (
    <Card className={styles.reunionCard}>
      <Card.Header className={styles.cardHeader}>
        <span>Bitácora #{reunion.idBitReu}</span>
        <span className={styles.cardFecha}>{formatDate(reunion.bitReuFec)}</span>
      </Card.Header>
      <Card.Body>
        {editing ? (
          <>
            <Form.Group className="mb-2">
              <Form.Label className={styles.fieldLabel}>Compromisos</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                maxLength={1000}
                value={compromisos}
                onChange={(e) => setCompromisos(e.target.value)}
              />
            </Form.Group>
            <Form.Group className="mb-2">
              <Form.Label className={styles.fieldLabel}>Observaciones</Form.Label>
              <Form.Control
                as="textarea"
                rows={2}
                maxLength={300}
                value={obs}
                onChange={(e) => setObs(e.target.value)}
              />
            </Form.Group>
            <div className="d-flex gap-2 justify-content-end">
              <Button size="sm" variant="outline-secondary" onClick={() => setEditing(false)} disabled={saving}>
                Cancelar
              </Button>
              <Button size="sm" className={styles.btnGranate} onClick={handleSave} disabled={saving}>
                {saving ? 'Guardando...' : 'Guardar'}
              </Button>
            </div>
          </>
        ) : (
          <>
            <p className={styles.fieldBlock}>
              <span className={styles.fieldLabel}>Compromisos</span>
              {reunion.bitReuCompromisos || 'Sin compromisos registrados'}
            </p>
            <p className={styles.fieldBlock}>
              <span className={styles.fieldLabel}>Observaciones</span>
              {reunion.bitReuObs || 'Sin observaciones'}
            </p>
            <div className="d-flex justify-content-between align-items-center">
              <small className="text-muted">Funcionario: {reunion.docenteUsuRut}</small>
              {canEdit && (
                <Button size="sm" variant="outline-secondary" onClick={() => setEditing(true)}>
                  Editar compromisos
                </Button>
              )}
            </div>
          </>
        )}
      </Card.Body>
    </Card>
  )
}

ReunionApoderadoCard.propTypes = {
  reunion: PropTypes.object.isRequired,
  formatDate: PropTypes.func.isRequired,
  onUpdate: PropTypes.func.isRequired,
  canEdit: PropTypes.bool,
}
