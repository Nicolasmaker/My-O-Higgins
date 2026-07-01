// MS-GestionAcademica → /api/academico/asignatura → localhost:8087
import axios from 'axios'

const asignaturaHttp = axios.create({
  baseURL: '/api/academico/asignatura',
  headers: { 'Content-Type': 'application/json' },
})

asignaturaHttp.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('app_token')
    if (token) config.headers.Authorization = `Bearer ${token}`
    return config
  },
  (error) => Promise.reject(error)
)

asignaturaHttp.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('app_token')
      localStorage.removeItem('app_user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default asignaturaHttp