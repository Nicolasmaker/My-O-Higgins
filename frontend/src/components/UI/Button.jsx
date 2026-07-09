import styles from '../../styles/Button.module.css';

/**
 * Componente Button Reutilizable
 * @param {string} variant - 'primary' (relleno) o 'outline' (borde)
 * @param {string} className - Clases CSS adicionales
 * @param {function} onClick - Función a ejecutar al hacer clic
 * @param {boolean} disabled - Deshabilitar botón
 * @param {ReactNode} children - Contenido del botón
 * @param {string} type - Tipo de botón: 'button', 'submit', 'reset'
 */
export default function Button({
  variant = 'primary',
  className = '',
  onClick,
  disabled = false,
  children,
  type = 'button',
  ...props
}) {
  const buttonClass = `${styles.button} ${styles[variant]} ${className}`;

  return (
    <button
      className={buttonClass}
      onClick={onClick}
      disabled={disabled}
      type={type}
      {...props}
    >
      {children}
    </button>
  );
}
