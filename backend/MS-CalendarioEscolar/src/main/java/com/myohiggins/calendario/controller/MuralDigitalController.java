package com.myohiggins.calendario.controller;

import com.myohiggins.calendario.dto.MuralDigitalDTO;
import com.myohiggins.calendario.service.MuralDigitalService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST del Mural Digital.
 * Publicaciones/comunicados institucionales; los eventos del
 * calendario pueden asociarse a un mural via idMuralDigital.
 */
@RestController
@RequestMapping("/api/murales")
public class MuralDigitalController {

    private final MuralDigitalService service;

    public MuralDigitalController(MuralDigitalService service) {
        this.service = service;
    }

    /** POST /api/murales — crea una publicación */
    @PostMapping
    public ResponseEntity<MuralDigitalDTO> crear(@RequestBody MuralDigitalDTO dto) {
        return new ResponseEntity<>(service.crear(dto), HttpStatus.CREATED);
    }

    /** GET /api/murales — lista todas las publicaciones */
    @GetMapping
    public ResponseEntity<List<MuralDigitalDTO>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    /** GET /api/murales/{id} — obtiene una publicación por ID */
    @GetMapping("/{id}")
    public ResponseEntity<MuralDigitalDTO> obtenerPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    /** PUT /api/murales/{id} — corrige título/contenido (no autoría ni fecha) */
    @PutMapping("/{id}")
    public ResponseEntity<MuralDigitalDTO> actualizar(
            @PathVariable("id") Long id,
            @RequestBody MuralDigitalDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    /** DELETE /api/murales/{id} — elimina una publicación */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        service.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /** Publicación no encontrada → 404 con mensaje */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
