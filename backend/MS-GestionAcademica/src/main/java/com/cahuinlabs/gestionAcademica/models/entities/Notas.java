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
@Table(name = "notas")
public class Notas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_not")
    private Integer idNot;

    @Column(name = "not_calif", nullable = false)
    private Double notCalif; 

    @Column(name = "not_fecha_registrada", nullable = false)
    private LocalDate notFechaRegistrada;

    @ManyToOne
    @JoinColumn(name = "evaluacion_id_eva", nullable = false)
    private Evaluacion evaluacion;

    @Column(name = "estudiante_usu_rut", nullable = false)
    private Integer estudianteUsuRut;
}
