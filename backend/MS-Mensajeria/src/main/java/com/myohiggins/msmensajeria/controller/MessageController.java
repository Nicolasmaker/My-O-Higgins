package com.myohiggins.msmensajeria.controller;

import com.myohiggins.msmensajeria.models.entities.Message;
import com.myohiggins.msmensajeria.models.request.MessageDTO;
import com.myohiggins.msmensajeria.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mensajeria")
public class MessageController {

    @Autowired
    private MessageService messageService;

    // 1. Enviar mensaje (POST)
    @PostMapping("/enviar")
    public ResponseEntity<?> enviarMensaje(@RequestBody MessageDTO nuevoMensaje) {
        if (nuevoMensaje.getRemitenteRut() == null || nuevoMensaje.getDestinatarioRut() == null
                || nuevoMensaje.getAsunto() == null || nuevoMensaje.getContenido() == null) {
            return ResponseEntity.badRequest().body("Error: Todos los campos son obligatorios");
        }

        Message mensajeGuardado = messageService.enviarMensaje(nuevoMensaje);
        return ResponseEntity.ok(mensajeGuardado);
    }

    // 2. Leer bandeja (GET)
    @GetMapping("/bandeja/{destinatarioRut}")
    public ResponseEntity<List<Message>> obtenerBandeja(@PathVariable Long destinatarioRut) {
        return ResponseEntity.ok(messageService.obtenerBandejaEntrada(destinatarioRut));
    }

    // 3. Leer enviados (GET)
    @GetMapping("/enviados/{remitenteRut}")
    public ResponseEntity<List<Message>> obtenerMensajesEnviados(@PathVariable Long remitenteRut) {
        return ResponseEntity.ok(messageService.obtenerMensajesEnviados(remitenteRut));
    }

    // 4. Marcar mensaje como leído (RF-22)
    @PatchMapping("/{id}/leido")
    public ResponseEntity<Message> marcarComoLeido(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.marcarComoLeido(id));
    }

    // 5. Eliminar mensaje
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMensaje(@PathVariable Long id) {
        messageService.eliminarMensaje(id);
        return ResponseEntity.noContent().build();
    }
}