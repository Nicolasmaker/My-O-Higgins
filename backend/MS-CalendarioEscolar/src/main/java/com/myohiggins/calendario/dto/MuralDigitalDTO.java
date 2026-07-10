package com.myohiggins.calendario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MuralDigitalDTO {
    private Long idMurDig;
    private String titulo;
    private String contenido;
    private LocalDate fechaPublicacion;
    private Long funcionarioUsuRut;
}
