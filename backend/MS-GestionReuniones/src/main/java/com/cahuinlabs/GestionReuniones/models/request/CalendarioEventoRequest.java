package com.cahuinlabs.GestionReuniones.models.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarioEventoRequest {

    private String tituloEvento;
    private String tipoEvento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Long idMuralDigital;
    private Long idAsignatura;
    private String descripcionEvento;
}
