package com.cahuinlabs.gestionAcademica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cahuinlabs.gestionAcademica.models.entities.Sala;
import com.cahuinlabs.gestionAcademica.models.request.SalaRequest;
import com.cahuinlabs.gestionAcademica.service.SalaService;
import java.util.List;

@RestController
@RequestMapping("/sala")
public class SalaController {

    @Autowired
    private SalaService salaService;

    @PostMapping
    public ResponseEntity<Sala> crear(@RequestBody SalaRequest salaRequest) {
        Sala nuevaSala = salaService.crearSala(salaRequest);
        return ResponseEntity.ok(nuevaSala);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sala> actualizar(@PathVariable Integer id, @RequestBody SalaRequest request) {
        Sala actualizada = salaService.actualizarSala(id, request);
        return ResponseEntity.ok(actualizada);
    }

    @GetMapping
    public ResponseEntity<List<Sala>> listar() {
        return ResponseEntity.ok(salaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sala> obtenerPorId(@PathVariable Integer id) {
        return salaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (salaService.buscarPorId(id).isPresent()) {
            salaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
