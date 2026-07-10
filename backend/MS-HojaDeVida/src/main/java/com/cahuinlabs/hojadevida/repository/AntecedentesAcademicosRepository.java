package com.cahuinlabs.hojadevida.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cahuinlabs.hojadevida.model.AntecedentesAcademicos;

@Repository
public interface AntecedentesAcademicosRepository extends JpaRepository<AntecedentesAcademicos, Long> {
    List<AntecedentesAcademicos> findByHojaVida_IdHojaVida(Long idHojaVida);
}
