package com.cahuinlabs.gestionAcademica.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Ignoramos los campos del JSON que no necesitamos
@JsonIgnoreProperties(ignoreUnknown = true)
public record FuncionarioDTO(
    @JsonProperty("usuRut") Long rut,
    @JsonProperty("usuDvRut") String dv,
    @JsonProperty("usuPNombre") String nombre,
    @JsonProperty("usuApePat") String apellido,
    @JsonProperty("rol") RolDTO rol
) {
    // Sub-record para capturar el objeto "rol" que viene dentro del JSON
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RolDTO(
        @JsonProperty("rolNombre") String rolNombre
    ) {}
}
