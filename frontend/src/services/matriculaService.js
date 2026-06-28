// =============================================================
// SERVICIO DE MATRÍCULA — matriculaService.js
// =============================================================
// Funciones para gestión de matrículas.
// Apunta al MS-GestionMatricula (puerto 8086).
//
// =============================================================
import matriculaHttp from './http/matriculaHttp'

export const getMatriculasByAlumno = (alumnoId) => matriculaHttp.get(`/alumno/${alumnoId}`)
export const getMatriculaById = (id) => matriculaHttp.get(`/${id}`)
export const crearMatricula = (data) => matriculaHttp.post('/', data)
export const actualizarMatricula = (id, data) => matriculaHttp.put(`/${id}`, data)
export const eliminarMatricula = (id) => matriculaHttp.delete(`/${id}`)
