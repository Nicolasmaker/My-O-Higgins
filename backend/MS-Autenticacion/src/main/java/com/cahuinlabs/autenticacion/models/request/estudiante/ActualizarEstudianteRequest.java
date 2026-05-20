package com.cahuinlabs.autenticacion.models.request.estudiante;

import lombok.Data;

@Data
public class ActualizarEstudianteRequest {
    private String    estPrimerNombre;
    private String    estSegundoNombre;
    private String    estApellidoPat;
    private String    estApellidoMat;
    private String    estEmail;
    private String    estTel;
    private String    estParentesco;
    private Boolean   estEstadoActividad;
//Datos de la tabla direccion
    private String    estDireccion;  
    private Integer   estNumeroDireccion;
    private String    estTipoCasa;
    private Integer   idComuna;
}