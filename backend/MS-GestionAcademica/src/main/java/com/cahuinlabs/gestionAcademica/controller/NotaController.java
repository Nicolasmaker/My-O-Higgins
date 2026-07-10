package com.cahuinlabs.gestionAcademica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cahuinlabs.gestionAcademica.models.entities.Notas;
import com.cahuinlabs.gestionAcademica.models.request.Nota.ActualizarNotaRequest;
import com.cahuinlabs.gestionAcademica.models.request.Nota.CrearNotaRequest;
import com.cahuinlabs.gestionAcademica.service.NotaService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/notas")
public class NotaController {

    @Autowired
    private NotaService notasService;

    @PostMapping
    public ResponseEntity<Notas> crear(@Valid @RequestBody CrearNotaRequest request) {
        return ResponseEntity.ok(notasService.crearNota(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notas> actualizar(@PathVariable Integer id, @Valid @RequestBody ActualizarNotaRequest request) {
        return ResponseEntity.ok(notasService.actualizarNota(id, request));
    }

    @GetMapping
    public ResponseEntity<List<Notas>> listar() {
        return ResponseEntity.ok(notasService.listarTodas());
    }

    @GetMapping("/estudiante/{rut}")
    public ResponseEntity<List<Notas>> listarPorEstudiante(@PathVariable Integer rut) {
        return ResponseEntity.ok(notasService.listarPorEstudiante(rut));
    }

    @GetMapping("/evaluacion/{idEva}")
    public ResponseEntity<List<Notas>> listarPorEvaluacion(@PathVariable Integer idEva) {
        return ResponseEntity.ok(notasService.listarPorEvaluacion(idEva));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notas> obtenerPorId(@PathVariable Integer id) {
        return notasService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (notasService.buscarPorId(id).isPresent()) {
            notasService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
