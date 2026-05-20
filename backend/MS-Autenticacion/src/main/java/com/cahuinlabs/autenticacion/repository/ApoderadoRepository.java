package com.cahuinlabs.autenticacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cahuinlabs.autenticacion.models.entities.usuarios.Apoderado;

@Repository
public interface ApoderadoRepository extends JpaRepository<Apoderado, Integer> {

}
