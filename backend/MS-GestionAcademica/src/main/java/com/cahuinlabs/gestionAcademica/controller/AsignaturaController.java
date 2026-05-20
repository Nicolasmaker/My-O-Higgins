package com.cahuinlabs.gestionAcademica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cahuinlabs.gestionAcademica.models.entities.Asignatura;
import com.cahuinlabs.gestionAcademica.models.request.AsignaturaRequest;
import com.cahuinlabs.gestionAcademica.service.AsignaturaService;
import java.util.List;

@RestController
@RequestMapping("/asignatura")
public class AsignaturaController {

    @Autowired
    private AsignaturaService asignaturaService;

 //CREAR ASIGNATURA 
    @PostMapping
    public ResponseEntity<Asignatura> crear(@RequestBody AsignaturaRequest asignaturaRequest) {
        Asignatura nuevaAsignatura = asignaturaService.crearAsignatura(asignaturaRequest);
        return ResponseEntity.ok(nuevaAsignatura);
    }

 //ACTUALIZAR ASIGNATURA
    @PutMapping("/{id}")
    public ResponseEntity<Asignatura> actualizar(@PathVariable Integer id, @RequestBody AsignaturaRequest request) {
        Asignatura actualizada = asignaturaService.actualizarAsignatura(id, request);
        return ResponseEntity.ok(actualizada);
    }

 //OBTENER TODAS 
    @GetMapping
    public ResponseEntity<List<Asignatura>> listar() {
        return ResponseEntity.ok(asignaturaService.listarTodas());
    }

 //OBTENER UNA POR ID 
    @GetMapping("/{id}")
    public ResponseEntity<Asignatura> obtenerPorId(@PathVariable Integer id) {
        return asignaturaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

 //ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (asignaturaService.buscarPorId(id).isPresent()) {
            asignaturaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
