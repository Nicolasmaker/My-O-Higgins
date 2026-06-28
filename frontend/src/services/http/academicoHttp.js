// MS-GestionAcademica → /api/academico → localhost:8087
import axios from 'axios'

const academicoHttp = axios.create({
  baseURL: '/api/academico',
  headers: { 'Content-Type': 'application/json' },
})

academicoHttp.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('app_token')
    if (token) config.headers.Authorization = `Bearer ${token}`
    return config
  },
  (error) => Promise.reject(error)
)

academicoHttp.interceptors.response.use(
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

export default academicoHttp
