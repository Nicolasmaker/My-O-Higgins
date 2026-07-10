package com.cahuinlabs.gestionAcademica.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Espeja la respuesta de MS-CalendarioEscolar (GET/POST/PUT /api/calendarios) — solo
// necesitamos idCalEst para guardarlo en la Evaluacion y poder actualizar/eliminar el evento despues.
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarioEstudiantilDTO {

    private Long idCalEst;
    private String tituloEvento;
    private String tipoEvento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Long idMuralDigital;
    private Long idAsignatura;
    private String descripcionEvento;
}
