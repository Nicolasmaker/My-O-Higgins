package com.cahuinlabs.GestionReuniones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cahuinlabs.GestionReuniones.models.entities.BitReunionApoderado;

@Repository
public interface BitReunionApoderadoRepository extends JpaRepository<BitReunionApoderado, Long> {
    // Permite filtrar el historial de reuniones por el funcionario que las dictó
    List<BitReunionApoderado> findByDocenteUsuRut(Long docenteUsuRut);
}
