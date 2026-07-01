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
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { ToastContainer } from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css'
import { AuthProvider } from './context/AuthContext'

<<<<<<< HEAD
import MainLayout from './components/Layout/MainLayout'
import Anotaciones from './pages/Anotaciones/Anotaciones'
import Login from './pages/Login/Login'
=======
// ── Páginas (se importan a medida que se crean) ──────────────
import Home        from './pages/Home/Home'
import Login       from './pages/Login/Login'
// import Registro    from './pages/Registro/Registro'
>>>>>>> feat/frontend-homepage

// ── Layout con Navbar + Footer ────────────────────────────────
// function Layout({ children }) {
//   return (
//     <>
//       <Navbar />
//       {children}
//       <Footer />
//     </>
//   )
// }

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
<<<<<<< HEAD
          <Route element={<MainLayout />}>
            <Route index element={<Navigate to="/anotaciones" replace />} />
            <Route path="anotaciones" element={<Anotaciones />} />
          </Route>
=======
          {/* Home - Landing Page Principal */}
          <Route path="/" element={<Home />} />
          
>>>>>>> feat/frontend-homepage
          <Route path="/login" element={<Login />} />

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

