package com.myohiggins.msmatricula.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

// Ignoramos los campos del JSON que no necesitamos. Mismo shape que HojaVidaEstudianteDTO
// en MS-HojaDeVida — se usa tanto para leer (GET) como para armar el body de POST/PUT.
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HojaVidaDTO {
    private Long idHojaVida;
    private Long estudianteUsuRut;
    private Long matriculaId;
    private String estado;
}
