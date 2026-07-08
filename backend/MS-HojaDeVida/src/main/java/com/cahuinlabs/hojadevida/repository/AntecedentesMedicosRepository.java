package com.cahuinlabs.hojadevida.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cahuinlabs.hojadevida.model.AntecedentesMedicos;

@Repository
public interface AntecedentesMedicosRepository extends JpaRepository<AntecedentesMedicos, Long> {
    List<AntecedentesMedicos> findByHojaVida_IdHojaVida(Long idHojaVida);
}
