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
@Table(name = "directivo")
@PrimaryKeyJoinColumn(name = "usu_rut")
public class Directivo extends Funcionario {

    @Column(name = "dir_cargo", nullable = false, length = 40)
    private String dirCargo;
}
