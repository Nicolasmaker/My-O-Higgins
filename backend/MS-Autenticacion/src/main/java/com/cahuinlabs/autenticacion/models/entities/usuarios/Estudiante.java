package com.cahuinlabs.autenticacion.models.entities.usuarios;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;


//ESTA TABLA ESTA ASOCIADA AL ID DE UN CURSO EL CUAL NO SE HA DEFINIDO AUN, 
//POR LO QUE SE DEBE CREAR UNA CLAVE FORANEA HACIA LA TABLA CURSO CUANDO ESTA SE HAYA DEFINIDO (MS-GestionAcademica)

@Entity
@Data
@EqualsAndHashCode(callSuper = true) //esto es para que el equals y hashCode de esta clase incluyan los campos de la clase padre (Usuario)
@Table(name = "estudiante")
@PrimaryKeyJoinColumn(name = "usu_rut") //esto es para que la clave primaria de esta tabla sea la misma que la clave primaria de la tabla padre (Usuario)
public class Estudiante extends Usuario {

    @Column(name = "est_parentesco", nullable = false, length = 40)
    private String estParentesco;
}
