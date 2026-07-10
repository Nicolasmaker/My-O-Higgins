package com.cahuinlabs.GestionReuniones.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Espeja MessageDTO de MS-Mensajeria (POST /api/mensajeria/enviar)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensajeRequest {
    private Long remitenteRut;
    private Long destinatarioRut;
    private String asunto;
    private String contenido;
}
