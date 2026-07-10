package com.cahuinlabs.gestionAcademica.models.request.Asistencia;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class ActualizarAsistenciaRequest {

    @NotNull(message = "El estado de asistencia es obligatorio.")
    private String asisEstado;
}
