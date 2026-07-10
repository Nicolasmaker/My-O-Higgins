package com.myohiggins.msmensajeria.models.entities;

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
@Table(name = "mensajeria")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje", nullable = false)
    private Long idMensaje;

    // RUT del usuario que envía el mensaje (FK a Usuario)
    @Column(name = "remitente_rut", nullable = false)
    private Long remitenteRut;

    // RUT del usuario que recibe el mensaje (FK a Usuario)
    @Column(name = "destinatario_rut", nullable = false)
    private Long destinatarioRut;

    @Column(name = "men_asunto", nullable = false, length = 200)
    private String asunto;

    @Column(name = "men_contenido", nullable = false, length = 2000)
    private String contenido;

    // Se setea server-side con LocalDate.now() — no viene del cliente
    @Column(name = "men_fecha_envio", nullable = false)
    private LocalDate fechaEnvio;

    // RF-22: seguimiento de lectura
    @Column(name = "men_estado_lectura", nullable = false)
    private Boolean estadoLectura;
}
