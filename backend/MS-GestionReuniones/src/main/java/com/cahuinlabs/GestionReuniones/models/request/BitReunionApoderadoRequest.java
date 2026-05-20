package com.cahuinlabs.GestionReuniones.models.request;

import java.time.LocalDate;
import lombok.Data;

@Data
public class BitReunionApoderadoRequest {
    private LocalDate bitReuFec;
    private String bitReuCompromisos;
    private String bitReuObs;
    private Long docenteUsuRut;
}
