package com.myohiggins.calendario.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * Entidad que mapea la tabla MURAL_DIGITAL de la base de datos.
 * Un mural agrupa publicaciones/comunicados visibles para la comunidad;
 * los eventos del calendario pueden referenciarlo mediante
 * CALENDARIO_ESTUDIANTIL.MURAL_DIGITAL_id_mur_dig.
 */
@Entity
@Table(name = "MURAL_DIGITAL")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MuralDigital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mur_dig")
    private Long idMurDig;

    @Column(name = "mur_dig_titulo", length = 100, nullable = false)
    private String titulo;

    @Column(name = "mur_dig_contenido", length = 1000, nullable = false)
    private String contenido;

    @Column(name = "mur_dig_fec_pub", nullable = false)
    private LocalDate fechaPublicacion;

    // RUT del funcionario que publica (referencia plana a MS-Autenticacion)
    @Column(name = "FUNCIONARIO_usu_rut", nullable = false)
    private Long funcionarioUsuRut;

    // Fecha de publicación server-side si el cliente no la envía
    @PrePersist
    protected void onCreate() {
        if (this.fechaPublicacion == null) {
            this.fechaPublicacion = LocalDate.now();
        }
    }
}
