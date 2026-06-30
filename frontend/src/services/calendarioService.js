// =============================================================
// SERVICIO DE CALENDARIO ESCOLAR — calendarioService.js
// =============================================================
// Funciones para consultar el calendario escolar.
// Apunta al MS-CalendarioEscolar (puerto 8085).
//
// =============================================================
import calendarioHttp from './http/calendarioHttp'

export const getEventos = () => calendarioHttp.get('')
export const getEventoById = (id) => calendarioHttp.get(`/${id}`)
export const crearEvento = (data) => calendarioHttp.post('', data)
export const actualizarEvento = (id, data) => calendarioHttp.put(`/${id}`, data)
export const eliminarEvento = (id) => calendarioHttp.delete(`/${id}`)
