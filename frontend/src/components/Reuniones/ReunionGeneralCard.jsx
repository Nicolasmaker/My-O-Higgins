// =============================================================
// TARJETA REUNIÓN GENERAL — ReunionGeneralCard.jsx
// =============================================================
// Muestra una reunión general de curso. El comunicado/acuerdos se
// completan después de la reunión con "Completar acta" — no hay
// flujo de aceptar/rechazar (es un aviso al curso, no una invitación
// 1-a-1).
// =============================================================
import { useState } from 'react'
import PropTypes from 'prop-types'
import { Card, Badge, Button, Form } from 'react-bootstrap'
import { formatRut } from '../../utils/formatRut'
import styles from '../../styles/Reuniones.module.css'

export default function ReunionGeneralCard({ reunion, formatDate, cursoLabel, onCompletarActa, canManage }) {
  const base = reunion.bitReunionApoderado
  const esOrdinaria = String(reunion.bitReuGenTipReu || '').toLowerCase() === 'ordinaria'

  const [editando, setEditando] = useState(false)
  const [guardando, setGuardando] = useState(false)
  const [comunicado, setComunicado] = useState(reunion.bitReuGenComunicEmi || '')
  const [acuerdos, setAcuerdos] = useState(reunion.bitReuGenAcuerTrat || '')
  const [obs, setObs] = useState(reunion.bitReuGenObs || '')

  const handleGuardar = async () => {
    setGuardando(true)
    const ok = await onCompletarActa(reunion.bitReuGen, {
      bitReuGenComunicEmi: comunicado,
      bitReuGenAcuerTrat: acuerdos,
      bitReuGenObs: obs,
    })
    setGuardando(false)
    if (ok) setEditando(false)
  }

  return (
    <Card className={styles.reunionCard}>
      <Card.Header className={styles.cardHeader}>
        <span>
          Reunión de curso #{reunion.bitReuGen}{' '}
          <Badge bg={esOrdinaria ? 'secondary' : 'danger'} className="ms-1">
            {reunion.bitReuGenTipReu}
          </Badge>
        </span>
        {base?.bitReuFec && <span className={styles.cardFecha}>{formatDate(base.bitReuFec)}</span>}
      </Card.Header>
      <Card.Body>
        <p className={styles.fieldBlock}>
          <span className={styles.fieldLabel}>Curso</span>
          {cursoLabel || '—'}
        </p>

        {editando ? (
          <>
            <Form.Group className="mb-2">
              <Form.Label className={styles.fieldLabel}>Comunicado emitido</Form.Label>
              <Form.Control as="textarea" rows={2} maxLength={200} value={comunicado} onChange={(e) => setComunicado(e.target.value)} />
            </Form.Group>
            <Form.Group className="mb-2">
              <Form.Label className={styles.fieldLabel}>Acuerdos tratados</Form.Label>
              <Form.Control as="textarea" rows={2} maxLength={200} value={acuerdos} onChange={(e) => setAcuerdos(e.target.value)} />
            </Form.Group>
            <Form.Group className="mb-2">
              <Form.Label className={styles.fieldLabel}>Observaciones</Form.Label>
              <Form.Control as="textarea" rows={2} maxLength={300} value={obs} onChange={(e) => setObs(e.target.value)} />
            </Form.Group>
            <div className="d-flex gap-2 justify-content-end">
              <Button size="sm" variant="outline-secondary" onClick={() => setEditando(false)} disabled={guardando}>
                Cancelar
              </Button>
              <Button size="sm" className={styles.btnGranate} onClick={handleGuardar} disabled={guardando}>
                {guardando ? 'Guardando...' : 'Guardar acta'}
              </Button>
            </div>
          </>
        ) : (
          <>
            {reunion.bitReuGenComunicEmi ? (
              <p className={styles.fieldBlock}>
                <span className={styles.fieldLabel}>Comunicado emitido</span>
                {reunion.bitReuGenComunicEmi}
              </p>
            ) : (
              <p className={styles.sectionEmpty}>Acta pendiente de completar.</p>
            )}
            {reunion.bitReuGenAcuerTrat && (
              <p className={styles.fieldBlock}>
                <span className={styles.fieldLabel}>Acuerdos tratados</span>
                {reunion.bitReuGenAcuerTrat}
              </p>
            )}
            {reunion.bitReuGenObs && (
              <p className={styles.fieldBlock}>
                <span className={styles.fieldLabel}>Observaciones</span>
                {reunion.bitReuGenObs}
              </p>
            )}
            {canManage && (
              <div className="d-flex justify-content-end">
                <Button size="sm" variant="outline-secondary" onClick={() => setEditando(true)}>
                  Completar acta
                </Button>
              </div>
            )}
          </>
        )}

        {base?.docenteUsuRut && <small className="text-muted d-block mt-2">Funcionario: {formatRut(base.docenteUsuRut, base.docenteDv)}</small>}
      </Card.Body>
    </Card>
  )
}

ReunionGeneralCard.propTypes = {
  reunion: PropTypes.object.isRequired,
  formatDate: PropTypes.func.isRequired,
  cursoLabel: PropTypes.string,
  onCompletarActa: PropTypes.func,
  canManage: PropTypes.bool,
}
