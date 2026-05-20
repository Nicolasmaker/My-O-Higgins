package com.cahuinlabs.autenticacion.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.cahuinlabs.autenticacion.models.entities.usuarios.Estudiante;
import com.cahuinlabs.autenticacion.models.entities.usuarios.Rol;
import com.cahuinlabs.autenticacion.models.entities.usuarios.Usuario;
import com.cahuinlabs.autenticacion.models.request.estudiante.ActualizarEstudianteRequest;
import com.cahuinlabs.autenticacion.models.request.estudiante.CrearEstudianteRequest;
import com.cahuinlabs.autenticacion.repository.EstudianteRepository;
import com.cahuinlabs.autenticacion.repository.RolRepository;
import com.cahuinlabs.autenticacion.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class EstudianteService {

//Variables de tipo objeto de los repositories y el servicio de direccion
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final DireccionService direccionService;
    private final EstudianteRepository estudianteRepository;
    private final PasswordEncoder passwordEncoder;

//Inyeccion del repositorios a traves del constructor
    public EstudianteService(UsuarioRepository usuarioRepository, RolRepository rolRepository, DireccionService direccionService, EstudianteRepository estudianteRepository, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.direccionService = direccionService;
        this.estudianteRepository = estudianteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Estudiante crearEstudiante(CrearEstudianteRequest requestEstudiante){

        if(usuarioRepository.existsById(requestEstudiante.getEstRut())){
            throw new RuntimeException("Estudiante ya registrado con el rut: " + requestEstudiante.getEstRut());
        }

        Estudiante nuevoEstudiante = new Estudiante();
    //Datos de la entidad usuario
        nuevoEstudiante.setUsuRut(requestEstudiante.getEstRut());
        nuevoEstudiante.setUsuDvRut(requestEstudiante.getEstDvRut());
        nuevoEstudiante.setUsuPNombre(requestEstudiante.getEstPrimerNombre());
        nuevoEstudiante.setUsuSNombre(requestEstudiante.getEstSegundoNombre());
        nuevoEstudiante.setUsuApePat(requestEstudiante.getEstApellidoPat());
        nuevoEstudiante.setUsuApeMat(requestEstudiante.getEstApellidoMat());
        nuevoEstudiante.setUsuEmail(requestEstudiante.getEstEmail());
        nuevoEstudiante.setUsuTel(requestEstudiante.getEstTel());

        String passwordEncriptada = passwordEncoder.encode(requestEstudiante.getEstPassword()); //Encriptacion de la contraseña
        nuevoEstudiante.setUsuPassword(passwordEncriptada);

        nuevoEstudiante.setUsuEstadoActividad(true);
    //Datos de la entidad estudiante
        nuevoEstudiante.setEstParentesco(requestEstudiante.getEstParentesco());

        Estudiante estudianteGuardado = usuarioRepository.save(nuevoEstudiante); //Guarda al estudiante

    //Asignar rol
        Rol rolEstudiante = new Rol();
        rolEstudiante.setRolNombre("ROLE_ESTUDIANTE");
        rolEstudiante.setUsuario(estudianteGuardado);
        rolRepository.save(rolEstudiante); //Guarda el rol del estudiante

    //Gestionar la direccion
        direccionService.guardarDireccionUsuario(
            estudianteGuardado.getUsuRut(), 
            requestEstudiante.getEstDireccion(), 
            requestEstudiante.getEstNumeroDireccion(), 
            requestEstudiante.getEstTipoCasa(), 
            requestEstudiante.getIdComuna()
        );

        return estudianteGuardado;
    }

    public List<Estudiante> listarEstudiantes() {
        return estudianteRepository.findAll();
    }

    public Estudiante obtenerEstudiantePorRut(Integer rut){
        Usuario usuario = usuarioRepository.findById(rut)
            .orElseThrow(() -> new RuntimeException("No se encontro un usuario con el rut: " + rut));

    //Esto es para verificar que el usuario encontrado es un estudiante y no otro wom
        if(!(usuario instanceof Estudiante)){
            throw new RuntimeException("El rut ingresado pertenece a un usuario pero no es un estudiante");
        }

        return (Estudiante) usuario;
    }

    @Transactional
    public Estudiante actualizarEstudiante(Integer rut, ActualizarEstudianteRequest requestActEstudiante){
        Estudiante estudianteActual = obtenerEstudiantePorRut(rut);

    //Aqui se usa el if != null para verificar si el campo a actualizar fue enviado en la request, si no es nulo se actualiza, sino se mantiene el valor actual
        if(requestActEstudiante.getEstPrimerNombre() != null){
            estudianteActual.setUsuPNombre(requestActEstudiante.getEstPrimerNombre());
        }
        if(requestActEstudiante.getEstSegundoNombre() != null){
            estudianteActual.setUsuSNombre(requestActEstudiante.getEstSegundoNombre());
        }
        if(requestActEstudiante.getEstApellidoPat() != null){
            estudianteActual.setUsuApePat(requestActEstudiante.getEstApellidoPat());
        }
        if(requestActEstudiante.getEstApellidoMat() != null){
            estudianteActual.setUsuApeMat(requestActEstudiante.getEstApellidoMat());
        }
        if(requestActEstudiante.getEstEmail() != null){
            estudianteActual.setUsuEmail(requestActEstudiante.getEstEmail());
        }
        if(requestActEstudiante.getEstTel() != null){
            estudianteActual.setUsuTel(requestActEstudiante.getEstTel());
        }
        if(requestActEstudiante.getEstParentesco() != null){
            estudianteActual.setEstParentesco(requestActEstudiante.getEstParentesco());
        }
        if(requestActEstudiante.getEstEstadoActividad() != null){
            estudianteActual.setUsuEstadoActividad(requestActEstudiante.getEstEstadoActividad());
        }

    //Actualizar la direccion
        if(requestActEstudiante.getEstDireccion() != null || requestActEstudiante.getEstNumeroDireccion() != null || requestActEstudiante.getEstTipoCasa() != null || requestActEstudiante.getIdComuna() != null){
            
            direccionService.actualizarDireccionUsuario(
                rut, 
                requestActEstudiante.getEstDireccion(), 
                requestActEstudiante.getEstNumeroDireccion(), 
                requestActEstudiante.getEstTipoCasa(), 
                requestActEstudiante.getIdComuna()
            );
        }

        return usuarioRepository.save(estudianteActual);
    }
}

