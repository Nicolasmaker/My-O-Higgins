package com.cahuinlabs.autenticacion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cahuinlabs.autenticacion.models.entities.geografia.Direccion;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Integer>{

    /*
    * Busca la dirección asociada a un usuario mediante su rut.
    * 
    * Spring Data JPA genera automáticamente la consulta
    * usando la relación:
    * Direccion -> Usuario -> usuRut
    * 
    * Retorna un Optional porque el usuario puede no tener dirección registrada.
    */
    Optional<Direccion> findByUsuarioUsuRut(Integer rut);
}
