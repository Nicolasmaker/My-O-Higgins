package com.cahuinlabs.gestionAcademica.models.request.Nota;

import lombok.Data;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

@Data
public class CrearNotaRequest {

    @NotNull(message = "La calificación es obligatoria.")
    @DecimalMin(value = "1.0", message = "La nota mínima permitida es 1.0")
    @DecimalMax(value = "7.0", message = "La nota máxima permitida es 7.0")
    private Double    notCalif; 

    @NotNull(message = "La fecha de registro es obligatoria.")
    @PastOrPresent(message = "La fecha de la nota no puede ser en el futuro.")
    private LocalDate notFechaReg;

    @NotNull(message = "El ID de la evaluación es obligatorio.")
    @Positive(message = "El ID de la evaluación debe ser un número válido positivo.")
    private Integer   idEvaluacion;

    @NotNull(message = "El RUT del estudiante es obligatorio.")
    @Positive(message = "El RUT del estudiante debe ser un número válido positivo.")
    private Integer   estudianteUsuRut;
}
