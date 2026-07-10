package com.cahuinlabs.GestionReuniones.models.request;

import lombok.Data;

@Data
public class ConfirmarReunionRequest {
    // ACEPTADA o RECHAZADA
    private String estadoConfirmacion;
}
