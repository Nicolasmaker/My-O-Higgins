import styles from './Footer.module.css';

export default function Footer() {
  const currentYear = new Date().getFullYear();

  return (
    <footer className={styles.footer}>
      {/* Contenido Principal del Footer */}
      <div className={styles.container}>
        {/* Columna 1: Branding */}
        <div className={styles.column}>
          <h3 className={styles.columnTitle}>My O'Higgins</h3>
          <p className={styles.columnDescription}>
            Plataforma de gestión institucional para el Colegio Bernardo O'Higgins.
            Facilita la administración académica y comunicación efectiva.
          </p>
          <div className={styles.socialLinks}>
            <a href="#" className={styles.socialIcon} aria-label="Facebook">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
                <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z" />
              </svg>
            </a>
            <a href="#" className={styles.socialIcon} aria-label="Instagram">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 0C5.373 0 0 5.373 0 12s5.373 12 12 12 12-5.373 12-12S18.627 0 12 0zm0 22C6.48 22 2 17.52 2 12S6.48 2 12 2s10 4.48 10 10-4.48 10-10 10zm3.5-10c0-1.933-1.567-3.5-3.5-3.5S8.5 10.067 8.5 12s1.567 3.5 3.5 3.5 3.5-1.567 3.5-3.5zm1.5 0c0 2.485-2.015 4.5-4.5 4.5S7.5 14.485 7.5 12 9.515 7.5 12 7.5s4.5 2.015 4.5 4.5zm1.6-4.6c0 .552-.448 1-1 1s-1-.448-1-1 .448-1 1-1 1 .448 1 1z" />
              </svg>
            </a>
          </div>
        </div>

        {/* Columna 2: Enlaces Rápidos */}
        <div className={styles.column}>
          <h3 className={styles.columnTitle}>Enlaces Rápidos</h3>
          <ul className={styles.linkList}>
            <li>
              <a href="#" className={styles.link}>
                Portal Apoderados
              </a>
            </li>
            <li>
              <a href="#" className={styles.link}>
                Portal Alumnos
              </a>
            </li>
            <li>
              <a href="#" className={styles.link}>
                Calendario Escolar
              </a>
            </li>
            <li>
              <a href="#" className={styles.link}>
                Proceso de Matrícula
              </a>
            </li>
            <li>
              <a href="#" className={styles.link}>
                Certificados
              </a>
            </li>
            <li>
              <a href="#" className={styles.link}>
                Noticias
              </a>
            </li>
          </ul>
        </div>

        {/* Columna 3: Contacto */}
        <div className={styles.column}>
          <h3 className={styles.columnTitle}>Contacto</h3>
          <div className={styles.contactItem}>
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z" />
              <circle cx="12" cy="10" r="3" />
            </svg>
            <div>
              <p className={styles.contactLabel}>Dirección</p>
              <p className={styles.contactValue}>Avenida Principal 1234, Santiago, Chile</p>
            </div>
          </div>
          <div className={styles.contactItem}>
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z" />
            </svg>
            <div>
              <p className={styles.contactLabel}>Teléfono</p>
              <p className={styles.contactValue}>+56 2 1234 5678</p>
            </div>
          </div>
        </div>
      </div>

      {/* Barra Inferior - Copyright y Enlaces Legales */}
      <div className={styles.bottomBar}>
        <div className={styles.copyright}>
          <p>&copy; {currentYear} Colegio Bernardo O'Higgins. Todos los derechos reservados.</p>
        </div>
        <div className={styles.legalLinks}>
          <a href="#" className={styles.legalLink}>
            Política de Privacidad
          </a>
          <a href="#" className={styles.legalLink}>
            Términos de Uso
          </a>
          <a href="#" className={styles.legalLink}>
            Cookies
          </a>
        </div>
      </div>
    </footer>
  );
}
