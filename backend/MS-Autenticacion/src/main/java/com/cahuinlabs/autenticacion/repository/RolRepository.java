package com.cahuinlabs.autenticacion.repository;

import org.springframework.stereotype.Repository;
import com.cahuinlabs.autenticacion.models.entities.usuarios.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer>{

    void deleteByUsuarioUsuRut(Integer usuRut);

}
