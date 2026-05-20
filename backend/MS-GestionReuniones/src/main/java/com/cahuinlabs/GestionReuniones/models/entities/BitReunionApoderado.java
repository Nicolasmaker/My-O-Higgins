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

    @Column(name = "bit_reu_compromisos", nullable = false, length = 1000)
    private String bitReuCompromisos;

    @Column(name = "bit_reu_obs", nullable = true, length = 300)
    private String bitReuObs;

    // identificador del Docente o Inspector que registra la reunión
    @Column(name = "docente_usu_rut", nullable = false)
    private Long docenteUsuRut;
}