// =============================================================
// SERVICIO DE ANOTACIONES — anotacionesService.js
// =============================================================
// Funciones para gestionar anotaciones de alumnos.
// Apunta al MS-Anotaciones (puerto 8082).
//
// =============================================================
import anotacionesHttp from './http/anotacionesHttp'

export const getAnotacionesByAlumno = (alumnoId) => anotacionesHttp.get(`/alumno/${alumnoId}`)
export const getAnotacionById = (id) => anotacionesHttp.get(`/${id}`)
export const crearAnotacion = (data) => anotacionesHttp.post('/', data)
export const actualizarAnotacion = (id, data) => anotacionesHttp.put(`/${id}`, data)
export const eliminarAnotacion = (id) => anotacionesHttp.delete(`/${id}`)
