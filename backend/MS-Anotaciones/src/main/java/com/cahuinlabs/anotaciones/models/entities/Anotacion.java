package com.cahuinlabs.anotaciones.models.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "anotaciones")
public class Anotacion 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_anot", nullable = false)
    private Long idAnot;

    // tipo de anotacion positiva/negativa
    @Column(name = "anot_tip", nullable = false, length = 15)
    private String anotTip;

    // gravedad (Leve/Grave/Muy Grave), solo aplica cuando anotTip = Negativa
    @Column(name = "anot_gravedad", length = 15)
    private String anotGravedad;

    // descripcion de la observacion, logro, falta...
    @Column(name = "anot_des", nullable = false, length = 1000)
    private String anotDes;

    // fecha en la que se realiza la anotacion
    @Column(name = "anot_fec", nullable = false)
    private LocalDate anotFec;

    // FK hacia la tabla funcionario (Docente o Inspector)
    @Column(name = "funcionario_usu_rut", nullable = false)
    private Long funcionarioUsuRut;

    // FK hacia la tabla Hoja de Vida Estudiante
    // el RF11 indica que se copia automaticamente a la hoja de vida
    @Column(name = "id_hoja_vida", nullable = false)
    private Long idHojaVida;
}
