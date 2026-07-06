package com.cahuinlabs.gestionAcademica.models.request.Impartir;

import lombok.Data;

@Data
public class CrearImpartirRequest {
    private Integer docenteUsuRut;
    private Integer idAsignatura;
    private Integer idCurso;
}
