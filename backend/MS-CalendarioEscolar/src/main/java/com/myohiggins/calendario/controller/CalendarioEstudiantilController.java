package com.myohiggins.calendario.controller;

import com.myohiggins.calendario.dto.CalendarioEstudiantilDTO;
import com.myohiggins.calendario.service.CalendarioEstudiantilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para el Calendario Estudiantil.
 * Define las rutas (endpoints) que van a recibir peticiones HTTP.
 */
@RestController
@RequestMapping("/api/calendarios")
public class CalendarioEstudiantilController {

    private final CalendarioEstudiantilService service;

    public CalendarioEstudiantilController(CalendarioEstudiantilService service) {
        this.service = service;
    }

    /**
     * POST /api/calendarios
     * Crea un nuevo evento en el calendario.
     */
    @PostMapping
    public ResponseEntity<CalendarioEstudiantilDTO> crear(@RequestBody CalendarioEstudiantilDTO dto) {
        CalendarioEstudiantilDTO nuevoCalendario = service.crear(dto);
        return new ResponseEntity<>(nuevoCalendario, HttpStatus.CREATED);
    }

    /**
     * GET /api/calendarios
     * Obtiene una lista de todos los eventos del calendario.
     */
    @GetMapping
    public ResponseEntity<List<CalendarioEstudiantilDTO>> obtenerTodos() {
        List<CalendarioEstudiantilDTO> lista = service.obtenerTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    /**
     * GET /api/calendarios/{id}
     * Obtiene un evento específico por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CalendarioEstudiantilDTO> obtenerPorId(@PathVariable("id") Long id) {
        CalendarioEstudiantilDTO evento = service.obtenerPorId(id);
        return new ResponseEntity<>(evento, HttpStatus.OK);
    }

    /**
     * PUT /api/calendarios/{id}
     * Actualiza la información de un evento existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CalendarioEstudiantilDTO> actualizar(
            @PathVariable("id") Long id, 
            @RequestBody CalendarioEstudiantilDTO dto) {
        CalendarioEstudiantilDTO actualizado = service.actualizar(id, dto);
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }

    /**
     * DELETE /api/calendarios/{id}
     * Elimina un evento del calendario por su ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        service.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
