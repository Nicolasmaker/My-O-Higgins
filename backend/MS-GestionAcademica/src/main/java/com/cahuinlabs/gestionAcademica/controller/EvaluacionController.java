package com.cahuinlabs.gestionAcademica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cahuinlabs.gestionAcademica.models.entities.Evaluacion;
import com.cahuinlabs.gestionAcademica.models.request.Evaluacion.ActualizarEvaluacionRequest;
import com.cahuinlabs.gestionAcademica.models.request.Evaluacion.CrearEvaluacionRequest;
import com.cahuinlabs.gestionAcademica.service.EvaluacionService;

import java.util.List;

@RestController
@RequestMapping("/evaluacion")
public class EvaluacionController {

    @Autowired
    private EvaluacionService evaluacionService;

    @PostMapping
    public ResponseEntity<Evaluacion> crear(@RequestBody CrearEvaluacionRequest request) {
        return ResponseEntity.ok(evaluacionService.crearEvaluacion(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evaluacion> actualizar(@PathVariable Integer id, @RequestBody ActualizarEvaluacionRequest request) {
        return ResponseEntity.ok(evaluacionService.actualizarEvaluacion(id, request));
    }

    @GetMapping
    public ResponseEntity<List<Evaluacion>> listar() {
        return ResponseEntity.ok(evaluacionService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evaluacion> obtenerPorId(@PathVariable Integer id) {
        return evaluacionService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (evaluacionService.buscarPorId(id).isPresent()) {
            evaluacionService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
