package com.cahuinlabs.gestionAcademica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cahuinlabs.gestionAcademica.models.entities.Impartir;

@Repository
public interface ImpartirRepository extends JpaRepository<Impartir, Integer> {

    List<Impartir> findByDocenteUsuRut(Integer docenteUsuRut);
}
