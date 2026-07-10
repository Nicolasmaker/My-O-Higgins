package com.cahuinlabs.GestionReuniones.dto;

// DTO de lectura: la reunion general con su bitacora base ya enriquecida
// (RUT+DV y nombre de docente/apoderado/alumno).
public record BitReunionGeneralResponseDTO(
    Long bitReuGen,
    String bitReuGenTipReu,
    String bitReuGenComunicEmi,
    String bitReuGenAcuerTrat,
    String bitReuGenObs,
    Long cursoId,
    BitReunionApoderadoResponseDTO bitReunionApoderado
) {}
