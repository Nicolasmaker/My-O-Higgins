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
@Table(name = "inspector")
@PrimaryKeyJoinColumn(name = "usu_rut")
public class Inspector extends Funcionario {

    @Column(name = "ins_nivel", nullable = false, length = 20)
    private String insNivel;
}
