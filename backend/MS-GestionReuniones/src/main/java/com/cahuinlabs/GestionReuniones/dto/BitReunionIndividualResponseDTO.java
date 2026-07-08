package com.cahuinlabs.GestionReuniones.dto;

// DTO de lectura: la entrevista individual con su bitacora base ya enriquecida
// (RUT+DV y nombre de docente/apoderado/alumno).
public record BitReunionIndividualResponseDTO(
    Long idBitReuInd,
    String bitReuIndMotivReu,
    String bitReuIndTemTrat,
    Integer bitReuIndFirmaDoc,
    Integer bitReuIndFirmaApo,
    Long idAnotacion,
    BitReunionApoderadoResponseDTO bitReunionApoderado
) {}
