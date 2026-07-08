package com.cahuinlabs.autenticacion.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cahuinlabs.autenticacion.models.entities.geografia.Comuna;
import com.cahuinlabs.autenticacion.repository.ComunaRepository;

// Lectura de comunas para poblar el <select> de "idComuna" en los formularios de creación
// de Estudiante/Apoderado/Funcionario (el frontend no tenía forma de listar comunas válidas).
@RestController
@RequestMapping("/comunas")
public class ComunaController {

    private final ComunaRepository comunaRepository;

    public ComunaController(ComunaRepository comunaRepository) {
        this.comunaRepository = comunaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Comuna>> listar() {
        return ResponseEntity.ok(comunaRepository.findAll());
    }
}
