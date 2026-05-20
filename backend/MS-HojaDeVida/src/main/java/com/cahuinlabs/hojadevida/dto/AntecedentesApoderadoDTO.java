package com.cahuinlabs.hojadevida.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
