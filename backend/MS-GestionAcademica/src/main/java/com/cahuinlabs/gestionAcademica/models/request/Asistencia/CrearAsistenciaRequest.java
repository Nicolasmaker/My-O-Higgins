package com.cahuinlabs.gestionAcademica.models.request.Asistencia;

import lombok.Data;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
public class CrearAsistenciaRequest {

    @NotNull(message = "La fecha es obligatoria.")
    private LocalDate asisFecha;

    @NotNull(message = "El estado de asistencia es obligatorio.")
    private String asisEstado;

    @NotNull(message = "El RUT del estudiante es obligatorio.")
    @Positive(message = "El RUT del estudiante debe ser un número válido positivo.")
    private Integer estudianteUsuRut;

    @NotNull(message = "El ID de Impartir (docente+asignatura+curso) es obligatorio.")
    @Positive(message = "El ID de Impartir debe ser un número válido positivo.")
    private Integer idImpartir;
}
