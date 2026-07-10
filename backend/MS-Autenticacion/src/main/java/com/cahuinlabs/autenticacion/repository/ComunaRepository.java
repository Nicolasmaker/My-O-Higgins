package com.cahuinlabs.autenticacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cahuinlabs.autenticacion.models.entities.geografia.Comuna;

@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Integer>{

}
