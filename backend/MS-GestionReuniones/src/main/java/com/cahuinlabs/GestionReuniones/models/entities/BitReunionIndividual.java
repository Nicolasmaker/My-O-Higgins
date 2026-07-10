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
@Table(name = "bitacora_reunion_individual")
public class BitReunionIndividual {

   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bit_reu_individual", nullable = false)
    private Long idBitReuInd;

    // el motivo o razon por la que se llevo a cabo la reunion individual
    @Column(name = "bit_reu_ind_motivo_reunion", nullable = false, length = 100)
    private String bitReuIndMotivReu;

    // los temas que se trataron durante la reunion individual — parte de la bitácora
    // post-reunión, se llena con "Rellenar bitácora" tras aceptar, no al agendar.
    @Column(name = "bit_reu_ind_temas_tratados", length = 100)
    private String bitReuIndTemTrat;

    // estado de la firma del documento: 1 si está firmado, 0 si aún está pendiente
    @Column(name = "bit_reu_ind_firma_doc", nullable = false)
    private Integer bitReuIndFirmaDoc;

    // estado de la firma del apoderado, 1 si está firmado y 0 si aun esta pendiente
    @Column(name = "bit_reu_ind_firma_apo", nullable = false)
    private Integer bitReuIndFirmaApo;

    // referencia cruda (cross-microservicio) a la Anotacion que originó la citación, si aplica.
    // Nunca @ManyToOne: Anotaciones vive en otra base de datos (MS-Anotaciones).
    @Column(name = "id_anotacion")
    private Long idAnotacion;

    // la conexion con el apoderado es que cada reunion individual pertenece a un apoderado especifico
    @ToString.Exclude // sirve para ocultar datos del apoderado evirando problemas de rendimiento o de privacidad
    @EqualsAndHashCode.Exclude // no usar en comparaciones de igualdad para evitar problemas con datos no cargados
    @ManyToOne(fetch = FetchType.LAZY) // muchas reuniones pueden pertenecer a un apoderado y carga los datos solo cuando se necesita
    @JoinColumn(name = "id_bit_reu_apoderado", nullable = false) // columna que conecta con la tabla de apoderados
    private BitReunionApoderado bitReunionApoderado;
}
