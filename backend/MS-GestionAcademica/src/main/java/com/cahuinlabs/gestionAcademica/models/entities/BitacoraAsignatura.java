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
@Table(name = "bitacora_asignatura")
public class BitacoraAsignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bit_asi")
    private Integer idBitAsi;

    @Column(name = "bit_asi_fecha_clase", nullable = false)
    private LocalDate bitAsiFechaClase;

    @Column(name = "bit_asi_actividades", nullable = false, length = 1000)
    private String bitAsiActividades;

    @Column(name = "bit_asi_contenidos", nullable = false, length = 1000)
    private String bitAsiContenidos;

    @Column(name = "bit_asi_obs", nullable = false, length = 1000)
    private String bitAsiObs;

    @Column(name = "bit_asi_obj_apren", length = 500)
    private String bitAsiObjApren;

    @ManyToOne
    @JoinColumn(name = "asignatura_id_asi", nullable = false)
    private Asignatura asignatura;
}
