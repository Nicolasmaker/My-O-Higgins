package com.cahuinlabs.GestionReuniones.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "bitacora_reunion_general")
public class BitReunionGeneral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bit_reu_general", nullable = false)
    private Long bitReuGen;

    // el tipo de reunion que fue, por ejemplo ordinaria, extraordinaria
    @Column(name = "bit_reu_gen_tipo_reunion", nullable = false, length = 30)
    private String bitReuGenTipReu;

    // lo que se comunico en la reunion 
    @Column(name = "bit_reu_gen_comunicado_emitido", nullable = false, length = 200)
    private String bitReuGenComunicEmi;

    // los acuerdos y decisiones que se tomaron durante la reunion
    @Column(name = "bit_reu_gen_acuerdos_tratados", nullable = false, length = 200)
    private String bitReuGenAcuerTrat;

    // campo opcional para notas adicionales o comentarios especiales
    @Column(name = "bit_reu_gen_obs", nullable = true, length = 300)
    private String bitReuGenObs;

    // conexion con el apoderado cada reunión general pertenece a un apoderado específico
    @ToString.Exclude // sirve para ocultar datos del apoderado evirando problemas de rendimiento o de privacidad
    @EqualsAndHashCode.Exclude // no usar en comparaciones de igualdad para evitar problemas con datos no cargados
    @ManyToOne(fetch = FetchType.LAZY) // muchas reuniones pueden pertenecer a un apoderado y carga los datos solo cuando se necesita
    @JoinColumn(name = "id_bit_reu_apoderado", nullable = false) // columna que conecta con la tabla de apoderados
    private BitReunionApoderado bitReunionApoderado;
}