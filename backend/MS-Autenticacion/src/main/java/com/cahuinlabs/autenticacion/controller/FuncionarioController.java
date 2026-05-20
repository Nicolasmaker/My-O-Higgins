package com.cahuinlabs.autenticacion.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cahuinlabs.autenticacion.models.entities.usuarios.Funcionario;
import com.cahuinlabs.autenticacion.models.request.funcionarios.directivos.ActualizarDirectivoRequest;
import com.cahuinlabs.autenticacion.models.request.funcionarios.directivos.CrearDirectivoRequest;
import com.cahuinlabs.autenticacion.models.request.funcionarios.docente.ActualizarDocenteRequest;
import com.cahuinlabs.autenticacion.models.request.funcionarios.docente.CrearDocenteRequest;
import com.cahuinlabs.autenticacion.models.request.funcionarios.inspector.ActualizarInspectorRequest;
import com.cahuinlabs.autenticacion.models.request.funcionarios.inspector.CrearInspectorRequest;
import com.cahuinlabs.autenticacion.service.FuncionarioService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequestMapping("/funcionarios")
@RestController
public class FuncionarioController {

 //Variable de tipo objeto del servicio de funcionario
    private final FuncionarioService funcionarioService;

 //Inyeccion del serivicio a traves del constructor
    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

 //============================================================================
 //                           ENDPOINTS DE GETS
 //============================================================================
    @GetMapping
    public ResponseEntity<?> listarFuncionarios(){
        List<Funcionario> funcionarios = funcionarioService.listarFuncionarios();
        return ResponseEntity.ok(funcionarios);
    }

    @GetMapping("/{rut}")
    public ResponseEntity<?> obtenerFuncionarioPorRut(@PathVariable Integer rut){
        Funcionario funcionario = funcionarioService.buscarFuncionarioPorRut(rut);
        return ResponseEntity.ok(funcionario);
    }

 //============================================================================
 //                           ENDPOINTS DE POST
 //============================================================================

    @PostMapping("/docente")
    public ResponseEntity<?> registrarDocente(@RequestBody CrearDocenteRequest docenteRequest) {
        
     //Meotdo para crear al docente a traves del service
        funcionarioService.crearDocente(docenteRequest);
     //Retorna una respuesta con el estado de creado (codigo 201) y un mensaje de exito - SE VA A AGREGAR UN RESPONSE DESPUES, ESTO ES TEMPORAL
        return ResponseEntity.status(HttpStatus.CREATED).body("Docente registrado exitosamente con el rut: " + docenteRequest.getDcteRut());    
    }

    @PostMapping("/inspector")
    public ResponseEntity<?> crearInspector(@RequestBody CrearInspectorRequest inspectorRequest) {

        funcionarioService.crearInspector(inspectorRequest);
     //Retorna una respuesta con el estado de creado (codigo 201) y un mensaje de exito - SE VA A AGREGAR UN RESPONSE DESPUES, ESTO ES TEMPORAL
        return ResponseEntity.status(HttpStatus.CREATED).body("Inspector registrado exitosamente con el rut: " + inspectorRequest.getInsRut());
    }

    @PostMapping("/directivo")
    public ResponseEntity<?> crearDirectivo(@RequestBody CrearDirectivoRequest directivoRequest){

        funcionarioService.crearDirectivo(directivoRequest);
     //Retorna una respuesta con el estado de creado (codigo 201) y un mensaje de exito - SE VA A AGREGAR UN RESPONSE DESPUES, ESTO ES TEMPORAL
        return ResponseEntity.status(HttpStatus.CREATED).body("Directivo registrado exitosamente con el rut: " + directivoRequest.getDirRut());
    }
    
 //============================================================================
 //                             ENDPOINTS DE PUT
 //============================================================================

    @PutMapping("/docente/{rut}")
    public ResponseEntity<?> actualizarDocente(@PathVariable Integer rut, @RequestBody ActualizarDocenteRequest actDocenteRequest){

        funcionarioService.actualizarDocente(rut, actDocenteRequest);
        return ResponseEntity.ok("Datos del docente actualizados correctamente");
    }

    @PutMapping("/inspector/{rut}")
    public ResponseEntity<?> actualizarInspector(@PathVariable Integer rut, @RequestBody ActualizarInspectorRequest actInspectorRequest){
        
        funcionarioService.actualizarInspector(rut, actInspectorRequest);
        return ResponseEntity.ok("Datos del inspector actualizados correctamente");
    }

    @PutMapping("/directivo/{rut}")
    public ResponseEntity<?> actualizarDirectivo(@PathVariable Integer rut, @RequestBody ActualizarDirectivoRequest actDirectivoRequest){
        
        funcionarioService.actualizarDirectivo(rut, actDirectivoRequest);
        return ResponseEntity.ok("Datos del directivo actualizados correctamente");
    }
}
