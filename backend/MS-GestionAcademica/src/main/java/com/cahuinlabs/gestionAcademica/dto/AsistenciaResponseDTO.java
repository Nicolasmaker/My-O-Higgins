package com.cahuinlabs.gestionAcademica.dto;

import com.cahuinlabs.gestionAcademica.models.entities.Impartir;

import java.time.LocalDate;

// DTO de lectura: la asistencia enriquecida con RUT+DV y nombre del estudiante, resueltos
// desde MS-Autenticacion (mismo patron que AnotacionResponseDTO en MS-Anotaciones).
public record AsistenciaResponseDTO(
    Integer idAsis,
    LocalDate asisFecha,
    String asisEstado,
    Integer estudianteUsuRut,
    String estudianteDv,
    String estudianteNombre,
    String estudianteApellido,
    Impartir impartir
) {}
