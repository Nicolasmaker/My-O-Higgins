package com.myohiggins.msmatricula.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Ignoramos los campos del JSON que no necesitamos
@JsonIgnoreProperties(ignoreUnknown = true)
public record ApoderadoDTO(
    @JsonProperty("usuRut") Long rut,
    @JsonProperty("usuDvRut") String dv,
    @JsonProperty("usuPNombre") String nombre,
    @JsonProperty("usuApePat") String apellido,
    @JsonProperty("usuTel") String telefono
) {}
