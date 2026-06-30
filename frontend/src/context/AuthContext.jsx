// =============================================================
// CONTEXTO DE AUTENTICACIÓN — AuthContext.jsx
// =============================================================
// Guarda globalmente: ¿quién está logueado? ¿cuál es su token?
// ¿cuál es su rol?
//
// Cualquier componente de la app puede leer estos datos sin que
// nadie se los pase por props. Lo toman directamente de acá.
//
// Qué expone:
//   - token             → el JWT que se envía en cada petición al backend
//   - usuario           → datos del usuario logueado (rut, nombre, rol, etc.)
//   - login(t, u)       → guarda la sesión en estado + localStorage
//   - logout()          → limpia estado + localStorage
//   - isAuthenticated   → true si hay token Y usuario al mismo tiempo
//   - hasRole(rol)      → verifica si el usuario tiene un rol específico
//                       acepta string ('ADMIN') o array (['ADMIN', 'DOCENTE'])
//
// El estado se inicializa leyendo localStorage al arrancar:
// si el usuario recarga la página, la sesión se recupera sola.
// =============================================================
import { createContext, useState, useCallback } from 'react'
import PropTypes from 'prop-types'

export const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  // Lee localStorage solo una vez al montar (lazy initializer)
  const [token, setToken] = useState(() => localStorage.getItem('app_token')) //Qué hace (LINEA 29): Cuando la aplicación se abre o el usuario recarga la página (F5), 
  const [usuario, setUsuario] = useState(() => {                              //React va al disco duro del navegador (localStorage) a buscar si ya había una sesión guardada.
    const stored = localStorage.getItem('app_user')                           //Si la encuentra, la restaura automáticamente. Así el usuario no tiene que iniciar sesión cada vez que refresca la pantalla.
    return stored ? JSON.parse(stored) : null
  })

  //Este metodo lo que hace es guardar el token y los datos del usuario en el almacenamiento local del navegador (LocalStorage)
  //Y en la memoria de react (useState) recuerden q esto seria un hook de react

  const login = useCallback((newToken, userData) => { //useCallback evita recrear estas funciones en cada reinicio de la pagina (F5),
    localStorage.setItem('app_token', newToken)       //lo que causaría re-renders innecesarios en toda la app.
    localStorage.setItem('app_user', JSON.stringify(userData))
    setToken(newToken)
    setUsuario(userData)
  }, [])

  //Este culiao hace lo contrario, borra todo xd por algo se llama logout
  const logout = useCallback(() => {
    localStorage.removeItem('app_token')
    localStorage.removeItem('app_user')
    setToken(null)
    setUsuario(null)
  }, [])

  // Se considera autenticado solo si existen AMBOS: token y usuario
  const isAuthenticated = !!token && !!usuario

  //Acepta string ('ADMIN') o array (['ADMIN', 'DOCENTE'])
  //Este funcioin es para preguntar roles del usuario o dar ordenes a x usuario
  //Por ejemplo se usaria para ocultar o mostrar botones dependiendo del rol. 
  const hasRole = useCallback((rol) => {
    if (!usuario) return false
    if (Array.isArray(rol)) return rol.includes(usuario.rolNombre)
    return usuario.rolNombre === rol
  }, [usuario])


  //El "Provider" (Proveedor) es como una caja, adentro irian los componentes que creemos y podran tener acceso a la informacion de este archivo.
  //Todo lo que pongas en "value" estará disponible para cualquier componente de la app. (los metodos de este archivo)
  return (
    <AuthContext.Provider value={{ token, usuario, login, logout, isAuthenticated, hasRole }}>
      {children} 
    </AuthContext.Provider>
  )
  //children representa a todos los componentes hijos que queden envueltos dentro de esta burbuja (como el menú, las páginas, los botones, etc.)*
}

//Esto es para que React nos obligue a pasarle componentes al Authprovider para evitar errores 
AuthProvider.propTypes = {
  children: PropTypes.node.isRequired,
}
