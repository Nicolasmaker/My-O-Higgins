package com.myohiggins.calendario.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Ignoramos los campos del JSON que no necesitamos
@JsonIgnoreProperties(ignoreUnknown = true)
public record AsignaturaDTO(
    @JsonProperty("idAsi") Integer id,
    @JsonProperty("asiNombre") String nombre,
    @JsonProperty("asiDescripcion") String descripcion
) {}
