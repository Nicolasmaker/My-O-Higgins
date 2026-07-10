// MS-GestionReuniones → /api/reuniones → localhost:8081
import axios from 'axios'

const reunionesHttp = axios.create({
  baseURL: '/api/reuniones',
  headers: { 'Content-Type': 'application/json' },
})

reunionesHttp.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('app_token')
    if (token) config.headers.Authorization = `Bearer ${token}`
    return config
  },
  (error) => Promise.reject(error)
)

reunionesHttp.interceptors.response.use(
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

export default reunionesHttp
