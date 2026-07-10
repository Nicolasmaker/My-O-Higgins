package com.cahuinlabs.gestionAcademica.models.request;

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

    // Campos de audiencia — determinan quién puede ver el evento en su calendario.
    private Long cursoId;
    private Long docenteUsuRut;
    private Long apoderadoUsuRut;
    private Long alumnoRut;
}
