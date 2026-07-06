package com.cahuinlabs.gestionAcademica.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "impartir")
public class Impartir {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imp")
    private Integer idImp;

    //Referencia externa al Microservicio de Autenticacion
    @Column(name = "docente_usu_rut", nullable = false)
    private Integer docenteUsuRut;

    //Relaciones locales: que asignatura dicta el docente, en que curso
    @ManyToOne
    @JoinColumn(name = "asignatura_id_asi", nullable = false)
    private Asignatura asignatura;

    @ManyToOne
    @JoinColumn(name = "curso_id_cur", nullable = false)
    private Curso curso;
}
