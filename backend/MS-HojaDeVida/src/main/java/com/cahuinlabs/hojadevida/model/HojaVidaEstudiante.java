package com.cahuinlabs.hojadevida.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "HOJA_VIDA_ESTUDIANTE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HojaVidaEstudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hoja_vida")
    private Long idHojaVida;

    @Column(name = "ESTUDIANTE_usu_rut", nullable = false)
    private Long estudianteUsuRut;

    @Column(name = "MATRICULA_id_mat", nullable = false)
    private Long matriculaId;

    // Estado general del estudiante (Incorporado/Retirado/Suspendido). Editable solo por Directivo.
    @Column(name = "estado", length = 20)
    private String estado;
}