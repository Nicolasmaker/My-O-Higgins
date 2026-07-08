package com.cahuinlabs.autenticacion.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cahuinlabs.autenticacion.models.entities.usuarios.Estudiante;
import com.cahuinlabs.autenticacion.models.request.estudiante.ActualizarEstudianteRequest;
import com.cahuinlabs.autenticacion.models.request.estudiante.ActualizarCursoRequest;
import com.cahuinlabs.autenticacion.models.request.estudiante.CrearEstudianteRequest;
import com.cahuinlabs.autenticacion.service.EstudianteService;

@RequestMapping("/estudiantes")
@RestController
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

 //============================================================================
 //                           ENDPOINTS DE GETS
 //============================================================================

    @GetMapping
    public ResponseEntity<?> listarEstudiantes(){
        List<Estudiante> estudiantes = estudianteService.listarEstudiantes();
        return ResponseEntity.ok(estudiantes);
    }

    @GetMapping("/{rut}")
    public ResponseEntity<?> obtenerEstudiantePorRut(@PathVariable Integer rut){
        Estudiante estudiante = estudianteService.obtenerEstudiantePorRut(rut);
        return ResponseEntity.ok(estudiante);
    }

 //============================================================================
 //                           ENDPOINT POST Y PUT
 //============================================================================

    @PostMapping
    public ResponseEntity<?> crearEstudiante(@RequestBody CrearEstudianteRequest estudianteRequest){
        estudianteService.crearEstudiante(estudianteRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Estudiante creado exitosamente con el rut: " + estudianteRequest.getEstRut());
    }

    @PutMapping("/{rut}")
    public ResponseEntity<?> actualizarEstudiante(@PathVariable Integer rut, @RequestBody ActualizarEstudianteRequest actEstudianteRequest){
        estudianteService.actualizarEstudiante(rut, actEstudianteRequest);
        return ResponseEntity.ok("Estudiante actualizado exitosamente con el rut: " + rut);
    }

    // Endpoint angosto (solo cursoId) para que MS-GestionMatricula sincronice el curso del
    // estudiante al registrar/editar una matrícula, sin exponer PUT completo (que permitiría
    // cambiar email/nombre/etc) a llamadas de servidor a servidor sin JWT de usuario.
    @PatchMapping("/{rut}/curso")
    public ResponseEntity<?> actualizarCurso(@PathVariable Integer rut, @RequestBody ActualizarCursoRequest request){
        estudianteService.actualizarCursoEstudiante(rut, request.getCursoId());
        return ResponseEntity.ok().build();
    }
}
