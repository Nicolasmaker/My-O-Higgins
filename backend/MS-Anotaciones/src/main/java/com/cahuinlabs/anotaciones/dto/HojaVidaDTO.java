package com.cahuinlabs.anotaciones.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Ignoramos los campos del JSON que no necesitamos
@JsonIgnoreProperties(ignoreUnknown = true)
public record HojaVidaDTO(
    @JsonProperty("idHojaVida") Long idHojaVida,
    @JsonProperty("estudianteUsuRut") Long estudianteUsuRut,
    @JsonProperty("matriculaId") Long matriculaId
) {}
