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
@Table(name = "comuna")
public class Comuna {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id_com")
    private int idCom;

    @ManyToOne
    @JoinColumn(name = "id_ciu", nullable = false)
    private Ciudad ciudad;

    @Column(name = "com_nom", nullable = false, length = 60)
    private String comNom;
}
