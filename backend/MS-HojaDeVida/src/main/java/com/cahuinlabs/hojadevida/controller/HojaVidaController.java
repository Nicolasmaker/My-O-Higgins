package com.cahuinlabs.hojadevida.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cahuinlabs.hojadevida.dto.HojaVidaEstudianteDTO;
import com.cahuinlabs.hojadevida.service.HojaVidaService;

@RestController
@RequestMapping("/api/hojas-vida")
public class HojaVidaController {

    private final HojaVidaService hojaVidaService;

    public HojaVidaController(HojaVidaService hojaVidaService) {
        this.hojaVidaService = hojaVidaService;
    }

    @GetMapping
    public ResponseEntity<List<HojaVidaEstudianteDTO>> obtenerTodasLasHojasDeVida() {
        return ResponseEntity.ok(hojaVidaService.obtenerTodasLasHojasDeVida());
    }

    @GetMapping("/{idHojaVida}")
    public ResponseEntity<HojaVidaEstudianteDTO> obtenerHojaVidaPorId(@PathVariable Long idHojaVida) {
        return ResponseEntity.ok(hojaVidaService.obtenerHojaVidaPorId(idHojaVida));
    }

    // Busca la hoja de vida a partir del RUT del estudiante
    @GetMapping("/estudiante/{rut}")
    public ResponseEntity<HojaVidaEstudianteDTO> obtenerHojaVidaPorRut(@PathVariable Long rut) {
        return ResponseEntity.ok(hojaVidaService.obtenerHojaVidaPorRut(rut));
    }

    @PostMapping
    public ResponseEntity<HojaVidaEstudianteDTO> crearHojaVida(@RequestBody HojaVidaEstudianteDTO request) {
        HojaVidaEstudianteDTO hojaVidaCreada = hojaVidaService.crearHojaVida(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(hojaVidaCreada);
    }

    @PutMapping("/{idHojaVida}")
    public ResponseEntity<HojaVidaEstudianteDTO> actualizarHojaVida(
            @PathVariable Long idHojaVida,
            @RequestBody HojaVidaEstudianteDTO request) {
        return ResponseEntity.ok(hojaVidaService.actualizarHojaVida(idHojaVida, request));
    }

    @DeleteMapping("/{idHojaVida}")
    public ResponseEntity<Void> eliminarHojaVida(@PathVariable Long idHojaVida) {
        hojaVidaService.eliminarHojaVida(idHojaVida);
        return ResponseEntity.noContent().build();
    }
}