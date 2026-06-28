// MS-CalendarioEscolar → /api/calendario → localhost:8085
import axios from 'axios'

const calendarioHttp = axios.create({
  baseURL: '/api/calendarios',
  headers: { 'Content-Type': 'application/json' },
})

calendarioHttp.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('app_token')
    if (token) config.headers.Authorization = `Bearer ${token}`
    return config
  },
  (error) => Promise.reject(error)
)

calendarioHttp.interceptors.response.use(
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

export default calendarioHttp
