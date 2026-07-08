package com.cahuinlabs.autenticacion.models.request.apoderado;

import lombok.Data;

@Data
public class ActualizarApoderadoRequest {

//Datos de la tabla padre usuario 
    private String    apoPrimerNombre;
    private String    apoSegundoNombre;
    private String    apoApellidoPat;
    private String    apoApellidoMat;
    private String    apoEmail;
    private String    apoTel;
    private Boolean   apoEstadoActividad;
//Datos de la tabla direccion
    private String    apoDireccion;
    private Integer   apoNumeroDireccion;
    private String    apoTipoCasa;
    private Integer   idComuna;
}
