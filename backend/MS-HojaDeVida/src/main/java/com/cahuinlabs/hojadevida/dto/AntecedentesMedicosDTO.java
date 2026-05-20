package com.cahuinlabs.hojadevida.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AntecedentesMedicosDTO {
    private Long idAntMed;
    private String alergias;
    private String condicionesMedicas;
    private String medicamentos;
    private String tipoSangre;
    private String observaciones;
    private Long idHojaVida;
}
