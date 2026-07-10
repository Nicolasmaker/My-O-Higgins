package com.cahuinlabs.GestionReuniones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cahuinlabs.GestionReuniones.models.entities.BitReunionGeneral;

@Repository
public interface BitReunionGeneralRepository extends JpaRepository<BitReunionGeneral, Long> {
}
