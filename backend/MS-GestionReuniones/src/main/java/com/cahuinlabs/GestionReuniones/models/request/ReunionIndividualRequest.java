package com.cahuinlabs.GestionReuniones.models.request;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ReunionIndividualRequest {
    private LocalDate bitReuFec;
    private String bitReuCompromisos;
    private String bitReuObs;
    private Long docenteUsuRut;
    private Long apoderadoUsuRut;
    private Long alumnoRut;
    private String bitReuIndMotivReu;
    private String bitReuIndTemTrat;
    private Long idAnotacion;
}
