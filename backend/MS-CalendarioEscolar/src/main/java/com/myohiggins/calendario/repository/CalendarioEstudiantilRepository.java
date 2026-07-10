package com.myohiggins.calendario.repository;

import com.myohiggins.calendario.entity.CalendarioEstudiantil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Spring Data JPA para la entidad CalendarioEstudiantil.
 * Al extender de JpaRepository hereda automáticamente métodos como 
 * save(), findById(), findAll(), deleteById(), sin necesidad de implementarlos.
 */
@Repository
public interface CalendarioEstudiantilRepository extends JpaRepository<CalendarioEstudiantil, Long> {
}
