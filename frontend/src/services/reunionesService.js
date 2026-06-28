// =============================================================
// SERVICIO DE REUNIONES — reunionesService.js
// =============================================================
// Funciones para gestionar reuniones.
// Apunta al MS-GestionReuniones (puerto 8081).
//
// =============================================================
import reunionesHttp from './http/reunionesHttp'

export const getReunionesByUsuario = (usuarioId) => reunionesHttp.get(`/usuario/${usuarioId}`)
export const getReunionById = (id) => reunionesHttp.get(`/${id}`)
export const crearReunion = (data) => reunionesHttp.post('/', data)
export const actualizarReunion = (id, data) => reunionesHttp.put(`/${id}`, data)
export const eliminarReunion = (id) => reunionesHttp.delete(`/${id}`)
