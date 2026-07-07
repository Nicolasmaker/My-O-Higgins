// =============================================================
// SERVICIO DE REUNIONES — reunionesService.js
// =============================================================
// Funciones para gestionar reuniones.
// Apunta al MS-GestionReuniones (puerto 8081).
//
// Tipos de reunión: apoderado, individual (entrevista), general (acta).
// =============================================================
import reunionesHttp from './http/reunionesHttp'

// --- Reuniones con Apoderado ---
export const getReunionesApoderado = () => reunionesHttp.get('/apoderado')
export const getReunionApoderadoById = (id) => reunionesHttp.get(`/apoderado/${id}`)
export const crearReunionApoderado = (data) => reunionesHttp.post('/apoderado', data)
export const actualizarReunionApoderado = (id, data) => reunionesHttp.put(`/apoderado/${id}`, data)
export const confirmarReunionApoderado = (id, data) => reunionesHttp.patch(`/apoderado/${id}/confirmacion`, data)

// --- Entrevistas Individuales ---
export const getReunionesIndividuales = () => reunionesHttp.get('/individual')
export const getReunionIndividualById = (id) => reunionesHttp.get(`/individual/${id}`)
export const crearReunionIndividual = (data) => reunionesHttp.post('/individual', data)
export const actualizarFirmasIndividual = (id, data) => reunionesHttp.put(`/individual/${id}/firmas`, data)
// Bitácora post-reunión (temas tratados) — solo se llena tras aceptar, no al agendar
export const actualizarIndividual = (id, data) => reunionesHttp.put(`/individual/${id}`, data)

// --- Reuniones Generales ---
export const getReunionesGenerales = () => reunionesHttp.get('/general')
export const getReunionGeneralById = (id) => reunionesHttp.get(`/general/${id}`)
export const crearReunionGeneral = (data) => reunionesHttp.post('/general', data)
// Acta post-reunión (comunicado/acuerdos/obs) — se completa con "Completar acta"
export const actualizarGeneral = (id, data) => reunionesHttp.put(`/general/${id}`, data)

// --- Consulta por funcionario ---
export const getReunionesApoderadoPorFuncionario = (rut) => reunionesHttp.get(`/funcionario/${rut}`)
