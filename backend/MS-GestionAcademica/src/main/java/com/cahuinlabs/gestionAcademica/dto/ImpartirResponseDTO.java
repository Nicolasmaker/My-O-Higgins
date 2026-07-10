package com.cahuinlabs.gestionAcademica.dto;

import com.cahuinlabs.gestionAcademica.models.entities.Asignatura;
import com.cahuinlabs.gestionAcademica.models.entities.Curso;

// DTO de lectura: el registro de Impartir enriquecido con RUT+DV y nombre del docente,
// resueltos desde MS-Autenticacion (mismo patron que AnotacionResponseDTO en MS-Anotaciones).
public record ImpartirResponseDTO(
    Integer idImp,
    Integer docenteUsuRut,
    String docenteDv,
    String docenteNombre,
    String docenteApellido,
    Asignatura asignatura,
    Curso curso
) {}
