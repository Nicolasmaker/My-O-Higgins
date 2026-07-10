package com.cahuinlabs.autenticacion.models.request.funcionarios.directivos;

import lombok.Data;

@Data
public class ActualizarDirectivoRequest {

//Datos de la tabla usuario
    private String  dirPrimerNombre;
    private String  dirSegundoNombre;
    private String  dirApellidoPat;
    private String  dirApellidoMat;
    private String  dirEmail;
    private String  dirTel;
    private Boolean dirEstadoActividad;
//Datos de la tabla funcionario
    private String  dirTitulo;
//Datos de la entidad directivo
    private String  dirCargo;
//Datos de la tabla direccion
    private String  dirDireccion;
    private Integer dirNumeroDireccion;
    private String  dirTipoCasa;
    private Integer idComuna;
}
