package com.cahuinlabs.autenticacion.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cahuinlabs.autenticacion.models.entities.usuarios.Usuario;
import com.cahuinlabs.autenticacion.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

 //============================================================================
 //                           ENDPOINTS DE GETS
 //============================================================================

    @GetMapping("/me") 
    public ResponseEntity<?> obtenerMiPerfil() {
    
     //Spring Security saca al usuario automáticamente del Token JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
     //Extrae el objeto principal 
        Object principal = authentication.getPrincipal();
    
     //Lo devuelve al usuario
        return ResponseEntity.ok(principal);
    }

    @GetMapping
    public ResponseEntity<?> listarUsuarios(){
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{rut}")
    public ResponseEntity<?> obtenerUsuarioGeneralPorRut(@PathVariable Integer rut){
        var usuario = usuarioService.buscarPorRut(rut); //el var asume que el tipo de dato es Usuario para cosas muy obvias se puede usar :b
        return ResponseEntity.ok(usuario);
    }

 //============================================================================
 //                       ENDPOINTS DE PATCH Y DELETE
 //============================================================================
    @PatchMapping("/{rut}/desactivar")
    public ResponseEntity<?> desactivarUsuario(@PathVariable Integer rut){
        usuarioService.desactivarUsuario(rut);
        return ResponseEntity.ok("La cuenta del usuario ha sido desactivado exitosamente.");
    }

    @DeleteMapping("/{rut}/eliminar")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Integer rut){
        usuarioService.eliminarUsuario(rut);
        return ResponseEntity.ok("El usuario ha sido eliminado exitosamente.");
    }
}
