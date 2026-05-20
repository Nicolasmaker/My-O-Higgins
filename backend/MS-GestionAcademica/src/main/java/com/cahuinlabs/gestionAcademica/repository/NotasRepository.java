package com.cahuinlabs.gestionAcademica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cahuinlabs.gestionAcademica.models.entities.Notas;

@Repository
public interface NotasRepository extends JpaRepository<Notas, Integer>{

}
