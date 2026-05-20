package com.cahuinlabs.autenticacion.models.request.estudiante;

import lombok.Data;

@Data
public class CrearEstudianteRequest {

//Datos de la tabla padre usuario 
    private Integer   estRut; 
    private Character estDvRut;
    private String    estPrimerNombre;
    private String    estSegundoNombre;
    private String    estApellidoPat;
    private String    estApellidoMat;
    private String    estEmail;
    private String    estTel;
    private String    estPassword;
//Dato de la tabla hija estudiante
    private String    estParentesco;
//Datos de la tabla direccion
    private String    estDireccion;  
    private Integer   estNumeroDireccion;
    private String    estTipoCasa;
    private Integer   idComuna;
}
