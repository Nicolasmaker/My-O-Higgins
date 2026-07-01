import { useForm } from 'react-hook-form'
import { toast } from 'react-toastify'
import { login } from '../../services/authService'
import { useAuth } from '../../hooks/useAuth'
import Button from '../../components/UI/Button/Button'
import Input from '../../components/UI/Input/Input'
import { emailRules, passwordRules } from '../../validators/fieldValidators'
import './login.css'

export default function Login() {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm()
  const { login: loginContext } = useAuth()

  const onSubmit = async (data) => {
    try {
      const response = await login(data)
<<<<<<< HEAD
      const token = response.data?.token || response.data?.jwt || response.data?.accessToken
      const usuario = response.data?.usuario || response.data?.user || response.data?.data || null

      if (!token) {
        throw new Error('El backend no devolvió token')
      }

      loginContext(token, usuario || { rol: 'DOCENTE' })
      toast.success('Sesión iniciada')
      window.location.href = '/anotaciones'
=======
      const { token, ...usuario } = response.data
      loginContext(token, usuario)
      
      toast.success('Me conecte con la caga de backend')
      console.log('Respuesta del backend:', response.data)
      
>>>>>>> feat/frontend-homepage
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || error.message || 'No se pudo iniciar sesión')
    }
  }

  return (
    <main className="login-page">
      <section className="login-card">
        <p className="eyebrow">MS-Autenticación</p>
        <h1>Ingresar al panel</h1>
        <p>
          Usa tus credenciales para guardar el token en el contexto y entrar al módulo de anotaciones.
        </p>

        <form onSubmit={handleSubmit(onSubmit)} className="login-form">
          <Input
            label="Usuario o RUT"
            type="text"
            placeholder="rut o usuario"
            error={errors.email?.message}
            {...register('email', emailRules)}
          />

          <Input
            label="Contraseña"
            type="password"
            placeholder="••••••••"
            error={errors.password?.message}
            {...register('password', passwordRules)}
          />

          <Button type="submit">Entrar</Button>
        </form>
      </section>
    </main>
  )
}
