package com.cahuinlabs.gestionAcademica.models.request.Evaluacion;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CrearEvaluacionRequest {
    private String    evaNom;
    private LocalDate evaFec;
    private String    evaPerioAcad; // Ej: Primer Trimestre
    private String    evaTip;       
    private Integer   docenteUsuRut; //RUT del profe (viene del otro MS)
    private Integer   idAsignatura;  
}