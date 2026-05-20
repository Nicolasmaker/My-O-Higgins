package com.cahuinlabs.GestionReuniones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cahuinlabs.GestionReuniones.models.entities.BitReunionIndividual;

@Repository
public interface BitReunionIndividualRepository extends JpaRepository<BitReunionIndividual, Long> {
}
