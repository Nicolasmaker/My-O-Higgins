package com.cahuinlabs.anotaciones.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Ignoramos los campos del JSON que no necesitamos
@JsonIgnoreProperties(ignoreUnknown = true)
public record CursoDTO(
    @JsonProperty("idCur") Integer idCur,
    @JsonProperty("curLetraSeccion") String curLetraSeccion,
    @JsonProperty("nivel") NivelDTO nivel
) {
    // Sub-record para capturar el objeto "nivel" que viene dentro del JSON
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record NivelDTO(
        @JsonProperty("nivNum") Integer nivNum
    ) {}
}
