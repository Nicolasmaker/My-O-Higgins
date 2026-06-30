import { useState } from 'react';
import Button from '../UI/Button';
import styles from './Navbar.module.css';

export default function Navbar() {
  const [activeLink, setActiveLink] = useState('inicio');

  const navLinks = [
    { id: 'inicio', label: 'Inicio', href: '#inicio' },
    { id: 'admision', label: 'Admisión', href: '#admision' },
    { id: 'nosotros', label: 'Nosotros', href: '#noticias' },
    { id: 'contacto', label: 'Contacto', href: '#contacto' },
  ];

  return (
    <nav className={styles.navbar}>
      <div className={styles.container}>
        {/* Logo */}
        <div className={styles.logo}>
          <span className={styles.logoText}>My O'Higgins</span>
        </div>

        {/* Links del Centro */}
        <div className={styles.navLinks}>
          {navLinks.map((link) => (
            <a
              key={link.id}
              href={link.href}
              className={`${styles.navLink} ${
                activeLink === link.id ? styles.active : ''
              }`}
              onClick={() => setActiveLink(link.id)}
            >
              {link.label}
            </a>
          ))}
        </div>

        {/* Botón Login */}
        <div className={styles.navActions}>
          <Button variant="primary">Iniciar Sesión</Button>
        </div>
      </div>
    </nav>
  );
}
