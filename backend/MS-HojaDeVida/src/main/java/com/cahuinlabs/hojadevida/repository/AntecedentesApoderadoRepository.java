package com.cahuinlabs.hojadevida.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cahuinlabs.hojadevida.model.AntecedentesApoderado;

@Repository
public interface AntecedentesApoderadoRepository extends JpaRepository<AntecedentesApoderado, Long> {
}
