package com.cahuinlabs.hojadevida.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HojaVidaEstudianteDTO {
    private Long idHojaVida;
    private Long estudianteUsuRut;
    private Long matriculaId;
}
