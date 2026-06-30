// =============================================================
// SERVICIO DE ANOTACIONES — anotacionesService.js
// =============================================================
// Funciones para gestionar anotaciones de alumnos.
// Apunta al MS-Anotaciones (puerto 8083).
//
// =============================================================
import anotacionesHttp from './http/anotacionesHttp'

export const getTodasAnotaciones = () => anotacionesHttp.get('')
export const getAnotacionesByHojaVida = (idHojaVida) => anotacionesHttp.get(`/hojavida/${idHojaVida}`)
export const crearAnotacion = (data) => anotacionesHttp.post('', data)
export const actualizarAnotacion = (id, data) => anotacionesHttp.put(`/${id}`, data)
export const eliminarAnotacion = (id) => anotacionesHttp.delete(`/${id}`)
