package com.myohiggins.msmatricula.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myohiggins.msmatricula.model.entities.SolicitudMatricula;

@Repository
public interface SolicitudMatriculaRepository extends JpaRepository<SolicitudMatricula, Long> {
    List<SolicitudMatricula> findByApoderadoRut(Long apoderadoRut);
}
