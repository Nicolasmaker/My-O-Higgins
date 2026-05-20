package com.cahuinlabs.autenticacion.service;
import org.springframework.stereotype.Service;

import com.cahuinlabs.autenticacion.models.entities.usuarios.Usuario;
import com.cahuinlabs.autenticacion.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

import com.cahuinlabs.autenticacion.repository.RolRepository;
import java.util.List;

@Service
public class UsuarioService {

 //Cree una variable de tipo objeto de la clase UsuarioRepository
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

 //Inyecto el repositorio a traves del constructor de la clase UsuarioService
    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

 //Metodo para listar todos los usuarios
    public List<Usuario> listarUsuarios(){
        return usuarioRepository.findAll();
    }

 //Metodo para buscar un usuario por su rut, si no se encuentra lanza una excepcion
    public Usuario buscarPorRut(Integer rut){
        return usuarioRepository.findById(rut)
            .orElseThrow(() -> new RuntimeException("No se encontro un usuario con el rut: " + rut));
    }

 //Metodo para desactivar al usuario DUHH
    @Transactional //Si falla alguna wea en el metodo, se revierte lo que se estaba haciendo
    public void desactivarUsuario(Integer rut){
        Usuario usuario = buscarPorRut(rut);
        usuario.setUsuEstadoActividad(false);
        usuarioRepository.save(usuario);
    }

 //Metodo para eliminar a un usuario
    @Transactional
    public void eliminarUsuario(Integer rut){
        Usuario usuario = buscarPorRut(rut);
        
        //Se elimina el rol asociado para evitar el error de integridad referencial
        rolRepository.deleteByUsuarioUsuRut(rut);
        usuarioRepository.delete(usuario);
    }
    
 //Metodo para verificar la existencia del usuario
    public boolean existeUsuario(Integer rut){
        return usuarioRepository.existsById(rut);
    }

}
