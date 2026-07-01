package com.cahuinlabs.autenticacion.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String  token;
    private Integer usuRut;
    private String  usuPNombre;
    private String  usuApePat;
    private String  usuEmail;
    private String  rolNombre;
    private Boolean usuEstadoActividad;
}
