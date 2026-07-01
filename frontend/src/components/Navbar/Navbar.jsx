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
}
