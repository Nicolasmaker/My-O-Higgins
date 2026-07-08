package com.cahuinlabs.hojadevida.model;

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

@Entity
@Table(name = "ANTECEDENTES_APODERADO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AntecedentesApoderado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ant_apo")
    private Long idAntApo;

    @Column(name = "ant_apo_nom", nullable = false, length = 80)
    private String nombre;

    @Column(name = "ant_apo_profesion", nullable = false, length = 30)
    private String profesion;

    @Column(name = "ant_apo_tel", nullable = false, length = 20)
    private String telefono;

    @Column(name = "ant_apo_dir", nullable = false, length = 100)
    private String direccion;

    @Column(name = "ant_apo_lug_trab", nullable = false, length = 100)
    private String lugarTrabajo;

    @Column(name = "ant_apo_disp_horaria", nullable = false, length = 1)
    private String disponibilidadHoraria;

    @ManyToOne
    @JoinColumn(name = "hve_id_hoja_vida", nullable = false)
    private HojaVidaEstudiante hojaVida;
}
