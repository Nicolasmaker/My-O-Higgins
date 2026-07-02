// =============================================================
// TARJETA ENTREVISTA INDIVIDUAL — ReunionIndividualCard.jsx
// =============================================================
// Muestra una entrevista individual con su estado de firmas.
// Las firmas solo avanzan de pendiente (0) a firmado (1), según
// la regla del backend (RF29).
// =============================================================
import { useState } from 'react'
import PropTypes from 'prop-types'
import { Card, Badge, Button } from 'react-bootstrap'
import styles from '../../pages/Reuniones/Reuniones.module.css'

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

export default function ReunionIndividualCard({ reunion, formatDate, onFirmar, canEdit }) {
  const [saving, setSaving] = useState(false)

  const firmaDoc = reunion.bitReuIndFirmaDoc === 1
  const firmaApo = reunion.bitReuIndFirmaApo === 1
  const base = reunion.bitReunionApoderado

  const handleFirmar = async (campo) => {
    setSaving(true)
    await onFirmar(reunion.idBitReuInd, {
      firmaDoc: campo === 'doc' ? 1 : reunion.bitReuIndFirmaDoc,
      firmaApo: campo === 'apo' ? 1 : reunion.bitReuIndFirmaApo,
    })
    setSaving(false)
  }

  return (
    <Card className={styles.reunionCard}>
      <Card.Header className={styles.cardHeader}>
        <span>Entrevista #{reunion.idBitReuInd}</span>
        {base?.bitReuFec && <span className={styles.cardFecha}>{formatDate(base.bitReuFec)}</span>}
      </Card.Header>
      <Card.Body>
        <p className={styles.fieldBlock}>
          <span className={styles.fieldLabel}>Motivo</span>
          {reunion.bitReuIndMotivReu}
        </p>
        <p className={styles.fieldBlock}>
          <span className={styles.fieldLabel}>Temas tratados</span>
          {reunion.bitReuIndTemTrat}
        </p>
        {base?.bitReuCompromisos && (
          <p className={styles.fieldBlock}>
            <span className={styles.fieldLabel}>Compromisos</span>
            {base.bitReuCompromisos}
          </p>
        )}

        <div className="d-flex gap-2 flex-wrap mb-3">
          <FirmaBadge firmado={firmaDoc} label="Funcionario" />
          <FirmaBadge firmado={firmaApo} label="Apoderado" />
        </div>

        {canEdit && (!firmaDoc || !firmaApo) && (
          <div className="d-flex gap-2 justify-content-end">
            {!firmaDoc && (
              <Button size="sm" variant="outline-secondary" disabled={saving} onClick={() => handleFirmar('doc')}>
                Firmar funcionario
              </Button>
            )}
            {!firmaApo && (
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
  canEdit: PropTypes.bool,
}
