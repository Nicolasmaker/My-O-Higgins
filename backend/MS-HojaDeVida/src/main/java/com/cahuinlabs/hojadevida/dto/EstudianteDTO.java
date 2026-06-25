package com.cahuinlabs.hojadevida.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Le decimos que ignore cualquier otro dato del JSON que no necesitemos
@JsonIgnoreProperties(ignoreUnknown = true)
public record EstudianteDTO(
    @JsonProperty("usuRut") Long rut,
    @JsonProperty("usuPNombre") String nombre,
    @JsonProperty("usuEmail") String correo,
    @JsonProperty("rol") RolDTO rol
) {
    // Creamos un sub-record para atrapar el objeto anidado "rol"
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RolDTO(
        @JsonProperty("rolNombre") String rolNombre
    ) {}
}
