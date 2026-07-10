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
@Table(name = "asistencia")
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asis")
    private Integer idAsis;

    @Column(name = "asis_fecha", nullable = false)
    private LocalDate asisFecha;

    // PRESENTE | AUSENTE | ATRASADO | JUSTIFICADO
    @Column(name = "asis_estado", nullable = false, length = 15)
    private String asisEstado;

    // Referencia externa al Microservicio de Autenticacion
    @Column(name = "estudiante_usu_rut", nullable = false)
    private Integer estudianteUsuRut;

    // Relacion local: de que asignatura/curso/docente es esta asistencia (asistencia por
    // asignatura/hora especifica, no una asistencia general del dia).
    @ManyToOne
    @JoinColumn(name = "impartir_id_imp", nullable = false)
    private Impartir impartir;
}
