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
@Table(name = "docente")
@PrimaryKeyJoinColumn(name = "usu_rut")
public class Docente extends Funcionario {

    @Column(name = "dcte_especialidad", nullable = false, length = 80)
    private String dcteEspecialidad;
}
