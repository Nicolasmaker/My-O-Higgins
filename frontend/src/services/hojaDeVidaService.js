// =============================================================
// SERVICIO DE HOJA DE VIDA — hojaDeVidaService.js
// =============================================================
// Funciones para gestionar la hoja de vida de alumnos.
// Apunta al MS-HojaDeVida (puerto 8084).
// Vite reescribe /api/hoja-vida/* → /api/* en el proxy.
//
// =============================================================
import hojaDeVidaHttp from './http/hojaDeVidaHttp'

export const getTodasHojasDeVida = () => hojaDeVidaHttp.get('/hojas-vida')
export const getHojaDeVida = (idHojaVida) => hojaDeVidaHttp.get(`/hojas-vida/${idHojaVida}`)
export const crearHojaDeVida = (data) => hojaDeVidaHttp.post('/hojas-vida', data)
export const actualizarHojaDeVida = (idHojaVida, data) => hojaDeVidaHttp.put(`/hojas-vida/${idHojaVida}`, data)
export const eliminarHojaDeVida = (idHojaVida) => hojaDeVidaHttp.delete(`/hojas-vida/${idHojaVida}`)
