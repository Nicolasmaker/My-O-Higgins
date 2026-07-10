package com.cahuinlabs.autenticacion.models.entities.geografia;

import com.cahuinlabs.autenticacion.models.entities.usuarios.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "direccion")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dir")
    private int idDir;

    @ManyToOne
    @JoinColumn(name = "usu_rut", nullable = false)
    private Usuario usuario;

    @Column(name = "dir_nom", nullable = false, length = 100)
    private String dirNom;

    @Column(name = "dir_num", nullable = false)
    private int dirNum;

    @Column(name = "dir_depto_casa", nullable = false, length = 20)
    private String dirDeptoCasa;

    @ManyToOne
    @JoinColumn(name = "id_com", nullable = false)
    private Comuna comuna;
}
