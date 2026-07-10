package com.cahuinlabs.hojadevida.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cahuinlabs.hojadevida.dto.AntecedentesMedicosDTO;
import com.cahuinlabs.hojadevida.service.AntecedentesMedicosService;

@RestController
@RequestMapping("/api/antecedentes-medicos")
public class AntecedentesMedicosController {

    private final AntecedentesMedicosService service;

    public AntecedentesMedicosController(AntecedentesMedicosService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AntecedentesMedicosDTO>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AntecedentesMedicosDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<AntecedentesMedicosDTO> crear(@RequestBody AntecedentesMedicosDTO request) {
        AntecedentesMedicosDTO creado = service.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AntecedentesMedicosDTO> actualizar(@PathVariable Long id, @RequestBody AntecedentesMedicosDTO request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
