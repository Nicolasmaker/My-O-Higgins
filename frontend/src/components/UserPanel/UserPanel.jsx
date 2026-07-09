import { createPortal } from 'react-dom'
import PropTypes from 'prop-types'
import { toast } from 'react-toastify'
import Button from '../UI/Button'
import styles from '../../styles/UserPanel.module.css'

export default function UserPanel({ show, usuario, onClose }) {
  if (!show) return null

  const nombreCompleto = `${usuario?.usuPNombre || ''} ${usuario?.usuApePat || ''}`.trim() || 'Sesión activa'
  const roleLabel = (usuario?.rolNombre || usuario?.rol || 'Sin rol asignado').replace('ROLE_', '')

  const handleCambiarPassword = () => {
    toast.info('Funcionalidad de cambio de contraseña próximamente disponible')
  }

  return createPortal(
    <>
      <div className={styles.overlay} onClick={onClose} />
      <aside className={styles.panel}>
        <button type="button" className={styles.closeBtn} onClick={onClose} aria-label="Cerrar">
          ×
        </button>

        <h2 className={styles.title}>Mi cuenta</h2>

        <dl className={styles.infoList}>
          <div className={styles.infoRow}>
            <dt>Nombre</dt>
            <dd>{nombreCompleto}</dd>
          </div>
          <div className={styles.infoRow}>
            <dt>RUT</dt>
            <dd>{usuario?.usuRut || 'Sin dato'}</dd>
          </div>
          <div className={styles.infoRow}>
            <dt>Correo</dt>
            <dd>{usuario?.usuEmail || 'Sin dato'}</dd>
          </div>
          <div className={styles.infoRow}>
            <dt>Rol</dt>
            <dd>{roleLabel}</dd>
          </div>
        </dl>

        <Button type="button" className={styles.changePasswordBtn} onClick={handleCambiarPassword}>
          Cambiar contraseña
        </Button>
      </aside>
    </>,
    document.body
  )
}

UserPanel.propTypes = {
  show: PropTypes.bool.isRequired,
  usuario: PropTypes.object,
  onClose: PropTypes.func.isRequired,
}
