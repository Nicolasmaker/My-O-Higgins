package com.cahuinlabs.autenticacion.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cahuinlabs.autenticacion.models.entities.usuarios.Apoderado;
import com.cahuinlabs.autenticacion.models.entities.usuarios.Rol;
import com.cahuinlabs.autenticacion.models.entities.usuarios.Usuario;
import com.cahuinlabs.autenticacion.models.request.apoderado.ActualizarApoderadoRequest;
import com.cahuinlabs.autenticacion.models.request.apoderado.CrearApoderadoRequest;
import com.cahuinlabs.autenticacion.repository.ApoderadoRepository;
import com.cahuinlabs.autenticacion.repository.RolRepository;
import com.cahuinlabs.autenticacion.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class ApoderadoService {

 //Variables de tipo objeto de los repositories y el servicio de direccion
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final DireccionService direccionService;
    private final ApoderadoRepository apoderadoRepository;
    private final PasswordEncoder passwordEncoder;

 //Inyeccion del repositorios a traves del constructor
    public ApoderadoService(UsuarioRepository usuarioRepository, RolRepository rolRepository, DireccionService direccionService, ApoderadoRepository apoderadoRepository, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.direccionService = direccionService;
        this.apoderadoRepository = apoderadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Apoderado crearApoderado(CrearApoderadoRequest requestApoderado){

        if(usuarioRepository.existsById(requestApoderado.getApoRut())){
            throw new RuntimeException("Apoderado ya registrado con el rut: " + requestApoderado.getApoRut());
        }

        Apoderado nuevoApoderado = new Apoderado();

     //Datos de la entidad usuario
        nuevoApoderado.setUsuRut(requestApoderado.getApoRut());
        nuevoApoderado.setUsuDvRut(requestApoderado.getApoDvRut());
        nuevoApoderado.setUsuPNombre(requestApoderado.getApoPrimerNombre());
        nuevoApoderado.setUsuSNombre(requestApoderado.getApoSegundoNombre());
        nuevoApoderado.setUsuApePat(requestApoderado.getApoApellidoPat());
        nuevoApoderado.setUsuApeMat(requestApoderado.getApoApellidoMat());
        nuevoApoderado.setUsuEmail(requestApoderado.getApoEmail());
        nuevoApoderado.setUsuTel(requestApoderado.getApoTel());

        String passwordEncriptada = passwordEncoder.encode(requestApoderado.getApoPassword()); //Encriptacion de la contraseña
        nuevoApoderado.setUsuPassword(passwordEncriptada);

        nuevoApoderado.setUsuEstadoActividad(true);
     //Datos de la entidad apoderado
        nuevoApoderado.setApoParentesco(requestApoderado.getApoParentesco());

        Apoderado apoderadoGuardado = usuarioRepository.save(nuevoApoderado); //Guarda al apoderado

     //Asignar rol
        Rol rolApoderado = new Rol();
        rolApoderado.setRolNombre("ROLE_APODERADO");
        rolApoderado.setUsuario(apoderadoGuardado);
        rolRepository.save(rolApoderado); //Guarda el rol del apoderado
        
     //Gestionar la direccion
        direccionService.guardarDireccionUsuario(apoderadoGuardado.getUsuRut(), requestApoderado.getApoDireccion(), requestApoderado.getApoNumeroDireccion(),
                                                 requestApoderado.getApoTipoCasa(), requestApoderado.getIdComuna());
        return apoderadoGuardado;
    }

    public Apoderado obtenerApoderadoPorRut(Integer rut){
        Usuario usuario =usuarioRepository.findById(rut)
            .orElseThrow(() -> new RuntimeException("Apoderado no encontrado con el rut: " + rut));

        if(!(usuario instanceof Apoderado)){
            throw new RuntimeException("El usuario con rut: " + rut + " no es un apoderado.");
        }
        return (Apoderado) usuario;
    }

    public List<Apoderado> listarApoderados() {
        return apoderadoRepository.findAll();
    }

    @Transactional
    public Apoderado actualizarApoderado(Integer rut, ActualizarApoderadoRequest requestActApoderado){

        Apoderado apoderadoExistente = obtenerApoderadoPorRut(rut);

        if(requestActApoderado.getApoPrimerNombre()    != null) { apoderadoExistente.setUsuPNombre(requestActApoderado.getApoPrimerNombre()); }
        if(requestActApoderado.getApoSegundoNombre()   != null) { apoderadoExistente.setUsuSNombre(requestActApoderado.getApoSegundoNombre()); }
        if(requestActApoderado.getApoApellidoPat()     != null) { apoderadoExistente.setUsuApePat(requestActApoderado.getApoApellidoPat()); }
        if(requestActApoderado.getApoApellidoMat()     != null) { apoderadoExistente.setUsuApeMat(requestActApoderado.getApoApellidoMat()); }
        if(requestActApoderado.getApoEmail()           != null) { apoderadoExistente.setUsuEmail(requestActApoderado.getApoEmail()); }
        if(requestActApoderado.getApoTel()             != null) { apoderadoExistente.setUsuTel(requestActApoderado.getApoTel()); }
        if(requestActApoderado.getApoParentesco()      != null) { apoderadoExistente.setApoParentesco(requestActApoderado.getApoParentesco()); }
        if(requestActApoderado.getApoEstadoActividad() != null) { apoderadoExistente.setUsuEstadoActividad(requestActApoderado.getApoEstadoActividad()); }

     //Actualizar la direccion
        if(requestActApoderado.getApoDireccion() != null || requestActApoderado.getApoNumeroDireccion() != null || requestActApoderado.getApoTipoCasa() != null || requestActApoderado.getIdComuna() != null){
     
            direccionService.actualizarDireccionUsuario(rut, requestActApoderado.getApoDireccion(), requestActApoderado.getApoNumeroDireccion(),
                                                          requestActApoderado.getApoTipoCasa(), requestActApoderado.getIdComuna());
        }

        return usuarioRepository.save(apoderadoExistente);        
    }
}
