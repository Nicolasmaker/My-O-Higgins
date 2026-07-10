import anotacionesHttp from './http/anotacionesHttp'

export const getTodasAnotaciones = () => anotacionesHttp.get('')
export const getAnotacionesByHojaVida = (idHojaVida) => anotacionesHttp.get(`/hojavida/${idHojaVida}`)
export const getAnotacionesByRut = (rut) => anotacionesHttp.get(`/estudiante/${rut}`)
export const crearAnotacion = (data) => anotacionesHttp.post('', data)
export const actualizarAnotacion = (id, data) => anotacionesHttp.put(`/${id}`, data)
export const eliminarAnotacion = (id) => anotacionesHttp.delete(`/${id}`)

// Alias para compatibilidad con usos previos del front.
export const getAllAnotaciones = getTodasAnotaciones