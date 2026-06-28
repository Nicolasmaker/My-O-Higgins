// =============================================================
// HOOK useAuth — useAuth.js
// =============================================================
// Punto de acceso al contexto de autenticación.
//
// En vez de escribir esto en cada componente que lo necesite:
//   const context = useContext(AuthContext)
//
// Simplemente escribes:
//   const { usuario, logout, hasRole } = useAuth()
//
// Ventaja extra: si alguien llama useAuth() fuera del AuthProvider,
// lanza un error descriptivo en vez de un crash silencioso.
//
// Devuelve: { token, usuario, login, logout, isAuthenticated, hasRole }
// =============================================================
import { useContext } from 'react'
import { AuthContext } from '../context/AuthContext'

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth debe usarse dentro de <AuthProvider>')
  }
  return context
}
