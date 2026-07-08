package com.myohiggins.msmatricula.dto;

import java.time.LocalDate;

// DTO de lectura: la solicitud de matricula enriquecida con RUT+DV y nombre del alumno
// y del apoderado, resueltos desde MS-Autenticacion (mismo patron que MatriculaResponseDTO).
public record SolicitudMatriculaResponseDTO(
    Long idSolicitud,
    Long alumnoRut,
    String alumnoDv,
    String alumnoNombre,
    String alumnoApellido,
    Long apoderadoRut,
    String apoderadoDv,
    String apoderadoNombre,
    String apoderadoApellido,
    Long cursoId,
    String tipoAlumno,
    String parentesco,
    String observaciones,
    String estado,
    LocalDate fechaSolicitud,
    String motivoRechazo
) {}
