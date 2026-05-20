package com.cahuinlabs.hojadevida.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cahuinlabs.hojadevida.model.AntecedentesAcademicos;

@Repository
public interface AntecedentesAcademicosRepository extends JpaRepository<AntecedentesAcademicos, Long> {
}
