package com.cahuinlabs.GestionReuniones.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cahuinlabs.GestionReuniones.models.entities.BitReunionIndividual;

@Repository
public interface BitReunionIndividualRepository extends JpaRepository<BitReunionIndividual, Long> {
    boolean existsByBitReunionApoderado_IdBitReu(Long idBitReu);
    Optional<BitReunionIndividual> findByBitReunionApoderado_IdBitReu(Long idBitReu);
}
