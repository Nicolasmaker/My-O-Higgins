package com.cahuinlabs.autenticacion.models.response;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private String        mensaje;
    private Integer       codigoEstado;
    private LocalDateTime fechaHora;

    public ErrorResponse(String mensaje, Integer codigoEstado) {
        this.mensaje = mensaje;
        this.codigoEstado = codigoEstado;
        this.fechaHora = LocalDateTime.now();
    }
}
