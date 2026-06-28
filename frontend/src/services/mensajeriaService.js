// =============================================================
// SERVICIO DE MENSAJERÍA — mensajeriaService.js
// =============================================================
// Funciones para el sistema de mensajería interna.
// Apunta al MS-Mensajeria (puerto 8089).
//
// =============================================================
import mensajeriaHttp from './http/mensajeriaHttp'

export const getBandeja = (destinatarioRut) => mensajeriaHttp.get(`/bandeja/${destinatarioRut}`)
export const getEnviados = (remitenteRut) => mensajeriaHttp.get(`/enviados/${remitenteRut}`)
export const enviarMensaje = (data) => mensajeriaHttp.post('/enviar', data)
