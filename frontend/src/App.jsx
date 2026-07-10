// =============================================================
// ENRUTADOR PRINCIPAL — App.jsx
// =============================================================
// Punto central de la app. Define:
//
//   - AuthProvider: envuelve TODA la app para que cualquier
//     componente pueda saber quién está logueado.
//
//   - BrowserRouter + Routes: el "mapa" de páginas. Cada <Route>
//     asocia una URL con una página (componente).
//
//   - Layout / PublicLayout: añaden Navbar y Footer a las páginas
//     que lo necesitan. Login y Registro van sin ellos.
//
//   - ToastContainer: el sistema de notificaciones global.
//     Se define UNA sola vez aquí y cualquier parte de la app
//     puede disparar un toast sin configurar nada más.
//
// Las páginas se van importando acá a medida que se desarrollan.
// =============================================================
import { BrowserRouter, Routes, Route, Navigate, useLocation } from 'react-router-dom'
import { ToastContainer } from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css'
import { useEffect } from 'react'
import { AuthProvider } from './context/AuthContext'
import ProtectedRoute from './components/ProtectedRoute'
import MainLayout from './components/Layout/MainLayout'
import { ACCESO_RUTA } from './constants/roles'

// ── Páginas (se importan a medida que se crean) ──────────────
import Home        from './pages/Home/Home'
import Login       from './pages/Login/Login'
import Anotaciones from './pages/Anotaciones/Anotaciones'
import Calendario  from './pages/Calendario/Calendario'
import Reuniones   from './pages/Reuniones/Reuniones'
import Mensajeria  from './pages/Mensajeria/Mensajeria'
import Matricula   from './pages/Matricula/Matricula'
import HojaDeVida  from './pages/HojaDeVida/HojaDeVida'
import Academico   from './pages/Academico/Academico'
import Funcionarios from './pages/Funcionarios/Funcionarios'
// import Registro    from './pages/Registro/Registro'

function ScrollToTop() {
  const { pathname } = useLocation()

  useEffect(() => {
    window.scrollTo({ top: 0, left: 0, behavior: 'auto' })
  }, [pathname])

  return null
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <ScrollToTop />
        <Routes>
          <Route path="/login" element={<Login />} />

          {/* Rutas con Navbar + Footer (MainLayout) */}
          <Route element={<MainLayout />}>
            {/* Home - Landing Page Principal */}
            <Route path="/" element={<Home />} />
            <Route path="/anotaciones" element={<ProtectedRoute><Anotaciones /></ProtectedRoute>} />
            <Route path="/calendario" element={<ProtectedRoute><Calendario /></ProtectedRoute>} />
            <Route path="/reuniones" element={<ProtectedRoute><Reuniones /></ProtectedRoute>} />
            <Route path="/mensajeria" element={<ProtectedRoute><Mensajeria /></ProtectedRoute>} />
            <Route path="/matriculas" element={<ProtectedRoute roles={ACCESO_RUTA['/matriculas']}><Matricula /></ProtectedRoute>} />
            <Route path="/hoja-de-vida" element={<ProtectedRoute><HojaDeVida /></ProtectedRoute>} />
            <Route path="/academico" element={<ProtectedRoute roles={ACCESO_RUTA['/academico']}><Academico /></ProtectedRoute>} />
            <Route path="/funcionarios" element={<ProtectedRoute roles={ACCESO_RUTA['/funcionarios']}><Funcionarios /></ProtectedRoute>} />
            <Route path="/inicio" element={<Navigate to="/" replace />} />
          </Route>

          {/* Catch-all: cualquier ruta no definida redirige a "/" */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>

        {/* Notificaciones globales — posición arriba a la derecha */}
        <ToastContainer
          position="top-right"
          autoClose={4000}
          hideProgressBar={false}
          closeOnClick
          pauseOnHover
          draggable
          theme="light"
        />
      </BrowserRouter>
    </AuthProvider>
  )
}

