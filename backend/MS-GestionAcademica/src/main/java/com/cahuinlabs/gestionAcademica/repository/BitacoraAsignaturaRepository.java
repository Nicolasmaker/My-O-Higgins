package com.cahuinlabs.gestionAcademica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cahuinlabs.gestionAcademica.models.entities.BitacoraAsignatura;

@Repository
public interface BitacoraAsignaturaRepository extends JpaRepository<BitacoraAsignatura, Integer>{

}
