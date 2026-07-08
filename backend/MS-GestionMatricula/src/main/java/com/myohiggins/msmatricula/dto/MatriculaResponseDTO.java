package com.myohiggins.msmatricula.dto;

import java.time.LocalDate;

// DTO de lectura: la matricula enriquecida con RUT+DV y nombre del alumno, apoderado
// y funcionario, resueltos desde MS-Autenticacion (mismo patron que AnotacionResponseDTO
// en MS-Anotaciones).
public record MatriculaResponseDTO(
    Long idMatricula,
    Long cursoId,
    String tipoAlumno,
    LocalDate matriculaFecha,
    String matriculaEstado,
    Integer matriculaAnioAcademico,
    Long alumnoRut,
    String alumnoDv,
    String alumnoNombre,
    String alumnoApellido,
    Long apoderadoRut,
    String apoderadoDv,
    String apoderadoNombre,
    String apoderadoApellido,
    Long funcionarioUsuRut,
    String funcionarioDv,
    String funcionarioNombre,
    String funcionarioApellido
) {}
