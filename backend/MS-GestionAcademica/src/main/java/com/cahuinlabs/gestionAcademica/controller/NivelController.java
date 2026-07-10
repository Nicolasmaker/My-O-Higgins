package com.cahuinlabs.gestionAcademica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cahuinlabs.gestionAcademica.models.entities.Nivel;
import com.cahuinlabs.gestionAcademica.models.request.NivelRequest;
import com.cahuinlabs.gestionAcademica.service.NivelService;
import java.util.List;

@RestController
@RequestMapping("/nivel")
public class NivelController {

    @Autowired
    private NivelService nivelService;

    @PostMapping
    public ResponseEntity<Nivel> crear(@RequestBody NivelRequest nivelRequest) {
        Nivel nuevoNivel = nivelService.crearNivel(nivelRequest);
        return ResponseEntity.ok(nuevoNivel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Nivel> actualizar(@PathVariable Integer id, @RequestBody NivelRequest request) {
        Nivel actualizado = nivelService.actualizarNivel(id, request);
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping
    public ResponseEntity<List<Nivel>> listar() {
        return ResponseEntity.ok(nivelService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nivel> obtenerPorId(@PathVariable Integer id) {
        return nivelService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (nivelService.buscarPorId(id).isPresent()) {
            nivelService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
