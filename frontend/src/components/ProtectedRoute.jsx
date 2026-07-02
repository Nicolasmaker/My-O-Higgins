// =============================================================
// RUTA PROTEGIDA — ProtectedRoute.jsx
// =============================================================
// Envuelve páginas que requieren sesión. Si no hay usuario
// autenticado redirige a /login, guardando la ruta original en
// state para poder volver después del login si se quisiera.
// =============================================================
import { Navigate, useLocation } from 'react-router-dom'
import PropTypes from 'prop-types'
import { useAuth } from '../hooks/useAuth'

export default function ProtectedRoute({ children }) {
  const { isAuthenticated } = useAuth()
  const location = useLocation()

  if (!isAuthenticated) {
    return <Navigate to="/login" replace state={{ from: location.pathname }} />
  }

  return children
}

ProtectedRoute.propTypes = {
  children: PropTypes.node.isRequired,
}
