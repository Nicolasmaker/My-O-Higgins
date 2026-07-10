package com.cahuinlabs.anotaciones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cahuinlabs.anotaciones.models.entities.Anotacion;

@Repository
public interface AnotacionRepository extends JpaRepository<Anotacion, Long> {
    // Permite consultar el historial de anotaciones de un estudiante en particular
    List<Anotacion> findByIdHojaVida(Long idHojaVida);
}
