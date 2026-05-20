package com.cahuinlabs.autenticacion.models.entities.geografia;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "pais")
public class Pais {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id_pais")
    private int idPais;

    @Column(name = "pai_nom", nullable = false, length = 30)
    private String paiNom;
}
