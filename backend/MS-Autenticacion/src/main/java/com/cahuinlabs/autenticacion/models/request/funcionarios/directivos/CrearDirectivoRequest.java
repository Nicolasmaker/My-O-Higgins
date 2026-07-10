package com.cahuinlabs.autenticacion.models.request.funcionarios.directivos;

import lombok.Data;

@Data
public class CrearDirectivoRequest {

//Datos de la tabla usuario
    private Integer   dirRut;
    private Character dirDvRut;
    private String    dirPrimerNombre;
    private String    dirSegundoNombre;
    private String    dirApellidoPat;
    private String    dirApellidoMat;
    private String    dirEmail;
    private String    dirPassword;
    private String    dirTel;
//Datos de la tabla funcionario
    private String    dirTitulo;
//Datos de la entidad directivo
    private String    dirCargo;
//Datos de la tabla direccion
    private String    dirDireccion;
    private Integer   dirNumeroDireccion;
    private String    dirTipoCasa;
    private Integer   idComuna;
}
