<<<<<<< HEAD
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'
import Button from '../UI/Button/Button'
import './Navbar.css'
import logo from '../../assets/anotaciones-logo.svg'

export default function Navbar() {
  const { usuario, logout, isAuthenticated } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <header className="navbar">
      <Link to="/anotaciones" className="navbar__brand">
        <img src={logo} alt="MS-Anotaciones" />
        <span>
          <strong>MS-Anotaciones</strong>
          <small>Panel de gestión</small>
        </span>
      </Link>

      <div className="navbar__session">
        {isAuthenticated ? (
          <>
            <span>
              {usuario?.usuPNombre ? `${usuario.usuPNombre} ${usuario.usuApePat || ''}`.trim() : 'Sesión activa'}
            </span>
            <Button variant="ghost" type="button" onClick={handleLogout}>
              Salir
            </Button>
          </>
        ) : (
          <Button variant="primary" type="button" onClick={() => navigate('/login')}>
            Ingresar
          </Button>
        )}
      </div>
    </header>
  )
=======
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
>>>>>>> feat/frontend-homepage
}
