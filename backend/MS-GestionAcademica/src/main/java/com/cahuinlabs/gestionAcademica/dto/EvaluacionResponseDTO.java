package com.cahuinlabs.gestionAcademica.dto;

import com.cahuinlabs.gestionAcademica.models.entities.Asignatura;
import com.cahuinlabs.gestionAcademica.models.entities.Curso;

import java.time.LocalDate;

// DTO de lectura: la evaluacion enriquecida con RUT+DV y nombre del docente, resueltos
// desde MS-Autenticacion (mismo patron que AnotacionResponseDTO en MS-Anotaciones).
public record EvaluacionResponseDTO(
    Integer idEva,
    String evaNom,
    LocalDate evaFecha,
    String evaPeriodoAcad,
    String evaTipo,
    Integer docenteUsuRut,
    String docenteDv,
    String docenteNombre,
    String docenteApellido,
    Asignatura asignatura,
    Curso curso,
    Long idCalEst
) {}
