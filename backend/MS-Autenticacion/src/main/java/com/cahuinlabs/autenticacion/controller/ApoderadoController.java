package com.cahuinlabs.autenticacion.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cahuinlabs.autenticacion.models.entities.usuarios.Apoderado;
import com.cahuinlabs.autenticacion.models.request.apoderado.ActualizarApoderadoRequest;
import com.cahuinlabs.autenticacion.models.request.apoderado.CrearApoderadoRequest;
import com.cahuinlabs.autenticacion.service.ApoderadoService;

@RequestMapping("/apoderados")
@RestController
public class ApoderadoController {

    private final ApoderadoService apoderadoService;

    public ApoderadoController(ApoderadoService apoderadoService) {
        this.apoderadoService = apoderadoService;
    }

 //============================================================================
 //                           ENDPOINTS DE GETS
 //============================================================================

    @GetMapping
    public ResponseEntity<?> listarApoderados(){
        List <Apoderado> apoderados = apoderadoService.listarApoderados();
        return ResponseEntity.ok(apoderados);
    }

    @GetMapping("/{rut}")
    public ResponseEntity<?> obtenerApoderadoPorRut(@PathVariable Integer rut){
        Apoderado apoderado = apoderadoService.obtenerApoderadoPorRut(rut);
        return ResponseEntity.ok(apoderado);
    }

 //============================================================================
 //                         ENDPOINTS DE POST Y PUT
 //============================================================================

    @PostMapping
    public ResponseEntity<?> registrarApoderado(@RequestBody CrearApoderadoRequest apoderadoRequest){
        apoderadoService.crearApoderado(apoderadoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Apoderado creado exitosamente con el rut: " + apoderadoRequest.getApoRut());
    }

    @PutMapping("/{rut}")
    public ResponseEntity<?> actualizarApoderado(@PathVariable Integer rut, @RequestBody ActualizarApoderadoRequest actApoderadoRequest){
        apoderadoService.actualizarApoderado(rut, actApoderadoRequest);
        return ResponseEntity.ok("Datos del apoderado actualizado correctamente");
    }
}
