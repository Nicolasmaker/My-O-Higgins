package com.cahuinlabs.autenticacion.models.request.funcionarios.docente;

import lombok.Data;

@Data
public class CrearDocenteRequest {

//Datos de la entidad usuario
    private Integer   dcteRut;
    private Character dcteDvRut;
    private String    dctePrimerNombre;
    private String    dcteSegundoNombre;
    private String    dcteApellidoPat;
    private String    dcteApellidoMat;
    private String    dcteEmail;
    private String    dctePassword;
    private String    dcteTel;
//Datos de la tabla funcionario
    private String    dcteTitulo;
//Datos de la entidad docente
    private String    dcteEspecialidad;
//Datos de la tabla direccion
    private String    dcteDireccion;
    private Integer   dcteNumeroDireccion;
    private String    dcteTipoCasa;
    private Integer   idComuna;
}
