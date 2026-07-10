package com.cahuinlabs.hojadevida.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cahuinlabs.hojadevida.model.AntecedentesApoderado;

@Repository
public interface AntecedentesApoderadoRepository extends JpaRepository<AntecedentesApoderado, Long> {
    List<AntecedentesApoderado> findByHojaVida_IdHojaVida(Long idHojaVida);
}
