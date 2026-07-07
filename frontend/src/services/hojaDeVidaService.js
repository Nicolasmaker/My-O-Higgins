// =============================================================
// SERVICIO DE HOJA DE VIDA — hojaDeVidaService.js
// =============================================================
// Funciones para gestionar la hoja de vida de alumnos
// y sus antecedentes (académicos, apoderado, médicos).
// Apunta al MS-HojaDeVida (puerto 8084).
// Vite reescribe /api/hoja-vida/* → /api/* en el proxy.
// =============================================================
import hojaDeVidaHttp from './http/hojaDeVidaHttp'

// --- Hoja de Vida ---
export const getTodasHojasDeVida = () => hojaDeVidaHttp.get('/hojas-vida')
export const getHojaDeVida = (idHojaVida) => hojaDeVidaHttp.get(`/hojas-vida/${idHojaVida}`)
export const getHojaDeVidaPorRut = (rut) => hojaDeVidaHttp.get(`/hojas-vida/estudiante/${rut}`)
export const crearHojaDeVida = (data) => hojaDeVidaHttp.post('/hojas-vida', data)
export const actualizarHojaDeVida = (idHojaVida, data) => hojaDeVidaHttp.put(`/hojas-vida/${idHojaVida}`, data)
export const eliminarHojaDeVida = (idHojaVida) => hojaDeVidaHttp.delete(`/hojas-vida/${idHojaVida}`)

// --- Documentos oficiales adjuntos ---
export const getDocumentosPorHoja = (idHojaVida) => hojaDeVidaHttp.get(`/hojas-vida/${idHojaVida}/documentos`)
// Content-Type se anula a propósito: con FormData el navegador debe fijar el
// multipart/form-data con el boundary correcto (el default de la instancia es application/json).
export const subirDocumentoHojaVida = (idHojaVida, formData) =>
  hojaDeVidaHttp.post(`/hojas-vida/${idHojaVida}/documentos`, formData, {
    headers: { 'Content-Type': undefined },
  })
export const descargarDocumentoHojaVida = (id) =>
  hojaDeVidaHttp.get(`/hojas-vida/documentos/${id}/descarga`, { responseType: 'blob' })
export const eliminarDocumentoHojaVida = (id) => hojaDeVidaHttp.delete(`/hojas-vida/documentos/${id}`)

// --- Antecedentes Académicos ---
export const getAntecedentesAcademicos = () => hojaDeVidaHttp.get('/antecedentes-academicos')
export const getAntecedenteAcademicoById = (id) => hojaDeVidaHttp.get(`/antecedentes-academicos/${id}`)
export const crearAntecedenteAcademico = (data) => hojaDeVidaHttp.post('/antecedentes-academicos', data)
export const actualizarAntecedenteAcademico = (id, data) => hojaDeVidaHttp.put(`/antecedentes-academicos/${id}`, data)
export const eliminarAntecedenteAcademico = (id) => hojaDeVidaHttp.delete(`/antecedentes-academicos/${id}`)

// --- Antecedentes Apoderado ---
export const getAntecedentesApoderado = () => hojaDeVidaHttp.get('/antecedentes-apoderado')
export const getAntecedenteApoderadoById = (id) => hojaDeVidaHttp.get(`/antecedentes-apoderado/${id}`)
export const crearAntecedenteApoderado = (data) => hojaDeVidaHttp.post('/antecedentes-apoderado', data)
export const actualizarAntecedenteApoderado = (id, data) => hojaDeVidaHttp.put(`/antecedentes-apoderado/${id}`, data)
export const eliminarAntecedenteApoderado = (id) => hojaDeVidaHttp.delete(`/antecedentes-apoderado/${id}`)

// --- Antecedentes Médicos ---
export const getAntecedentesMedicos = () => hojaDeVidaHttp.get('/antecedentes-medicos')
export const getAntecedenteMedicoById = (id) => hojaDeVidaHttp.get(`/antecedentes-medicos/${id}`)
export const crearAntecedenteMedico = (data) => hojaDeVidaHttp.post('/antecedentes-medicos', data)
export const actualizarAntecedenteMedico = (id, data) => hojaDeVidaHttp.put(`/antecedentes-medicos/${id}`, data)
export const eliminarAntecedenteMedico = (id) => hojaDeVidaHttp.delete(`/antecedentes-medicos/${id}`)
