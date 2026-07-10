// MS-Mensajeria → /api/mensajeria → localhost:8089
import axios from 'axios'

const mensajeriaHttp = axios.create({
  baseURL: '/api/mensajeria',
  headers: { 'Content-Type': 'application/json' },
})

mensajeriaHttp.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('app_token')
    if (token) config.headers.Authorization = `Bearer ${token}`
    return config
  },
  (error) => Promise.reject(error)
)

mensajeriaHttp.interceptors.response.use(
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

export default mensajeriaHttp
