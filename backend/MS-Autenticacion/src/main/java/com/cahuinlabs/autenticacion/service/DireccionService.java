package com.cahuinlabs.autenticacion.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cahuinlabs.autenticacion.models.entities.geografia.Comuna;
import com.cahuinlabs.autenticacion.models.entities.geografia.Direccion;
import com.cahuinlabs.autenticacion.models.entities.usuarios.Usuario;
import com.cahuinlabs.autenticacion.repository.ComunaRepository;
import com.cahuinlabs.autenticacion.repository.DireccionRepository;
import com.cahuinlabs.autenticacion.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class DireccionService {

    private final DireccionRepository direccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ComunaRepository comunaRepository;

    public DireccionService(DireccionRepository direccionRepository, UsuarioRepository usuarioRepository, ComunaRepository comunaRepository) {
        this.direccionRepository = direccionRepository;
        this.usuarioRepository = usuarioRepository;
        this.comunaRepository = comunaRepository;
    }

//Guardar la dirreccion de un usuario recien creado
    @Transactional
    public void guardarDireccionUsuario(Integer rutUsuario, String direccion, Integer numeroDireccion, String tipoCasa, Integer idComuna){

        Usuario usuario = usuarioRepository.findById(rutUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado para asociar una direccion"));

        Comuna comuna = comunaRepository.findById(idComuna)
                .orElseThrow(() -> new RuntimeException("Comuna no encontrada con el id: " + idComuna));

        Direccion nuevaDireccion = new Direccion();
        nuevaDireccion.setDirNom(direccion);
        nuevaDireccion.setDirNum(numeroDireccion);
        nuevaDireccion.setDirDeptoCasa(tipoCasa);
        nuevaDireccion.setComuna(comuna);
        nuevaDireccion.setUsuario(usuario);

        direccionRepository.save(nuevaDireccion);
    }

//Actualizar direccion de un usuario
    @Transactional
    public void actualizarDireccionUsuario(Integer rutUsuario, String nombreDireccion, Integer numeroDireccion, String tipoCasa, Integer idComuna){
        
    //Para que se entienda esta linea le pedi al gpt que la explique, es como decir: Busca en la base de datos si el usuario ya tiene una dirección registrada
        Optional<Direccion> direccionOptional = direccionRepository.findByUsuarioUsuRut(rutUsuario);

        Direccion direccion;

        if(direccionOptional.isPresent()){ //Existe una direccion? (.isPresent)
            direccion = direccionOptional.get(); //Si existe, se obtiene la direccion para actualizarla
        } else {
            direccion = new Direccion(); //Si no existe una direccion para el usuario, se crea una nueva
            Usuario usuario = usuarioRepository.findById(rutUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            direccion.setUsuario(usuario);
        }

        if(nombreDireccion != null){
            direccion.setDirNom(nombreDireccion);
        }

        if(numeroDireccion != null){
            direccion.setDirNum(numeroDireccion);
        }

        if(tipoCasa != null){
            direccion.setDirDeptoCasa(tipoCasa);
        }

        if(idComuna != null){
            Comuna nuevaComuna = comunaRepository.findById(idComuna)
                .orElseThrow(() -> new RuntimeException("La nueva comuna indicada no existe"));
            direccion.setComuna(nuevaComuna);
        }

        direccionRepository.save(direccion);
    }

}
