package com.cahuinlabs.hojadevida.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cahuinlabs.hojadevida.model.HojaVidaEstudiante;

@Repository
public interface HojaVidaRepository extends JpaRepository<HojaVidaEstudiante, Long> {
    // Se deja el repositorio simple para que el servicio concentre la lógica de negocio.

    // Permite resolver la hoja de vida de un estudiante a partir de su RUT
    Optional<HojaVidaEstudiante> findByEstudianteUsuRut(Long estudianteUsuRut);
}