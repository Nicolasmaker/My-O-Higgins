package com.cahuinlabs.hojadevida.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Ignoramos los campos del JSON que no necesitamos
@JsonIgnoreProperties(ignoreUnknown = true)
public record MatriculaDTO(
    @JsonProperty("idMatricula") Long idMatricula,
    @JsonProperty("matriculaEstado") String estado,
    @JsonProperty("alumnoRut") Long alumnoRut
) {}
