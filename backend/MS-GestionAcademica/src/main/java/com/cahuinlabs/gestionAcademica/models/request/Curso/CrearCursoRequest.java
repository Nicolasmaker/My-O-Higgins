package com.cahuinlabs.gestionAcademica.models.request.Curso;

import lombok.Data;

@Data
public class CrearCursoRequest {
    private String curLetraSec;
    private Integer curAnioEscolar;
    private Integer cupos;   // opcional; si es null se usa la capacidad de la sala
    private Integer idSala;
    private Integer idNivel;
}
