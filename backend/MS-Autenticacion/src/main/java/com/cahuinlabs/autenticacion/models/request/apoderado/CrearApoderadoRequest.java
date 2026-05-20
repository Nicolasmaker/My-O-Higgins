package com.cahuinlabs.autenticacion.models.request.apoderado;

import lombok.Data;

@Data
public class CrearApoderadoRequest {

//Datos de la tabla padre usuario 
    private Integer   apoRut; 
    private Character apoDvRut;
    private String    apoPrimerNombre;
    private String    apoSegundoNombre;
    private String    apoApellidoPat;
    private String    apoApellidoMat;
    private String    apoEmail;
    private String    apoTel;
    private String    apoPassword;
//Dato de la tabla hija apoderado
    private String    apoParentesco;
//Datos de la tabla direccion
    private String    apoDireccion;  
    private Integer   apoNumeroDireccion;
    private String    apoTipoCasa;
    private Integer   idComuna;
}
