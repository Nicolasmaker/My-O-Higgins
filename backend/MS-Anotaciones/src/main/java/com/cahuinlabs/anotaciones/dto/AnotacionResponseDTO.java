package com.cahuinlabs.anotaciones.dto;

import java.time.LocalDate;

// DTO de lectura: la anotacion enriquecida con datos del funcionario que la creo
// y del estudiante/curso al que pertenece, resueltos desde Autenticacion, HojaDeVida
// y GestionAcademica.
public record AnotacionResponseDTO(
    Long idAnot,
    String anotTip,
    String anotDes,
    LocalDate anotFec,
    Long funcionarioUsuRut,
    String funcionarioDv,
    String funcionarioNombre,
    String funcionarioApellido,
    String funcionarioRol,
    Long idHojaVida,
    Long estudianteUsuRut,
    String estudianteDv,
    String estudianteNombre,
    String estudianteApellido,
    String curso
) {}
