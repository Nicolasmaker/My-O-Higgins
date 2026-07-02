// =============================================================
// TARJETA REUNIÓN GENERAL — ReunionGeneralCard.jsx
// =============================================================
// Muestra un acta de reunión general de coordinación.
// Solo lectura: el backend no expone edición para este tipo.
// =============================================================
import PropTypes from 'prop-types'
import { Card, Badge } from 'react-bootstrap'
import styles from '../../pages/Reuniones/Reuniones.module.css'

export default function ReunionGeneralCard({ reunion, formatDate }) {
  const base = reunion.bitReunionApoderado
  const esOrdinaria = String(reunion.bitReuGenTipReu || '').toLowerCase() === 'ordinaria'

  return (
    <Card className={styles.reunionCard}>
      <Card.Header className={styles.cardHeader}>
        <span>
          Acta #{reunion.bitReuGen}{' '}
          <Badge bg={esOrdinaria ? 'secondary' : 'danger'} className="ms-1">
            {reunion.bitReuGenTipReu}
          </Badge>
        </span>
        {base?.bitReuFec && <span className={styles.cardFecha}>{formatDate(base.bitReuFec)}</span>}
      </Card.Header>
      <Card.Body>
        <p className={styles.fieldBlock}>
          <span className={styles.fieldLabel}>Comunicado emitido</span>
          {reunion.bitReuGenComunicEmi}
        </p>
        <p className={styles.fieldBlock}>
          <span className={styles.fieldLabel}>Acuerdos tratados</span>
          {reunion.bitReuGenAcuerTrat}
        </p>
        {reunion.bitReuGenObs && (
          <p className={styles.fieldBlock}>
            <span className={styles.fieldLabel}>Observaciones</span>
            {reunion.bitReuGenObs}
          </p>
        )}
        {base?.docenteUsuRut && <small className="text-muted">Funcionario: {base.docenteUsuRut}</small>}
      </Card.Body>
    </Card>
  )
}

ReunionGeneralCard.propTypes = {
  reunion: PropTypes.object.isRequired,
  formatDate: PropTypes.func.isRequired,
}
