package com.cahuinlabs.autenticacion.models.entities.usuarios;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private int id_rol;

    @OneToOne
    @JoinColumn(name = "usu_rut", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @Column(name = "rol_nom", nullable = false, length = 30)
    private String rolNombre;
}
