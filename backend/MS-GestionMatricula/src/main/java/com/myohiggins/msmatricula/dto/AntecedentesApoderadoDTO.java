package com.myohiggins.msmatricula.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

// Mismo shape que AntecedentesApoderadoDTO en MS-HojaDeVida — se usa solo para armar el body
// del POST /api/antecedentes-apoderado al auto-crear el antecedente en la primera matrícula.
@Data
@NoArgsConstructor
public class AntecedentesApoderadoDTO {
    private Long idAntApo;
    private String nombre;
    private String profesion;
    private String telefono;
    private String direccion;
    private String lugarTrabajo;
    private String disponibilidadHoraria;
    private Long idHojaVida;
}
