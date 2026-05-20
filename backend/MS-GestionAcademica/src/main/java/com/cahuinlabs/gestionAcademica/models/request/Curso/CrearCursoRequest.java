package com.cahuinlabs.gestionAcademica.models.request.Curso;

import lombok.Data;

@Data
public class CrearCursoRequest {
    private String curLetraSec;
    private Integer curAnioEscolar;
    private Integer idSala;  
    private Integer idNivel; 
}
