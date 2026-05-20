package com.cahuinlabs.gestionAcademica.models.request.Evaluacion;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ActualizarEvaluacionRequest {
    private String    evaNom;
    private LocalDate evaFec;
    private String    evaPerioAcad; // Ej: Primer Trimestre
    private String    evaTip;       
    private Integer   docenteUsuRut; //RUT del profe (viene del otro MS) 
}
