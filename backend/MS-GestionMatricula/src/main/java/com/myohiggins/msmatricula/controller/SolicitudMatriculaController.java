package com.myohiggins.msmatricula.controller;

import com.myohiggins.msmatricula.dto.SolicitudMatriculaResponseDTO;
import com.myohiggins.msmatricula.model.entities.Matricula;
import com.myohiggins.msmatricula.model.entities.SolicitudMatricula;
import com.myohiggins.msmatricula.service.SolicitudMatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/matriculas/solicitudes")
public class SolicitudMatriculaController {

    @Autowired
    private SolicitudMatriculaService solicitudService;

    @PostMapping
    public ResponseEntity<SolicitudMatricula> crearSolicitud(@RequestBody SolicitudMatricula solicitud) {
        SolicitudMatricula creada = solicitudService.crearSolicitud(solicitud);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SolicitudMatriculaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(solicitudService.listarTodas());
    }

    @GetMapping("/apoderado/{rut}")
    public ResponseEntity<List<SolicitudMatriculaResponseDTO>> listarPorApoderado(@PathVariable Long rut) {
        return ResponseEntity.ok(solicitudService.listarPorApoderado(rut));
    }

    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<Matricula> aprobarSolicitud(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        Long funcionarioUsuRut = body.get("funcionarioUsuRut");
        Long cursoId = body.get("cursoId");
        return ResponseEntity.ok(solicitudService.aprobarSolicitud(id, funcionarioUsuRut, cursoId));
    }

    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<SolicitudMatricula> rechazarSolicitud(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(solicitudService.rechazarSolicitud(id, body.get("motivoRechazo")));
    }

    // Manejador de excepciones: validaciones de negocio devuelven 400 con el mensaje, no un 500 crudo
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
