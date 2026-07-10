// =============================================================
// RUTA PROTEGIDA — ProtectedRoute.jsx
// =============================================================
// Envuelve páginas que requieren sesión. Si no hay usuario
// autenticado redirige a /login, guardando la ruta original en
// state para poder volver después del login si se quisiera.
// Si se pasa `roles`, además exige que el usuario tenga alguno
// de esos roles — si no, redirige a "/".
// =============================================================
import { Navigate, useLocation } from 'react-router-dom'
import PropTypes from 'prop-types'
import { useAuth } from '../hooks/useAuth'

export default function ProtectedRoute({ children, roles }) {
  const { isAuthenticated, hasRole } = useAuth()
  const location = useLocation()

  if (!isAuthenticated) {
    return <Navigate to="/login" replace state={{ from: location.pathname }} />
  }

  if (roles && !hasRole(roles)) {
    return <Navigate to="/" replace />
  }

  return children
}

ProtectedRoute.propTypes = {
  children: PropTypes.node.isRequired,
  roles: PropTypes.arrayOf(PropTypes.string),
}
