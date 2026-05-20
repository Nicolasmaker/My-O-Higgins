package com.cahuinlabs.hojadevida.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cahuinlabs.hojadevida.dto.AntecedentesApoderadoDTO;
import com.cahuinlabs.hojadevida.service.AntecedentesApoderadoService;

@RestController
@RequestMapping("/api/antecedentes-apoderado")
public class AntecedentesApoderadoController {

    private final AntecedentesApoderadoService service;

    public AntecedentesApoderadoController(AntecedentesApoderadoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AntecedentesApoderadoDTO>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AntecedentesApoderadoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<AntecedentesApoderadoDTO> crear(@RequestBody AntecedentesApoderadoDTO request) {
        AntecedentesApoderadoDTO creado = service.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AntecedentesApoderadoDTO> actualizar(@PathVariable Long id, @RequestBody AntecedentesApoderadoDTO request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
