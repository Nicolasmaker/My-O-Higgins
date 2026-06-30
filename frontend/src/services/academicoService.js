// =============================================================
// SERVICIO ACADÉMICO — academicoService.js
// =============================================================
// Funciones para gestión académica (cursos, notas, asignaturas).
// Apunta al MS-GestionAcademica (puerto 8087).
//
// =============================================================
import academicoHttp from './http/academicoHttp'

// --- Cursos ---
export const getCursos = () => academicoHttp.get('/curso')
export const getCursoById = (id) => academicoHttp.get(`/curso/${id}`)
export const crearCurso = (data) => academicoHttp.post('/curso', data)
export const actualizarCurso = (id, data) => academicoHttp.put(`/curso/${id}`, data)
export const eliminarCurso = (id) => academicoHttp.delete(`/curso/${id}`)

// --- Asignaturas ---
export const getAsignaturas = () => academicoHttp.get('/asignatura')
export const getAsignaturaById = (id) => academicoHttp.get(`/asignatura/${id}`)
export const crearAsignatura = (data) => academicoHttp.post('/asignatura', data)
export const actualizarAsignatura = (id, data) => academicoHttp.put(`/asignatura/${id}`, data)
export const eliminarAsignatura = (id) => academicoHttp.delete(`/asignatura/${id}`)

// --- Notas ---
export const getNotas = () => academicoHttp.get('/notas')
export const getNotaById = (id) => academicoHttp.get(`/notas/${id}`)
export const getNotasByEstudiante = (rut) => academicoHttp.get(`/notas/estudiante/${rut}`)
export const registrarNota = (data) => academicoHttp.post('/notas', data)
export const actualizarNota = (id, data) => academicoHttp.put(`/notas/${id}`, data)
export const eliminarNota = (id) => academicoHttp.delete(`/notas/${id}`)
