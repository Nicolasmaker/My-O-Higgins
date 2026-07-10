// =============================================================
// SERVICIO DE MATRÍCULA — matriculaService.js
// =============================================================
// Funciones para gestión de matrículas.
// Apunta al MS-GestionMatricula (puerto 8086).
//
// =============================================================
import matriculaHttp from './http/matriculaHttp'

export const getMatriculas = () => matriculaHttp.get('')
export const getMatriculaById = (id) => matriculaHttp.get(`/${id}`)
export const crearMatricula = (data) => matriculaHttp.post('', data)
export const actualizarMatricula = (id, data) => matriculaHttp.put(`/${id}`, data)
export const eliminarMatricula = (id) => matriculaHttp.delete(`/${id}`)

// --- Solicitudes de matrícula (self-service Apoderado) ---
export const crearSolicitudMatricula = (data) => matriculaHttp.post('/solicitudes', data)
export const getSolicitudesMatricula = () => matriculaHttp.get('/solicitudes')
export const getSolicitudesPorApoderado = (rut) => matriculaHttp.get(`/solicitudes/apoderado/${rut}`)
export const aprobarSolicitudMatricula = (id, data) => matriculaHttp.patch(`/solicitudes/${id}/aprobar`, data)
export const rechazarSolicitudMatricula = (id, data) => matriculaHttp.patch(`/solicitudes/${id}/rechazar`, data)
