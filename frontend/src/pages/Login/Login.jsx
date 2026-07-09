import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { toast } from 'react-toastify'
import { login } from '../../services/authService'
import { useAuth } from '../../hooks/useAuth'
import Button from '../../components/UI/Button/Button'
import Input from '../../components/UI/Input/Input'
import { emailRules, passwordRules } from '../../validators/fieldValidators'
import colegioFachada from '../../assets/colegioFachada.webp'
import '../../styles/login.css'

export default function Login() {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm()
  const { login: loginContext } = useAuth()

  const [showRecovery, setShowRecovery] = useState(false)
  const [recoverySent, setRecoverySent] = useState(false)
  const {
    register: registerRecovery,
    handleSubmit: handleRecoverySubmit,
    formState: { errors: recoveryErrors },
    reset: resetRecoveryForm,
  } = useForm()

  const onSubmit = async (data) => {
    try {
      const response = await login(data)
      const token = response.data?.token || response.data?.jwt || response.data?.accessToken
      const usuario = response.data?.usuario || response.data?.user || response.data?.data || {
        usuRut: response.data?.usuRut,
        usuPNombre: response.data?.usuPNombre,
        usuApePat: response.data?.usuApePat,
        usuEmail: response.data?.usuEmail,
        rolNombre: response.data?.rolNombre || response.data?.rol || response.data?.role,
      }

      if (!token) {
        throw new Error('El backend no devolvió token')
      }

      loginContext(token, usuario || { rolNombre: 'ROLE_DOCENTE' })
      toast.success('Sesión iniciada')
      // Tras login, ir al inicio (Home muestra el Mural Digital cuando hay sesión).
      window.location.href = '/'
    } catch (error) {
      console.error(error)
      toast.error(error.response?.data?.message || error.message || 'No se pudo iniciar sesión')
    }
  }

  // Recuperación de contraseña: solo maqueta de front, sin llamada al backend
  const onRecoverySubmit = (data) => {
    setRecoverySent(true)
    toast.success(`Si el correo ${data.recoveryEmail} está registrado, te enviaremos un enlace de recuperación`)
  }

  const toggleRecovery = () => {
    setShowRecovery((v) => !v)
    setRecoverySent(false)
    resetRecoveryForm()
  }

  return (
    <main className="login-page">
      <div className="login-bg" style={{ backgroundImage: `url(${colegioFachada})` }} />
      <div className="login-overlay" />
      <section className="login-card">
        <p className="login-eyebrow">Inicio de sesion</p>
        <h1>Ingreso a My Ohiggins</h1>
        <p className="login-subtitle">
          Ingresa con tu correo institucional para acceder al portal digital.
        </p>

        {!showRecovery ? (
          <form onSubmit={handleSubmit(onSubmit)} className="login-form">
            <Input
              label="Correo electrónico"
              type="email"
              placeholder="nombre@myohiggins.cl"
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

            <button type="button" className="login-forgot-link" onClick={toggleRecovery}>
              ¿Olvidaste tu contraseña?
            </button>

            <Button type="submit">Entrar</Button>
          </form>
        ) : (
          <div className="login-recovery">
            {recoverySent ? (
              <p className="login-recovery__success">
                Revisa tu correo: si la dirección está registrada, llegará un enlace para restablecer tu contraseña.
              </p>
            ) : (
              <form onSubmit={handleRecoverySubmit(onRecoverySubmit)} className="login-form">
                <Input
                  label="Correo electrónico"
                  type="email"
                  placeholder="nombre@myohiggins.cl"
                  error={recoveryErrors.recoveryEmail?.message}
                  {...registerRecovery('recoveryEmail', emailRules)}
                />
                <Button type="submit">Enviar enlace de recuperación</Button>
              </form>
            )}

            <button type="button" className="login-forgot-link" onClick={toggleRecovery}>
              Volver a iniciar sesión
            </button>
          </div>
        )}
      </section>
    </main>
  )
}
