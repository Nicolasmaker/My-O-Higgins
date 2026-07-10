package com.myohiggins.msmatricula.controller;

import com.myohiggins.msmatricula.dto.MatriculaResponseDTO;
import com.myohiggins.msmatricula.model.entities.Matricula;
import com.myohiggins.msmatricula.service.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;


    @PostMapping
    public ResponseEntity<Matricula> crearMatricula(@RequestBody Matricula matricula) {
        Matricula nuevaMatricula = matriculaService.crearMatricula(matricula);
        return new ResponseEntity<>(nuevaMatricula, HttpStatus.CREATED);
    }

    
    @GetMapping
    public ResponseEntity<List<MatriculaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(matriculaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaResponseDTO> obtenerPorId(@PathVariable Long id) {
        MatriculaResponseDTO matricula = matriculaService.buscarPorId(id);
        if (matricula != null) {
            return ResponseEntity.ok(matricula);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint para ACTUALIZAR una matrícula (PUT http://localhost:8081/api/matriculas/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Matricula> actualizarMatricula(@PathVariable Long id, @RequestBody Matricula detallesMatricula) {
        Matricula matriculaActualizada = matriculaService.actualizarMatricula(id, detallesMatricula);
        if (matriculaActualizada != null) {
            return ResponseEntity.ok(matriculaActualizada);
        } else {
            return ResponseEntity.notFound().build(); // Devuelve error 404 si no encuentra el ID
        }
    }

    // Endpoint para ELIMINAR una matrícula (DELETE http://localhost:8081/api/matriculas/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMatricula(@PathVariable Long id) {
        matriculaService.eliminarMatricula(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content cuando se borra con éxito
    }

    // Manejador de excepciones: validaciones de negocio (alumno/apoderado/funcionario
    // inexistente en MS-Autenticacion) devuelven 400 con el mensaje, no un 500 crudo
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleValidacion(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // Errores de comunicación con otros microservicios devuelven 503
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());
    }
}