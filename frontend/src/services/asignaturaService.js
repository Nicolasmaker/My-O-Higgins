// =============================================================
// SERVICIO DE ASIGNATURAS — asignaturaService.js
// =============================================================
// Funciones para consultar asignaturas desde MS-GestionAcademica.
// =============================================================
import asignaturaHttp from './http/asignaturaHttp'

export const getAsignaturas = () => asignaturaHttp.get('')