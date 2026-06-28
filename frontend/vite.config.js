import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// ==============================================================================================================
// CONFIGURACIÓN VITE — vite.config.js ESTO SOLO APLICA PARA EL DESARROLLO PQ SE USARA AWS API GATEWAY PARA ESTO
// ==============================================================================================================
// Este archivo actúa como un intermediario (Proxy) en entorno de desarrollo.
// Redirige las peticiones del frontend (puerto 5173) hacia los puertos locales
// de cada microservicio backend (8080-8089) según la ruta de la API.
// Esto centraliza las llamadas y evita por completo un problema de una wea llamada cors, es algo relacionado con seguridad.

// 1. Mapea rutas específicas de '/api/...' a sus respectivos puertos locales.
// 2. Modifica dinámicamente las URLs (mediante rewrite) para aquellos servicios 
//    que no exigen el prefijo '/api' o que usan subrutas personalizadas.
// =============================================================
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      //MS-Autenticacion (8080)
      '/api/auth': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/auth/, '') 
      },
      // MS-GestionReuniones (8081) 
      '/api/reuniones': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      },
      // MS-Anotaciones (8082)
      '/api/anotaciones': {
        target: 'http://localhost:8082',
        changeOrigin: true,
      },
      // MS-HojaDeVida (8084)
      '/api/hoja-vida': {
        target: 'http://localhost:8084',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/hoja-vida/, '/api')
      },
      // MS-CalendarioEscolar (8085)
      '/api/calendarios': {
        target: 'http://localhost:8085',
        changeOrigin: true,
      },
      // MS-GestionMatricula (8086)
      '/api/matriculas': {
        target: 'http://localhost:8086',
        changeOrigin: true,
      },
      // MS-GestionAcademica (8087)
      '/api/academico': {
        target: 'http://localhost:8087',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/academico/, '')
      },
      // MS-Mensajeria (8089)
      '/api/mensajeria': {
        target: 'http://localhost:8089',
        changeOrigin: true,
      },
    },
  },
})

