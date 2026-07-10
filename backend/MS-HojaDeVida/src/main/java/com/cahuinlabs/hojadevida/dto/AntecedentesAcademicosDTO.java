package com.cahuinlabs.hojadevida.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AntecedentesAcademicosDTO {
    private Long idAntAcad;
    private Integer anioEscolar;
    private Double promedioGeneralActual;
    private String situacionFinalAprobacion;
    private Long idHojaVida;
}
