// =============================================================
// INSTANCIA AXIOS — authHttp.js
// =============================================================
// Instancia de Axios configurada para el MS de Autenticación.
// Vite redirige /api/auth/* → localhost:8080 (ver vite.config.js).

// QUE HACE AXIOS
//Hace las peticiones HTTP al backend (GET, POST, PUT, DELETE). Reemplaza al fetch nativo con más opciones
// =============================================================
import axios from 'axios'

const authHttp = axios.create({
  baseURL: '/api/auth',
  headers: { 'Content-Type': 'application/json' },
})

//adjunta el token JWT en cada petición que se realice al backend
authHttp.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('app_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

//si el back responde 401 (token vencido, inválido, o el usuario ya no existe), limpia la
//sesión y manda al usuario a Home (no a /login: si el 401 ocurrió por una sesión restaurada
//de localStorage que ya no es válida, no tiene sentido forzar la pantalla de login).
authHttp.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('app_token')
      localStorage.removeItem('app_user')
      window.location.href = '/'
    }
    return Promise.reject(error)
  }
)

export default authHttp
