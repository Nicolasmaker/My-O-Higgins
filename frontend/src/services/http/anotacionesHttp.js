// MS-Anotaciones → /api/anotaciones → localhost:8083
import axios from 'axios'

const anotacionesHttp = axios.create({
  baseURL: '/api/anotaciones',
  headers: { 'Content-Type': 'application/json' },
})

anotacionesHttp.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('app_token')
    if (token) config.headers.Authorization = `Bearer ${token}`
    return config
  },
  (error) => Promise.reject(error)
)

anotacionesHttp.interceptors.response.use(
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

export default anotacionesHttp
