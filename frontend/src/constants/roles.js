// =============================================================
// ACCESO POR RUTA — roles.js
// =============================================================
// Mapa único de qué roles pueden ver cada sección. Usado tanto
// por Navbar (para no mostrar el link) como por ProtectedRoute
// (para bloquear la ruta si alguien la escribe a mano).
// Rutas que no aparecen acá quedan abiertas a cualquier usuario
// autenticado (ej. Anotaciones, Calendario, Reuniones, etc.).
// =============================================================
export const ACCESO_RUTA = {
  '/matriculas': ['ROLE_DIRECTIVO', 'ROLE_APODERADO'],
  '/academico': ['ROLE_ESTUDIANTE', 'ROLE_APODERADO', 'ROLE_DOCENTE', 'ROLE_DIRECTIVO'],
  '/funcionarios': ['ROLE_DIRECTIVO'],
}
