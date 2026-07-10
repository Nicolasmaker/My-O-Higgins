package com.cahuinlabs.GestionReuniones.models.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "bitacora_reuniones_apoderados")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BitReunionApoderado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bit_reu", nullable = false)
    private Long idBitReu;

    @Column(name = "bit_reu_fec", nullable = false)
    private LocalDate bitReuFec;

    // Bitácora post-reunión: se llena recién con "Rellenar bitácora" tras aceptar, no al agendar.
    @Column(name = "bit_reu_compromisos", length = 1000)
    private String bitReuCompromisos;

    @Column(name = "bit_reu_obs", nullable = true, length = 300)
    private String bitReuObs;

    // identificador del Docente o Inspector que registra la reunión
    @Column(name = "docente_usu_rut", nullable = false)
    private Long docenteUsuRut;

    // RUT del apoderado citado (nullable: las reuniones "General" son de curso completo, no por-apoderado)
    @Column(name = "apoderado_usu_rut")
    private Long apoderadoUsuRut;

    // RUT del alumno al que corresponde la reunión (Individual — un apoderado puede tener varios hijos;
    // necesario para que el estudiante también vea la reunión reflejada en su calendario)
    @Column(name = "alumno_rut")
    private Long alumnoRut;

    // PENDIENTE / ACEPTADA / RECHAZADA — respuesta del apoderado a la citación
    @Column(name = "estado_confirmacion", length = 20)
    private String estadoConfirmacion;

    // Id del evento sincronizado en MS-CalendarioEscolar, creado recien al aceptar (no al crear)
    @Column(name = "id_cal_est")
    private Long idCalEst;
}