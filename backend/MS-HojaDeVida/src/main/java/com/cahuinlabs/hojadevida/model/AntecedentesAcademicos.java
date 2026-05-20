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
@Table(name = "ANTECEDENTES_ACADEMICOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AntecedentesAcademicos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ant_acad")
    private Long idAntAcad;

    @Column(name = "ant_acad_anio_esc", nullable = false)
    private Integer anioEscolar;

    @Column(name = "ant_acad_prom_gen_act", nullable = false)
    private Double promedioGeneralActual;

    @Column(name = "ant_acad_sit_fin_aprob", nullable = false, length = 1)
    private String situacionFinalAprobacion; // 'S' o 'N' o char equivalente

    @ManyToOne
    @JoinColumn(name = "hve_id_hoja_vida", nullable = false)
    private HojaVidaEstudiante hojaVida;
}
