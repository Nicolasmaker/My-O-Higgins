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
@Table(name = "ANTECEDENTES_MEDICOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AntecedentesMedicos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ant_med")
    private Long idAntMed;

    @Column(name = "ant_med_alergias", nullable = false, length = 100)
    private String alergias;

    @Column(name = "ant_med_condi_med", nullable = false, length = 1000)
    private String condicionesMedicas;

    @Column(name = "ant_med_medicamentos", nullable = false, length = 100)
    private String medicamentos;

    @Column(name = "ant_med_tip_sang", nullable = false, length = 10)
    private String tipoSangre;

    @Column(name = "ant_med_obs", length = 500)
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "hve_id_hoja_vida", nullable = false)
    private HojaVidaEstudiante hojaVida;
}
