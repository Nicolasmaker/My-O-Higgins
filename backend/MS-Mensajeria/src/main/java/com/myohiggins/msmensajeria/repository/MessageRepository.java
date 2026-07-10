package com.myohiggins.msmensajeria.repository;

import com.myohiggins.msmensajeria.models.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByDestinatarioRut(Long destinatarioRut);
    List<Message> findByRemitenteRut(Long remitenteRut);
}