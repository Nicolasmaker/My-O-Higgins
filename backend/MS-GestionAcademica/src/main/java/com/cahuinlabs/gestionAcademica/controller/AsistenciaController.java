package com.cahuinlabs.gestionAcademica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cahuinlabs.gestionAcademica.dto.AsistenciaResponseDTO;
import com.cahuinlabs.gestionAcademica.models.entities.Asistencia;
import com.cahuinlabs.gestionAcademica.models.request.Asistencia.ActualizarAsistenciaRequest;
import com.cahuinlabs.gestionAcademica.models.request.Asistencia.CrearAsistenciaRequest;
import com.cahuinlabs.gestionAcademica.service.AsistenciaService;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/asistencia")
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    @PostMapping
    public ResponseEntity<Asistencia> crear(@Valid @RequestBody CrearAsistenciaRequest request) {
        return ResponseEntity.ok(asistenciaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Asistencia> actualizar(@PathVariable Integer id, @Valid @RequestBody ActualizarAsistenciaRequest request) {
        return ResponseEntity.ok(asistenciaService.actualizar(id, request));
    }

    @GetMapping
    public ResponseEntity<List<AsistenciaResponseDTO>> listar() {
        return ResponseEntity.ok(asistenciaService.listarTodas());
    }

    @GetMapping("/estudiante/{rut}")
    public ResponseEntity<List<AsistenciaResponseDTO>> listarPorEstudiante(@PathVariable Integer rut) {
        return ResponseEntity.ok(asistenciaService.listarPorEstudiante(rut));
    }

    // Roster de una asignatura/curso (Impartir) en una fecha puntual — para "pasar lista":
    // saber qué estudiantes ya tienen asistencia registrada ese día y no duplicarla.
    @GetMapping("/impartir/{idImpartir}")
    public ResponseEntity<List<AsistenciaResponseDTO>> listarPorImpartirYFecha(
            @PathVariable Integer idImpartir,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(asistenciaService.listarPorImpartirYFecha(idImpartir, fecha));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (asistenciaService.buscarPorId(id).isPresent()) {
            asistenciaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
