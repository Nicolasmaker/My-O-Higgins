package com.myohiggins.msmensajeria.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myohiggins.msmensajeria.models.entities.Message;
import com.myohiggins.msmensajeria.models.request.MessageDTO;
import com.myohiggins.msmensajeria.repository.MessageRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    // RF-14: Enviar mensaje — fecha y estado leido se setean server-side
    public Message enviarMensaje(MessageDTO dto) {
        Message mensaje = new Message();
        mensaje.setRemitenteRut(dto.getRemitenteRut());
        mensaje.setDestinatarioRut(dto.getDestinatarioRut());
        mensaje.setAsunto(dto.getAsunto());
        mensaje.setContenido(dto.getContenido());
        mensaje.setFechaEnvio(LocalDate.now());
        mensaje.setEstadoLectura(false);
        return messageRepository.save(mensaje);
    }

    // RF-14: Ver mensajes recibidos por un usuario
    public List<Message> obtenerBandejaEntrada(Long destinatarioRut) {
        return messageRepository.findByDestinatarioRut(destinatarioRut);
    }

    // RF-14: Ver mensajes enviados por un usuario
    public List<Message> obtenerMensajesEnviados(Long remitenteRut) {
        return messageRepository.findByRemitenteRut(remitenteRut);
    }

    // RF-22: Marcar mensaje como leido
    public Message marcarComoLeido(Long idMensaje) {
        Message mensaje = messageRepository.findById(idMensaje)
                .orElseThrow(() -> new EntityNotFoundException("Mensaje no encontrado: " + idMensaje));
        mensaje.setEstadoLectura(true);
        return messageRepository.save(mensaje);
    }

    // RF-14: Eliminar mensajee
    public void eliminarMensaje(Long idMensaje) {
        if (!messageRepository.existsById(idMensaje)) {
            throw new EntityNotFoundException("Mensaje no encontrado: " + idMensaje);
        }
        messageRepository.deleteById(idMensaje);
    }
}
