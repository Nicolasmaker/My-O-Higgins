// =============================================================
// SERVICIO DE AUTENTICACIÓN — authService.js
// =============================================================
// Funciones para el flujo de login y registro.
// Cada función llama a un endpoint del MS-Autenticacion (puerto 8080).
//
// Uso desde cualquier página:
//   import { login, register } from '../services/authService'
//   const res = await login({ email, password })
// =============================================================
import authHttp from './http/authHttp'

// POST /api/auth/auth/login — inicia sesión, retorna el JWT
export const login = (data) => authHttp.post('/auth/login', data)

// POST /api/auth/usuarios — registra un nuevo usuario (depende del MS)
export const register = (data) => authHttp.post('/usuarios', data)
