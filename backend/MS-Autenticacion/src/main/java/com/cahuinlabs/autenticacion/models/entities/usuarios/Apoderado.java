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
@Table(name = "apoderado")
@PrimaryKeyJoinColumn(name = "usu_rut") 
public class Apoderado extends Usuario {

    @Column(name = "apo_parentesco", nullable = false, length = 40)
    private String apoParentesco;
}
