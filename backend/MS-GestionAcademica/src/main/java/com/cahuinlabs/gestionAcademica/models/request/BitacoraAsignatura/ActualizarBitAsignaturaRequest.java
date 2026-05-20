package com.cahuinlabs.gestionAcademica.models.request.BitacoraAsignatura;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ActualizarBitAsignaturaRequest {
    private LocalDate bitAsiFecClase;
    private String    bitAsiActividades;
    private String    bitAsiContenidos;
    private String    bitAsiObs;
    private String    bitAsiObjApren; //Puede ser nulo
}
