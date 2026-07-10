package com.cahuinlabs.autenticacion.models.entities.geografia;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "ciudad")
public class Ciudad {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id_ciu")
    private int idCiu;

    @Column(name = "ciu_nom", nullable = false, length = 50)
    private String ciuNom;

    @ManyToOne
    @JoinColumn(name = "id_reg", nullable = false)
    private Region region;
}
