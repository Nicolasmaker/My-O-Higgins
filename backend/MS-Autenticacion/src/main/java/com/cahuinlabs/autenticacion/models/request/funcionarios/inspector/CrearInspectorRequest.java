package com.cahuinlabs.autenticacion.models.request.funcionarios.inspector;

import lombok.Data;

@Data
public class CrearInspectorRequest {

//Datos de la tabla usuario
    private Integer   insRut;
    private Character insDvRut;
    private String    insPrimerNombre;
    private String    insSegundoNombre;
    private String    insApellidoPat;
    private String    insApellidoMat;
    private String    insEmail;
    private String    insPassword;
    private String    insTel;
//Datos de la tabla funcionario
    private String    insTitulo;
//Datos de la entidad inspector
    private String    insNivel;
//Datos de la tabla direccion
    private String    insDireccion;
    private Integer   insNumeroDireccion;
    private String    insTipoCasa;
    private Integer   idComuna;
}
