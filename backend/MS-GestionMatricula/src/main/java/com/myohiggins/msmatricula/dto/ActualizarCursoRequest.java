package com.myohiggins.msmatricula.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

// Body para PATCH /estudiantes/{rut}/curso en MS-Autenticacion.
@Data
@NoArgsConstructor
public class ActualizarCursoRequest {
    private Integer cursoId;
}
