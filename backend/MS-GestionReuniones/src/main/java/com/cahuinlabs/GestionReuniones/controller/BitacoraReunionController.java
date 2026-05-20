package com.cahuinlabs.GestionReuniones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;

import com.cahuinlabs.GestionReuniones.models.entities.BitReunionApoderado;
import com.cahuinlabs.GestionReuniones.models.entities.BitReunionGeneral;
import com.cahuinlabs.GestionReuniones.models.entities.BitReunionIndividual;
import com.cahuinlabs.GestionReuniones.models.request.ActualizarApoderadoRequest;
import com.cahuinlabs.GestionReuniones.models.request.ActualizarFirmasRequest;
import com.cahuinlabs.GestionReuniones.models.request.BitReunionApoderadoRequest;
import com.cahuinlabs.GestionReuniones.models.request.ReunionGeneralRequest;
import com.cahuinlabs.GestionReuniones.models.request.ReunionIndividualRequest;
import com.cahuinlabs.GestionReuniones.service.GestionReunionesService;

@RestController
@RequestMapping("/api/reuniones")
// recibe peticiones HTTP de clientes y delega a service la logica

public class BitacoraReunionController {

    @Autowired
    private GestionReunionesService reunionesService;

    // POST registrar bitacora base de reunion con apoderado RF29
    @PostMapping("/apoderado")
    public ResponseEntity<BitReunionApoderado> crearApoderado(@RequestBody BitReunionApoderadoRequest request) {
        BitReunionApoderado nueva = reunionesService.registrarReunionApoderado(request);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // POST registrar una entrevista individual con un apoderado
    // accesible por docentes e inspectores autorizados
    @PostMapping("/individual")
    public ResponseEntity<BitReunionIndividual> crearIndividual(@RequestBody ReunionIndividualRequest request) {
        BitReunionIndividual nuevaReunion = reunionesService.registrarReunionIndividual(request);
        return new ResponseEntity<>(nuevaReunion, HttpStatus.CREATED);
    }

    // POST registrar un acta o reunion general de coordinacion
    // accesible por docentes, inspectores y equipo Directivo
    @PostMapping("/general")
    public ResponseEntity<BitReunionGeneral> crearGeneral(@RequestBody ReunionGeneralRequest request) {
        BitReunionGeneral nuevaMinuta = reunionesService.registrarReunionGeneral(request);
        return new ResponseEntity<>(nuevaMinuta, HttpStatus.CREATED);
    }

    // GET obtener el listado de reuniones agendadas por un funcionario en particular
    @GetMapping("/funcionario/{rut}")
    public ResponseEntity<List<BitReunionApoderado>> listarPorFuncionario(@PathVariable Long rut) {
        List<BitReunionApoderado> reuniones = reunionesService.listarPorFuncionario(rut);
        return new ResponseEntity<>(reuniones, HttpStatus.OK);
    }

    // GET obtener el detalle de una reunion individual por su propio ID
    @GetMapping("/individual/{id}")
    public ResponseEntity<BitReunionIndividual> verDetalleIndividual(@PathVariable Long id) {
        return new ResponseEntity<>(reunionesService.obtenerDetalleIndividual(id), HttpStatus.OK);
    }

    // GET obtener el detalle de una reunion general por su propio ID
    @GetMapping("/general/{id}")
    public ResponseEntity<BitReunionGeneral> verDetalleGeneral(@PathVariable Long id) {
        return new ResponseEntity<>(reunionesService.obtenerDetalleGeneral(id), HttpStatus.OK);
    }

    // GET listar todas las bitacoras base de reuniones con apoderado
    @GetMapping("/apoderado")
    public ResponseEntity<List<BitReunionApoderado>> listarReunionesApoderado() {
        List<BitReunionApoderado> reuniones = reunionesService.listarReunionesApoderado();
        return new ResponseEntity<>(reuniones, HttpStatus.OK);
    }

    // GET obtener una bitacora base por su ID
    @GetMapping("/apoderado/{id}")
    public ResponseEntity<BitReunionApoderado> obtenerApoderado(@PathVariable Long id) {
        return new ResponseEntity<>(reunionesService.obtenerApoderadoPorId(id), HttpStatus.OK);
    }

    // GET listar todas las entrevistas individuales
    @GetMapping("/individual")
    public ResponseEntity<List<BitReunionIndividual>> listarIndividuales() {
        return new ResponseEntity<>(reunionesService.listarIndividuales(), HttpStatus.OK);
    }

    // GET listar todas las actas de reunion general
    @GetMapping("/general")
    public ResponseEntity<List<BitReunionGeneral>> listarGenerales() {
        return new ResponseEntity<>(reunionesService.listarGenerales(), HttpStatus.OK);
    }

    // PUT actualizar estado de firmas en entrevista individual RF29
    @PutMapping("/individual/{id}/firmas")
    public ResponseEntity<BitReunionIndividual> actualizarFirmas(
            @PathVariable Long id,
            @RequestBody ActualizarFirmasRequest request) {
        return new ResponseEntity<>(reunionesService.actualizarFirmas(id, request), HttpStatus.OK);
    }

    // PUT corregir compromisos u observaciones de bitacora base RF29
    @PutMapping("/apoderado/{id}")
    public ResponseEntity<BitReunionApoderado> actualizarApoderado(
            @PathVariable Long id,
            @RequestBody ActualizarApoderadoRequest request) {
        return new ResponseEntity<>(reunionesService.actualizarApoderado(id, request), HttpStatus.OK);
    }

    // Manejador de excepciones, cuando no se encuentra una entidad devuelve error 404 con mensaje
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}