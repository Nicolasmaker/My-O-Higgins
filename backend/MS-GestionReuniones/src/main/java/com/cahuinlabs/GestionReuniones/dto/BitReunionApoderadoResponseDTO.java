package com.cahuinlabs.GestionReuniones.dto;

import java.time.LocalDate;

// DTO de lectura: la bitacora base de reunion enriquecida con RUT+DV y nombre del
// docente/inspector, del apoderado y del alumno, resueltos desde MS-Autenticacion
// (mismo patron que AnotacionResponseDTO en MS-Anotaciones).
public record BitReunionApoderadoResponseDTO(
    Long idBitReu,
    LocalDate bitReuFec,
    String bitReuCompromisos,
    String bitReuObs,
    Long docenteUsuRut,
    String docenteDv,
    String docenteNombre,
    String docenteApellido,
    Long apoderadoUsuRut,
    String apoderadoDv,
    String apoderadoNombre,
    String apoderadoApellido,
    Long alumnoRut,
    String alumnoDv,
    String alumnoNombre,
    String alumnoApellido,
    String estadoConfirmacion,
    Long idCalEst
) {}
