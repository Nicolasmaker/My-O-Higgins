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
@Table(name = "estudiante")
@PrimaryKeyJoinColumn(name = "usu_rut")
public class Estudiante extends Usuario {

    // FK cruzada a MS-GestionAcademica — plain Integer, sin @ManyToOne (distinta BD)
    @Column(name = "CURSO_id_cur")
    private Integer cursoId;
}
