package com.cahuinlabs.hojadevida.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cahuinlabs.hojadevida.dto.AntecedentesAcademicosDTO;
import com.cahuinlabs.hojadevida.service.AntecedentesAcademicosService;

@RestController
@RequestMapping("/api/antecedentes-academicos")
public class AntecedentesAcademicosController {

    private final AntecedentesAcademicosService service;

    public AntecedentesAcademicosController(AntecedentesAcademicosService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AntecedentesAcademicosDTO>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AntecedentesAcademicosDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<AntecedentesAcademicosDTO> crear(@RequestBody AntecedentesAcademicosDTO request) {
        AntecedentesAcademicosDTO creado = service.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AntecedentesAcademicosDTO> actualizar(@PathVariable Long id, @RequestBody AntecedentesAcademicosDTO request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
