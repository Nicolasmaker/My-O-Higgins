package com.myohiggins.msmatricula.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "SOLICITUD_MATRICULA")
@Data
public class SolicitudMatricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sol")
    private Long idSolicitud;

    @Column(name = "alu_rut", nullable = false)
    private Long alumnoRut;

    @Column(name = "apoderado_rut", nullable = false)
    private Long apoderadoRut;

    // Curso deseado por el apoderado; el Directivo puede ajustarlo al aprobar
    @Column(name = "curso_id")
    private Long cursoId;

    @Column(name = "tipo_alumno", length = 20)
    private String tipoAlumno; // NUEVO, ANTIGUO, REPITENTE

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado; // PENDIENTE, APROBADA, RECHAZADA

    @Column(name = "fecha_sol", nullable = false)
    private LocalDate fechaSolicitud;

    @Column(name = "motivo_rechazo", length = 500)
    private String motivoRechazo;

    @PrePersist
    protected void onCreate() {
        this.fechaSolicitud = LocalDate.now();
        if (this.estado == null) {
            this.estado = "PENDIENTE";
        }
    }
}
