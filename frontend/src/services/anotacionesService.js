<<<<<<< HEAD
import anotacionesHttp from './http/anotacionesHttp'

// CORRECCIÓN: Se cambió '/' por '' (string vacío) en el GET y POST principales.
export const getAllAnotaciones = () => anotacionesHttp.get('')
=======
// =============================================================
// SERVICIO DE ANOTACIONES — anotacionesService.js
// =============================================================
// Funciones para gestionar anotaciones de alumnos.
// Apunta al MS-Anotaciones (puerto 8083).
//
// =============================================================
import anotacionesHttp from './http/anotacionesHttp'

export const getTodasAnotaciones = () => anotacionesHttp.get('')
>>>>>>> feat/frontend-homepage
export const getAnotacionesByHojaVida = (idHojaVida) => anotacionesHttp.get(`/hojavida/${idHojaVida}`)
export const crearAnotacion = (data) => anotacionesHttp.post('', data)
export const actualizarAnotacion = (id, data) => anotacionesHttp.put(`/${id}`, data)
export const eliminarAnotacion = (id) => anotacionesHttp.delete(`/${id}`)