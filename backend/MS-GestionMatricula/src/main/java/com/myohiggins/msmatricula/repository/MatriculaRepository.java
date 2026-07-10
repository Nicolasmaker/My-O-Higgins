package com.myohiggins.msmatricula.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myohiggins.msmatricula.model.entities.Matricula;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    
}