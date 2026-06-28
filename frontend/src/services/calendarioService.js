// =============================================================
// SERVICIO DE CALENDARIO ESCOLAR — calendarioService.js
// =============================================================
// Funciones para consultar el calendario escolar.
// Apunta al MS-CalendarioEscolar (puerto 8085).
//
// =============================================================
import calendarioHttp from './http/calendarioHttp'

export const getEventos = () => calendarioHttp.get('/eventos')
export const getEventoById = (id) => calendarioHttp.get(`/eventos/${id}`)
export const crearEvento = (data) => calendarioHttp.post('/eventos', data)
export const actualizarEvento = (id, data) => calendarioHttp.put(`/eventos/${id}`, data)
export const eliminarEvento = (id) => calendarioHttp.delete(`/eventos/${id}`)
