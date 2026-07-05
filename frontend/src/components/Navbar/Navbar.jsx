// =============================================================
// BARRA DE NAVEGACIÓN — Navbar.jsx
// =============================================================
// Navegación principal conectada a react-router.
//
//   - Sin sesión: links institucionales (anclas del Home) +
//     botón "Iniciar Sesión" que lleva a /login.
//   - Con sesión: links a todos los módulos del sistema +
//     nombre del usuario y "Salir".
//
// NavLink marca automáticamente el link activo según la ruta.
// =============================================================
import { useState } from 'react';
import { NavLink, Link, useNavigate } from 'react-router-dom';
import { Modal } from 'react-bootstrap';
import { useAuth } from '../../hooks/useAuth';
import Button from '../UI/Button';
import styles from './Navbar.module.css';

const MODULOS = [
  { to: '/', label: 'Inicio', end: true },
  { to: '/anotaciones', label: 'Anotaciones' },
  { to: '/calendario', label: 'Calendario' },
  { to: '/reuniones', label: 'Reuniones' },
  { to: '/mensajeria', label: 'Mensajería' },
  { to: '/matriculas', label: 'Matrículas' },
  { to: '/hoja-de-vida', label: 'Hoja de Vida' },
  { to: '/academico', label: 'Académico' },
];

const PUBLICOS = [
  { href: '#inicio', label: 'Inicio' },
  { href: '#admision', label: 'Admisión' },
  { href: '#noticias', label: 'Nosotros' },
  { href: '#contacto', label: 'Contacto' },
];

export default function Navbar() {
  const { usuario, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();
  const [showLogoutModal, setShowLogoutModal] = useState(false);

  const handleLogout = () => {
    setShowLogoutModal(false);
    logout();
    navigate('/');
  };

  return (
    <nav className={styles.navbar}>
      <div className={styles.container}>
        {/* Logo */}
        <Link to="/" className={styles.logo}>
          <span className={styles.logoText}>My O'Higgins</span>
        </Link>

        {/* Links del Centro */}
        <div className={styles.navLinks}>
          {isAuthenticated
            ? MODULOS.map((link) => (
                <NavLink
                  key={link.to}
                  to={link.to}
                  end={link.end}
                  className={({ isActive }) =>
                    `${styles.navLink} ${isActive ? styles.active : ''}`
                  }
                >
                  {link.label}
                </NavLink>
              ))
            : PUBLICOS.map((link) => (
                <a key={link.href} href={link.href} className={styles.navLink}>
                  {link.label}
                </a>
              ))}
        </div>

        {/* Sesión */}
        <div className={styles.navActions}>
          {isAuthenticated ? (
            <div className={styles.sessionBox}>
              <span className={styles.userName}>
                {`${usuario?.usuPNombre || ''} ${usuario?.usuApePat || ''}`.trim() || 'Usuario'}
              </span>
              <Button variant="primary" onClick={() => setShowLogoutModal(true)}>
                Cerrar sesión
              </Button>
            </div>
          ) : (
            <Button variant="primary" onClick={() => navigate('/login')}>
              Iniciar Sesión
            </Button>
          )}
        </div>
      </div>

      <Modal show={showLogoutModal} onHide={() => setShowLogoutModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>Cerrar sesión</Modal.Title>
        </Modal.Header>
        <Modal.Body>¿Seguro que quieres cerrar sesión?</Modal.Body>
        <Modal.Footer>
          <Button variant="outline" onClick={() => setShowLogoutModal(false)}>
            Cancelar
          </Button>
          <Button variant="primary" onClick={handleLogout}>
            Cerrar sesión
          </Button>
        </Modal.Footer>
      </Modal>
    </nav>
  );
}
