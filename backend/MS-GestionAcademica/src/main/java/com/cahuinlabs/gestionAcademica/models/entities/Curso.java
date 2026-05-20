package com.cahuinlabs.gestionAcademica.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cur")
    private Integer idCur;

    @Column(name = "cur_letra_seccion", nullable = false, length = 1)
    private String curLetraSeccion;

    @Column(name = "cur_anio_escolar", nullable = false)
    private Integer curAnioEscolar;

    // Relaciones 
    @ManyToOne
    @JoinColumn(name = "sala_id_sal", nullable = false)
    private Sala sala;

    @ManyToOne
    @JoinColumn(name = "nivel_id_niv", nullable = false)
    private Nivel nivel;
}
