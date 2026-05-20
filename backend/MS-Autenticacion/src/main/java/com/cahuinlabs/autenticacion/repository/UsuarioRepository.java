package com.cahuinlabs.autenticacion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cahuinlabs.autenticacion.models.entities.usuarios.Usuario;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{

 //Metodo para buscar por email
    Optional<Usuario> findByUsuEmail(String email);
}
