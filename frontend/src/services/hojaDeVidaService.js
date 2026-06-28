// =============================================================
// SERVICIO DE HOJA DE VIDA — hojaDeVidaService.js
// =============================================================
// Funciones para gestionar la hoja de vida de docentes/alumnos.
// Apunta al MS-HojaDeVida (puerto 8084).
//
// =============================================================
import hojaDeVidaHttp from './http/hojaDeVidaHttp'

export const getHojaDeVida = (usuarioId) => hojaDeVidaHttp.get(`/${usuarioId}`)
export const actualizarHojaDeVida = (usuarioId, data) => hojaDeVidaHttp.put(`/${usuarioId}`, data)
