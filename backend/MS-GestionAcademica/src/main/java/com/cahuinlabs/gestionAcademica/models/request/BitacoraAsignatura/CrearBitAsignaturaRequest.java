package com.cahuinlabs.gestionAcademica.models.request.BitacoraAsignatura;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CrearBitAsignaturaRequest {
    private LocalDate bitAsiFecClase;
    private String    bitAsiActividades;
    private String    bitAsiContenidos;
    private String    bitAsiObs;
    private String    bitAsiObjApren; //Puede ser nulo
    private Integer   idAsignatura; 
}
