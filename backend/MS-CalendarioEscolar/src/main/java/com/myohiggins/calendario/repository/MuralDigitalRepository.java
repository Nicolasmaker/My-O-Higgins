package com.myohiggins.calendario.repository;

import com.myohiggins.calendario.entity.MuralDigital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MuralDigitalRepository extends JpaRepository<MuralDigital, Long> {
}
