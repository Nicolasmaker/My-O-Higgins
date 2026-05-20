package com.cahuinlabs.gestionAcademica.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "evaluacion")
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_eva")
    private Integer idEva;

    @Column(name = "eva_nombre", nullable = false, length = 100)
    private String evaNom;

    @Column(name = "eva_fecha", nullable = false)
    private LocalDate evaFecha;

    @Column(name = "eva_periodo_acad", nullable = false, length = 20)
    private String evaPeriodoAcad;

    @Column(name = "eva_tipo", nullable = false, length = 15)
    private String evaTipo;

    //Referencia externa al Microservicio de Autenticacion
    @Column(name = "docente_usu_rut", nullable = false)
    private Integer docenteUsuRut;

    //Relacion local
    @ManyToOne
    @JoinColumn(name = "asignatura_id_asi", nullable = false)
    private Asignatura asignatura;
}
