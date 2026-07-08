package com.cahuinlabs.gestionAcademica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cahuinlabs.gestionAcademica.dto.ImpartirResponseDTO;
import com.cahuinlabs.gestionAcademica.models.entities.Impartir;
import com.cahuinlabs.gestionAcademica.models.request.Impartir.CrearImpartirRequest;
import com.cahuinlabs.gestionAcademica.service.ImpartirService;

import java.util.List;

@RestController
@RequestMapping("/impartir")
public class ImpartirController {

    @Autowired
    private ImpartirService impartirService;

    @PostMapping
    public ResponseEntity<Impartir> crear(@RequestBody CrearImpartirRequest request) {
        return ResponseEntity.ok(impartirService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<ImpartirResponseDTO>> listar() {
        return ResponseEntity.ok(impartirService.listarTodos());
    }

    @GetMapping("/docente/{rut}")
    public ResponseEntity<List<ImpartirResponseDTO>> listarPorDocente(@PathVariable Integer rut) {
        return ResponseEntity.ok(impartirService.listarPorDocente(rut));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (impartirService.buscarPorId(id).isPresent()) {
            impartirService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
