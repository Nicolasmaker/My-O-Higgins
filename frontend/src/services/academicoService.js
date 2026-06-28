// =============================================================
// SERVICIO ACADÉMICO — academicoService.js
// =============================================================
// Funciones para gestión académica (cursos, notas, asignaturas).
// Apunta al MS-GestionAcademica (puerto 8087).
//
// =============================================================
import academicoHttp from './http/academicoHttp'

export const getCursos = () => academicoHttp.get('/cursos')
export const getCursoById = (id) => academicoHttp.get(`/cursos/${id}`)
export const getAsignaturas = () => academicoHttp.get('/asignaturas')
export const getNotasByAlumno = (alumnoId) => academicoHttp.get(`/notas/alumno/${alumnoId}`)
export const registrarNota = (data) => academicoHttp.post('/notas', data)
export const actualizarNota = (id, data) => academicoHttp.put(`/notas/${id}`, data)
