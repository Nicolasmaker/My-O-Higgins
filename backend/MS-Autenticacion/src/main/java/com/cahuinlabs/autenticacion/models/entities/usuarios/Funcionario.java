package com.cahuinlabs.autenticacion.models.entities.usuarios;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "funcionario")
@PrimaryKeyJoinColumn(name = "usu_rut") 
public class Funcionario extends Usuario {

    @Column(name = "fun_titulo", nullable = false, length = 50)
    private String funTitulo;
}
