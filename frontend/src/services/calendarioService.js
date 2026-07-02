// =============================================================
// SERVICIO DE CALENDARIO ESCOLAR — calendarioService.js
// =============================================================
// Funciones para consultar el calendario escolar.
// Apunta al MS-CalendarioEscolar (puerto 8085).
//
// =============================================================
import calendarioHttp from './http/calendarioHttp'
import muralesHttp from './http/muralesHttp'

export const getEventos = () => calendarioHttp.get('')
export const getEventoById = (id) => calendarioHttp.get(`/${id}`)
export const crearEvento = (data) => calendarioHttp.post('', data)
export const actualizarEvento = (id, data) => calendarioHttp.put(`/${id}`, data)
export const eliminarEvento = (id) => calendarioHttp.delete(`/${id}`)

// --- Mural Digital (mismo MS, puerto 8085) ---
export const getMurales = () => muralesHttp.get('')
export const getMuralById = (id) => muralesHttp.get(`/${id}`)
export const crearMural = (data) => muralesHttp.post('', data)
export const actualizarMural = (id, data) => muralesHttp.put(`/${id}`, data)
export const eliminarMural = (id) => muralesHttp.delete(`/${id}`)
