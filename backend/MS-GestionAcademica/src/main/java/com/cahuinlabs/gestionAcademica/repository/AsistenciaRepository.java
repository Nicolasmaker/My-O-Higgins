package com.cahuinlabs.gestionAcademica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cahuinlabs.gestionAcademica.models.entities.Asistencia;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Integer> {

    List<Asistencia> findByEstudianteUsuRut(Integer estudianteUsuRut);

    // Roster de una asignatura/curso en una fecha puntual — usado por "pasar lista" para saber
    // qué estudiantes ya tienen asistencia registrada ese día (evita duplicados).
    List<Asistencia> findByImpartir_IdImpAndAsisFecha(Integer idImpartir, LocalDate asisFecha);
}
