package com.myohiggins.msmensajeria.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Ignoramos los campos del JSON que no necesitamos
// Usamos Usuario generico porque cualquier tipo de usuario puede enviar o recibir mensajes
@JsonIgnoreProperties(ignoreUnknown = true)
public record UsuarioDTO(
    @JsonProperty("usuRut") Long rut,
    @JsonProperty("usuPNombre") String nombre,
    @JsonProperty("usuApePat") String apellido
) {}
