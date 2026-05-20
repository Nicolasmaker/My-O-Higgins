package com.myohiggins.calendario.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Entidad que mapea la tabla CALENDARIO_ESTUDIANTIL de la base de datos.
 * Utiliza Lombok (@Data, @NoArgsConstructor, @AllArgsConstructor) para
 * generar automáticamente getters, setters y constructores.
 */
@Entity
@Table(name = "CALENDARIO_ESTUDIANTIL")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarioEstudiantil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cal_est")
    private Long idCalEst;

    @Column(name = "cal_est_tit_eve", length = 100, nullable = false)
    private String tituloEvento;

    @Column(name = "cal_est_tip_eve", length = 100, nullable = false)
    private String tipoEvento;

    @Column(name = "cal_est_fec_ini", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;

    @Column(name = "cal_est_fec_fin", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaFin;

    // Se guardan las FKs como campos simples ya que son IDs a otros MS o módulos
    @Column(name = "MURAL_DIGITAL_id_mur_dig", nullable = false)
    private Long idMuralDigital;

    @Column(name = "ASIGNATURA_id_asi", nullable = false)
    private Long idAsignatura;

    @Column(name = "cal_est_des_eve", length = 300)
    private String descripcionEvento;
}
