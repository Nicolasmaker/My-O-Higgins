package com.cahuinlabs.anotaciones.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.cahuinlabs.anotaciones.models.entities.Anotacion;
import com.cahuinlabs.anotaciones.models.request.AnotacionDTO;
import com.cahuinlabs.anotaciones.service.AnotacionService;

@RestController
@RequestMapping("/api/anotaciones")
public class AnotacionController {

    @Autowired
    private AnotacionService anotacionService;

    // POST: Crear anotacion. Permiso: Docentes e Inspectores
    @PostMapping
    public ResponseEntity<Anotacion> registrarAnotacion(@RequestBody AnotacionDTO request) {
        Anotacion nuevaAnotacion = anotacionService.crearAnotacion(request);
        return new ResponseEntity<>(nuevaAnotacion, HttpStatus.CREATED);
    }

    // GET: Consultar por Hoja de vida. Permiso: Todos segun su perfil
    @GetMapping("/hojavida/{idHojaVida}")
    public ResponseEntity<List<Anotacion>> consultarAnotacionesEstudiante(@PathVariable Long idHojaVida) {
        List<Anotacion> anotaciones = anotacionService.obtenerPorHojaVida(idHojaVida);
        return new ResponseEntity<>(anotaciones, HttpStatus.OK);
    }

    // GET: Consultar por RUT del estudiante. Permiso: Todos segun su perfil
    @GetMapping("/estudiante/{rut}")
    public ResponseEntity<List<Anotacion>> consultarAnotacionesPorRutEstudiante(@PathVariable Long rut) {
        List<Anotacion> anotaciones = anotacionService.obtenerPorEstudianteRut(rut);
        return new ResponseEntity<>(anotaciones, HttpStatus.OK);
    }

    // GET: Obtener todas las anotaciones. Permiso: Todos segun su perfil
    @GetMapping
    public ResponseEntity<List<Anotacion>> obtenerTodasAnotaciones() {
        List<Anotacion> anotaciones = anotacionService.obtenerTodas();
        return new ResponseEntity<>(anotaciones, HttpStatus.OK);
    }

    // PUT: Modificar anotacion. Permiso: Solo Directivos
    @PutMapping("/{idAnot}")
    public ResponseEntity<Anotacion> modificarAnotacion(
            @PathVariable Long idAnot, 
            @RequestBody AnotacionDTO request) {
        Anotacion anotacionModificada = anotacionService.modificarAnotacion(idAnot, request);
        return new ResponseEntity<>(anotacionModificada, HttpStatus.OK);
    }

    // DELETE: Eliminar anotacion. Permiso: Solo Directivos
    @DeleteMapping("/{idAnot}")
    public ResponseEntity<Void> eliminarAnotacion(@PathVariable Long idAnot) {
        anotacionService.eliminarAnotacion(idAnot);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
