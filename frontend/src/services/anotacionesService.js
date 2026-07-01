import anotacionesHttp from './http/anotacionesHttp'

// CORRECCIÓN: Se cambió '/' por '' (string vacío) en el GET y POST principales.
export const getAllAnotaciones = () => anotacionesHttp.get('')
export const getAnotacionesByHojaVida = (idHojaVida) => anotacionesHttp.get(`/hojavida/${idHojaVida}`)
export const crearAnotacion = (data) => anotacionesHttp.post('', data)
export const actualizarAnotacion = (id, data) => anotacionesHttp.put(`/${id}`, data)
export const eliminarAnotacion = (id) => anotacionesHttp.delete(`/${id}`)