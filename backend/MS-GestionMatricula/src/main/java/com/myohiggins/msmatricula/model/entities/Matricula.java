package com.myohiggins.msmatricula.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "MATRICULA")
@Data
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mat")
    private Long idMatricula;

    @Column(name = "curso_id")
    private Long cursoId;

    @Column(name = "apoderado_rut")
    private Long apoderadoRut;

    @Column(name = "tipo_alumno", length = 20)
    private String tipoAlumno; // NUEVO, ANTIGUO, REPITENTE

    @Column(name = "mat_fec", nullable = false)
    private LocalDate matriculaFecha;

    @Column(name = "mat_est", nullable = false, length = 50)
    private String matriculaEstado;

    @Column(name = "mat_anio_acad", nullable = false)
    private Integer matriculaAnioAcademico;

    // Guardamos el RUT del alumno asociado a la matrícula
    @Column(name = "alu_rut", nullable = false)
    private Long alumnoRut;

    // Guardamos el RUT del funcionario/usuario que registra la matrícula
    @Column(name = "FUNCIONARIO_usu_rut", nullable = false)
    private Long funcionarioUsuRut;

    // Bloque automático que se ejecuta en MySQL justo antes de insertar el registro
    @PrePersist
    protected void onCreate() {
        this.matriculaFecha = LocalDate.now();
        this.matriculaAnioAcademico = LocalDate.now().getYear();
        if (this.matriculaEstado == null) {
            this.matriculaEstado = "ACTIVA"; // Estado inicial por defecto
        }
    }

public Matricula() {
    }
    // --- GETTERS Y SETTERS ---

    public String getMatriculaEstado() {
        return matriculaEstado;
    }

    public void setMatriculaEstado(String matriculaEstado) {
        this.matriculaEstado = matriculaEstado;
    }

    public Long getFuncionarioUsuRut() {
        return funcionarioUsuRut;
    }

    public void setFuncionarioUsuRut(Long funcionarioUsuRut) {
        this.funcionarioUsuRut = funcionarioUsuRut;
    }

    public Long getIdMatricula() {
        return idMatricula;
    }

    public void setIdMatricula(Long idMatricula) {
        this.idMatricula = idMatricula;
    }

    public Long getAlumnoRut() {
        return alumnoRut;
    }

    public void setAlumnoRut(Long alumnoRut) {
        this.alumnoRut = alumnoRut;
    }
    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }

    public Long getApoderadoRut() {
        return apoderadoRut;
    }

    public void setApoderadoRut(Long apoderadoRut) {
        this.apoderadoRut = apoderadoRut;
    }

    public String getTipoAlumno() {
        return tipoAlumno;
    }

    public void setTipoAlumno(String tipoAlumno) {
        this.tipoAlumno = tipoAlumno;
    }
}