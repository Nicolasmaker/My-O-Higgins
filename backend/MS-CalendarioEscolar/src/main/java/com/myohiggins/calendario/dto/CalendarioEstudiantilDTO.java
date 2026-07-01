package com.myohiggins.calendario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * Objeto de Transferencia de Datos (DTO) para la entidad CalendarioEstudiantil.
 * Sirve para intercambiar datos con el cliente (por ejemplo, Postman o frontend)
 * sin exponer directamente la estructura de la base de datos (Entidad).
 */
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
