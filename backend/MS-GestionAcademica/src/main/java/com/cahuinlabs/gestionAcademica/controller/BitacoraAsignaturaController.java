package com.cahuinlabs.gestionAcademica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cahuinlabs.gestionAcademica.models.entities.BitacoraAsignatura;
import com.cahuinlabs.gestionAcademica.models.request.BitacoraAsignatura.ActualizarBitAsignaturaRequest;
import com.cahuinlabs.gestionAcademica.models.request.BitacoraAsignatura.CrearBitAsignaturaRequest;
import com.cahuinlabs.gestionAcademica.service.BitacoraAsignaturaService;

import java.util.List;

@RestController
@RequestMapping("/bitacora-asignatura") 
public class BitacoraAsignaturaController {

    @Autowired
    private BitacoraAsignaturaService bitacoraService;

    @PostMapping
    public ResponseEntity<BitacoraAsignatura> crear(@RequestBody CrearBitAsignaturaRequest request) {
        return ResponseEntity.ok(bitacoraService.crearBitAsignatura(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BitacoraAsignatura> actualizar(@PathVariable Integer id, @RequestBody ActualizarBitAsignaturaRequest request) {
        return ResponseEntity.ok(bitacoraService.actualizarBitAsignatura(id, request));
    }

    @GetMapping
    public ResponseEntity<List<BitacoraAsignatura>> listar() {
        return ResponseEntity.ok(bitacoraService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BitacoraAsignatura> obtenerPorId(@PathVariable Integer id) {
        return bitacoraService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (bitacoraService.buscarPorId(id).isPresent()) {
            bitacoraService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
