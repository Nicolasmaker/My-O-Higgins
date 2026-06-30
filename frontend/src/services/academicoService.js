// =============================================================
// SERVICIO ACADÉMICO — academicoService.js
// =============================================================
// Funciones para gestión académica (cursos, notas, asignaturas,
// evaluaciones, bitácora, niveles, salas).
// Apunta al MS-GestionAcademica (puerto 8087).
// Vite reescribe /api/academico/* → /* en el proxy.
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

// --- Niveles ---
export const getNiveles = () => academicoHttp.get('/nivel')
export const getNivelById = (id) => academicoHttp.get(`/nivel/${id}`)
export const crearNivel = (data) => academicoHttp.post('/nivel', data)
export const actualizarNivel = (id, data) => academicoHttp.put(`/nivel/${id}`, data)
export const eliminarNivel = (id) => academicoHttp.delete(`/nivel/${id}`)

// --- Salas ---
export const getSalas = () => academicoHttp.get('/sala')
export const getSalaById = (id) => academicoHttp.get(`/sala/${id}`)
export const crearSala = (data) => academicoHttp.post('/sala', data)
export const actualizarSala = (id, data) => academicoHttp.put(`/sala/${id}`, data)
export const eliminarSala = (id) => academicoHttp.delete(`/sala/${id}`)

// --- Evaluaciones ---
export const getEvaluaciones = () => academicoHttp.get('/evaluacion')
export const getEvaluacionById = (id) => academicoHttp.get(`/evaluacion/${id}`)
export const crearEvaluacion = (data) => academicoHttp.post('/evaluacion', data)
export const actualizarEvaluacion = (id, data) => academicoHttp.put(`/evaluacion/${id}`, data)
export const eliminarEvaluacion = (id) => academicoHttp.delete(`/evaluacion/${id}`)

// --- Bitácora de Asignatura ---
export const getBitacorasAsignatura = () => academicoHttp.get('/bitacora-asignatura')
export const getBitacoraAsignaturaById = (id) => academicoHttp.get(`/bitacora-asignatura/${id}`)
export const crearBitacoraAsignatura = (data) => academicoHttp.post('/bitacora-asignatura', data)
export const actualizarBitacoraAsignatura = (id, data) => academicoHttp.put(`/bitacora-asignatura/${id}`, data)
export const eliminarBitacoraAsignatura = (id) => academicoHttp.delete(`/bitacora-asignatura/${id}`)

// --- Notas ---
export const getNotas = () => academicoHttp.get('/notas')
export const getNotaById = (id) => academicoHttp.get(`/notas/${id}`)
export const getNotasByEstudiante = (rut) => academicoHttp.get(`/notas/estudiante/${rut}`)
export const registrarNota = (data) => academicoHttp.post('/notas', data)
export const actualizarNota = (id, data) => academicoHttp.put(`/notas/${id}`, data)
export const eliminarNota = (id) => academicoHttp.delete(`/notas/${id}`)
