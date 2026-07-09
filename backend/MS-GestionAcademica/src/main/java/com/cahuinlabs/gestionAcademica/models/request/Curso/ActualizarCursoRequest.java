package com.cahuinlabs.gestionAcademica.models.request.Curso;

import lombok.Data;

@Data
public class ActualizarCursoRequest {
    private String curLetraSec;
    private Integer cupos;   // opcional; si es null se usa la capacidad de la sala
    private Integer idSala;
}
