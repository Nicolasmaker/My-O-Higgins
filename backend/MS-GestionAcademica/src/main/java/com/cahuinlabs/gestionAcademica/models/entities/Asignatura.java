package com.cahuinlabs.gestionAcademica.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "asignatura")
public class Asignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asi")
    private Integer idAsi;

    @Column(name = "asi_nombre", nullable = false, length = 30)
    private String asiNombre;

    @Column(name = "asi_descripcion", nullable = false, length = 200)
    private String asiDescripcion;
}
