package com.cahuinlabs.hojadevida.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cahuinlabs.hojadevida.model.HojaVidaEstudiante;

@Repository
public interface HojaVidaRepository extends JpaRepository<HojaVidaEstudiante, Long> {
    // Se deja el repositorio simple para que el servicio concentre la lógica de negocio.
}