package com.cahuinlabs.GestionReuniones.models.request;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ReunionGeneralRequest {
    private LocalDate bitReuFec;
    private String bitReuCompromisos;
    private String bitReuObs;
    private Long docenteUsuRut;
    private String bitReuGenTipReu;
    private String bitReuGenComunicEmi;
    private String bitReuGenAcuerTrat;
    private String bitReuGenObs;
}