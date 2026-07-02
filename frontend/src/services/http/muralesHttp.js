// MS-CalendarioEscolar (Mural Digital) → /api/murales → localhost:8085
import axios from 'axios'

const muralesHttp = axios.create({
  baseURL: '/api/murales',
  headers: { 'Content-Type': 'application/json' },
})

muralesHttp.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('app_token')
    if (token) config.headers.Authorization = `Bearer ${token}`
    return config
  },
  (error) => Promise.reject(error)
)

muralesHttp.interceptors.response.use(
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

export default muralesHttp
