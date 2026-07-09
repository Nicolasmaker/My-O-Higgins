package com.cahuinlabs.gestionAcademica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cahuinlabs.gestionAcademica.models.entities.Notas;
import java.util.List;

@Repository
public interface NotasRepository extends JpaRepository<Notas, Integer>{

    List<Notas> findByEstudianteUsuRut(Integer estudianteUsuRut);

    // Notas de una evaluación (para el ingreso masivo de notas por curso: precarga
    // las ya registradas y evita duplicar al reingresar el roster).
    List<Notas> findByEvaluacion_IdEva(Integer idEva);
}
