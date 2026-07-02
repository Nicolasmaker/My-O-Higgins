// =============================================================
// SERVICIO DE AUTENTICACIÓN — authService.js
// =============================================================
// Funciones para el flujo de login/logout y consulta de perfiles.
// Apunta al MS-Autenticacion (puerto 8080).
// =============================================================
import authHttp from './http/authHttp'

// POST /auth/login — retorna token + datos básicos del usuario
export const login = async (data) => {
  const response = await authHttp.post('/auth/login', data)
  const { token, ...usuario } = response.data
  localStorage.setItem('app_token', token)
  localStorage.setItem('app_user', JSON.stringify(usuario))
  return response
}

// Cierra sesión limpiando localStorage
export const logout = () => {
  localStorage.removeItem('app_token')
  localStorage.removeItem('app_user')
}

// Retorna el usuario guardado en localStorage (sin llamada al backend)
export const getUsuarioActual = () => {
  const raw = localStorage.getItem('app_user')
  return raw ? JSON.parse(raw) : null
}

// GET /usuarios/me — perfil completo desde el token JWT activo
export const getMe = () => authHttp.get('/usuarios/me')

// GET /usuarios — listado de todos los usuarios del sistema
export const getUsuarios = () => authHttp.get('/usuarios')

// GET /estudiantes/{rut} — datos completos del estudiante (incluye cursoId)
export const getEstudiante = (rut) => authHttp.get(`/estudiantes/${rut}`)

// GET /funcionarios/{rut} — datos completos del funcionario (docente/inspector/directivo)
export const getFuncionario = (rut) => authHttp.get(`/funcionarios/${rut}`)

// GET /apoderados/{rut} — datos completos del apoderado
export const getApoderado = (rut) => authHttp.get(`/apoderados/${rut}`)
