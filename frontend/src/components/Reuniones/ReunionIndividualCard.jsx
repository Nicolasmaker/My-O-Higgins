// =============================================================
// TARJETA ENTREVISTA INDIVIDUAL — ReunionIndividualCard.jsx
// =============================================================
// Muestra una entrevista individual con su estado de firmas.
// Las firmas solo avanzan de pendiente (0) a firmado (1), según
// la regla del backend (RF29).
// =============================================================
import { useState } from 'react'
import PropTypes from 'prop-types'
import { Card, Badge, Button, Form } from 'react-bootstrap'
import styles from '../../styles/Reuniones.module.css'

function FirmaBadge({ firmado, label }) {
  return (
    <Badge bg={firmado ? 'success' : 'warning'} text={firmado ? undefined : 'dark'}>
      {label}: {firmado ? 'Firmado' : 'Pendiente'}
    </Badge>
  )
}

FirmaBadge.propTypes = {
  firmado: PropTypes.bool.isRequired,
  label: PropTypes.string.isRequired,
}

const ESTADO_BADGE = { PENDIENTE: 'warning', ACEPTADA: 'success', RECHAZADA: 'danger' }
const ESTADO_LABEL = { PENDIENTE: 'Pendiente', ACEPTADA: 'Aceptada', RECHAZADA: 'Rechazada' }

export default function ReunionIndividualCard({
  reunion,
  formatDate,
  onFirmar,
  onConfirmar,
  onRellenarBitacora,
  canEdit,
  puedeConfirmar,
  puedeFirmarFuncionario,
  puedeFirmarApoderado,
}) {
  const [saving, setSaving] = useState(false)
  const [confirmando, setConfirmando] = useState(false)
  const [editandoBitacora, setEditandoBitacora] = useState(false)
  const [guardandoBitacora, setGuardandoBitacora] = useState(false)
  const [compromisos, setCompromisos] = useState(reunion.bitReunionApoderado?.bitReuCompromisos || '')
  const [obs, setObs] = useState(reunion.bitReunionApoderado?.bitReuObs || '')
  const [temasTratados, setTemasTratados] = useState(reunion.bitReuIndTemTrat || '')

  const firmaDoc = reunion.bitReuIndFirmaDoc === 1
  const firmaApo = reunion.bitReuIndFirmaApo === 1
  const base = reunion.bitReunionApoderado
  const puedeRellenarBitacora = canEdit && base?.estadoConfirmacion === 'ACEPTADA'

  const handleFirmar = async (campo) => {
    setSaving(true)
    await onFirmar(reunion.idBitReuInd, {
      firmaDoc: campo === 'doc' ? 1 : reunion.bitReuIndFirmaDoc,
      firmaApo: campo === 'apo' ? 1 : reunion.bitReuIndFirmaApo,
    })
    setSaving(false)
  }

  const handleConfirmar = async (estadoConfirmacion) => {
    setConfirmando(true)
    await onConfirmar(base?.idBitReu, estadoConfirmacion)
    setConfirmando(false)
  }

  const handleGuardarBitacora = async () => {
    setGuardandoBitacora(true)
    const ok = await onRellenarBitacora(reunion, {
      bitReuCompromisos: compromisos,
      bitReuObs: obs,
      bitReuIndTemTrat: temasTratados,
    })
    setGuardandoBitacora(false)
    if (ok) setEditandoBitacora(false)
  }

  return (
    <Card className={styles.reunionCard}>
      <Card.Header className={styles.cardHeader}>
        <span>Entrevista #{reunion.idBitReuInd}</span>
        <div className="d-flex align-items-center gap-2">
          {base?.estadoConfirmacion && (
            <Badge bg={ESTADO_BADGE[base.estadoConfirmacion] || 'secondary'}>
              {ESTADO_LABEL[base.estadoConfirmacion] || base.estadoConfirmacion}
            </Badge>
          )}
          {base?.bitReuFec && <span className={styles.cardFecha}>{formatDate(base.bitReuFec)}</span>}
        </div>
      </Card.Header>
      <Card.Body>
        <p className={styles.fieldBlock}>
          <span className={styles.fieldLabel}>Motivo</span>
          {reunion.bitReuIndMotivReu}
        </p>
        {editandoBitacora ? (
          <>
            <Form.Group className="mb-2">
              <Form.Label className={styles.fieldLabel}>Temas tratados</Form.Label>
              <Form.Control
                as="textarea"
                rows={2}
                maxLength={100}
                value={temasTratados}
                onChange={(e) => setTemasTratados(e.target.value)}
              />
            </Form.Group>
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
              <Form.Control as="textarea" rows={2} maxLength={300} value={obs} onChange={(e) => setObs(e.target.value)} />
            </Form.Group>
            <div className="d-flex gap-2 justify-content-end mb-3">
              <Button size="sm" variant="outline-secondary" onClick={() => setEditandoBitacora(false)} disabled={guardandoBitacora}>
                Cancelar
              </Button>
              <Button size="sm" className={styles.btnGranate} onClick={handleGuardarBitacora} disabled={guardandoBitacora}>
                {guardandoBitacora ? 'Guardando...' : 'Guardar bitácora'}
              </Button>
            </div>
          </>
        ) : (
          <>
            {reunion.bitReuIndTemTrat && (
              <p className={styles.fieldBlock}>
                <span className={styles.fieldLabel}>Temas tratados</span>
                {reunion.bitReuIndTemTrat}
              </p>
            )}
            {base?.bitReuCompromisos && (
              <p className={styles.fieldBlock}>
                <span className={styles.fieldLabel}>Compromisos</span>
                {base.bitReuCompromisos}
              </p>
            )}
          </>
        )}
        {reunion.idAnotacion && (
          <p className={styles.fieldBlock}>
            <span className={styles.fieldLabel}>Origen</span>
            Vinculada a anotación #{reunion.idAnotacion}
          </p>
        )}

        <div className="d-flex gap-2 flex-wrap mb-3">
          <FirmaBadge firmado={firmaDoc} label="Funcionario" />
          <FirmaBadge firmado={firmaApo} label="Apoderado" />
        </div>

        {puedeConfirmar && (
          <div className="d-flex gap-2 justify-content-end mb-2">
            <Button
              size="sm"
              variant="outline-success"
              disabled={confirmando}
              onClick={() => handleConfirmar('ACEPTADA')}
            >
              Aceptar
            </Button>
            <Button
              size="sm"
              variant="outline-danger"
              disabled={confirmando}
              onClick={() => handleConfirmar('RECHAZADA')}
            >
              Rechazar
            </Button>
          </div>
        )}

        {puedeRellenarBitacora && !editandoBitacora && (
          <div className="d-flex justify-content-end mb-2">
            <Button size="sm" variant="outline-secondary" onClick={() => setEditandoBitacora(true)}>
              Rellenar bitácora
            </Button>
          </div>
        )}

        {/* Cada quien firma solo SU sección: el funcionario la del funcionario,
            el apoderado dueño la del apoderado. Un funcionario no puede firmar
            por el apoderado ni viceversa. */}
        {((puedeFirmarFuncionario && !firmaDoc) || (puedeFirmarApoderado && !firmaApo)) && (
          <div className="d-flex gap-2 justify-content-end">
            {puedeFirmarFuncionario && !firmaDoc && (
              <Button size="sm" variant="outline-secondary" disabled={saving} onClick={() => handleFirmar('doc')}>
                Firmar funcionario
              </Button>
            )}
            {puedeFirmarApoderado && !firmaApo && (
              <Button size="sm" variant="outline-secondary" disabled={saving} onClick={() => handleFirmar('apo')}>
                Firmar apoderado
              </Button>
            )}
          </div>
        )}
      </Card.Body>
    </Card>
  )
}

ReunionIndividualCard.propTypes = {
  reunion: PropTypes.object.isRequired,
  formatDate: PropTypes.func.isRequired,
  onFirmar: PropTypes.func.isRequired,
  onConfirmar: PropTypes.func,
  onRellenarBitacora: PropTypes.func,
  canEdit: PropTypes.bool,
  puedeConfirmar: PropTypes.bool,
  puedeFirmarFuncionario: PropTypes.bool,
  puedeFirmarApoderado: PropTypes.bool,
}
