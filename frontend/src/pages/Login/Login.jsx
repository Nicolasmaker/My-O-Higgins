import { useForm } from 'react-hook-form'
import { toast } from 'react-toastify'
import { login } from '../../services/authService'
import { useAuth } from '../../hooks/useAuth'

export default function Login() {
  const { register, handleSubmit } = useForm()
  const { login: loginContext } = useAuth()

  const onSubmit = async (data) => {
    try {
      const response = await login(data)
      const { token, ...usuario } = response.data
      loginContext(token, usuario)
      
      toast.success('Me conecte con la caga de backend')
      console.log('Respuesta del backend:', response.data)
      
    } catch (error) {
      console.error('Error de conexión:', error)
      toast.error(error.response?.data?.message || 'Error al conectar con el backend')
    }
  }

  return (
    <div style={{ padding: '2rem', maxWidth: '400px', margin: '0 auto' }}>
      <h2>Login de Prueba</h2>
      <p>Verificando conexión al MS-Autenticacion (Puerto 8080)</p>
      
      <form onSubmit={handleSubmit(onSubmit)} style={{ display: 'flex', flexDirection: 'column', gap: '1rem', marginTop: '1rem' }}>
        
        {/* Cambia "email" a "rut" si tu backend pide RUT para el login */}
        <div>
          <label>Email culiao:</label><br />
          <input 
            type="text" 
            {...register('email')} // Cambiar a 'rut' si es necesario
            style={{ padding: '0.5rem', width: '100%' }}
          />
        </div>

        <div>
          <label>Contraseña:</label><br />
          <input 
            type="password" 
            {...register('password')} 
            style={{ padding: '0.5rem', width: '100%' }}
          />
        </div>

        <button type="submit" style={{ padding: '0.75rem', cursor: 'pointer' }}>
          Probar la wea
        </button>
      </form>
    </div>
  )
}
