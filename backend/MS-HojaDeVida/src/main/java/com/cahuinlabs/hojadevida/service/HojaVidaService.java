package com.cahuinlabs.hojadevida.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cahuinlabs.hojadevida.dto.HojaVidaEstudianteDTO;
import com.cahuinlabs.hojadevida.exception.ResourceNotFoundException;
import com.cahuinlabs.hojadevida.model.HojaVidaEstudiante;
import com.cahuinlabs.hojadevida.repository.HojaVidaRepository;

//NO BORREN ESTAS WEAS ES PARA VER SI FUNCA LA COMUNICACION
import org.springframework.web.client.RestClient; 
import org.springframework.web.client.HttpClientErrorException; 
import com.cahuinlabs.hojadevida.dto.EstudianteDTO; 

@Service
public class HojaVidaService {

    private final HojaVidaRepository hojaVidaRepository;
    private final RestClient autenticacionRestClient;

    public HojaVidaService(RestClient autenticacionRestClient, HojaVidaRepository hojaVidaRepository) {
        this.hojaVidaRepository = hojaVidaRepository;
        this.autenticacionRestClient = autenticacionRestClient; //nuevo
    }

    public List<HojaVidaEstudianteDTO> obtenerTodasLasHojasDeVida() {
        return hojaVidaRepository.findAll().stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    public HojaVidaEstudianteDTO obtenerHojaVidaPorId(Long idHojaVida) {
        HojaVidaEstudiante entidad = hojaVidaRepository.findById(idHojaVida)
                .orElseThrow(() -> new ResourceNotFoundException("Hoja de vida no encontrada: " + idHojaVida));
        return mapearADTO(entidad);
    }

    public HojaVidaEstudianteDTO crearHojaVida(HojaVidaEstudianteDTO request) {

        //NUEVO: Antes de crearla, validamos comunicándonos con el otro microservicio
        if (!existeEstudiante(request.getEstudianteUsuRut())) {
            throw new IllegalArgumentException("No se puede crear: El estudiante con RUT " + request.getEstudianteUsuRut() + " no existe o no tiene el rol correcto en Autenticación.");
        }

        HojaVidaEstudiante hojaVidaEstudiante = new HojaVidaEstudiante();
        hojaVidaEstudiante.setEstudianteUsuRut(request.getEstudianteUsuRut());
        hojaVidaEstudiante.setMatriculaId(request.getMatriculaId());
        
        HojaVidaEstudiante guardado = hojaVidaRepository.save(hojaVidaEstudiante);
        return mapearADTO(guardado);
    }

    public HojaVidaEstudianteDTO actualizarHojaVida(Long idHojaVida, HojaVidaEstudianteDTO request) {

        //NUEVO: También validamos al actualizar por si intentan poner un RUT falso
        if (!existeEstudiante(request.getEstudianteUsuRut())) {
            throw new IllegalArgumentException("No se puede actualizar: El estudiante con RUT " + request.getEstudianteUsuRut() + " no existe en Autenticación.");
        }

        HojaVidaEstudiante hojaVidaEstudiante = hojaVidaRepository.findById(idHojaVida)
                .orElseThrow(() -> new ResourceNotFoundException("Hoja de vida no encontrada: " + idHojaVida));
        
        hojaVidaEstudiante.setEstudianteUsuRut(request.getEstudianteUsuRut());
        hojaVidaEstudiante.setMatriculaId(request.getMatriculaId());
        
        HojaVidaEstudiante actualizado = hojaVidaRepository.save(hojaVidaEstudiante);
        return mapearADTO(actualizado);
    }

    public void eliminarHojaVida(Long idHojaVida) {
        if (!hojaVidaRepository.existsById(idHojaVida)) {
            throw new ResourceNotFoundException("Hoja de vida no encontrada: " + idHojaVida);
        }
        hojaVidaRepository.deleteById(idHojaVida);
    }

        // NUEVO: Este es el método que hace la magia de ir a preguntar al otro MS
    private boolean existeEstudiante(Long rut) {
        try {
            EstudianteDTO estudiante = autenticacionRestClient.get()
                    .uri("/estudiantes/{rut}", rut) //URL a /estudiantes en base al ms de autenticacion
                    .retrieve()
                    .body(EstudianteDTO.class);
            
            return estudiante != null 
                && estudiante.rol() != null 
                && "ROLE_ESTUDIANTE".equalsIgnoreCase(estudiante.rol().rolNombre()); 
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el servicio de autenticación", e);
        }
    }

    // Helper
    private HojaVidaEstudianteDTO mapearADTO(HojaVidaEstudiante entidad) {
        return new HojaVidaEstudianteDTO(
                entidad.getIdHojaVida(),
                entidad.getEstudianteUsuRut(),
                entidad.getMatriculaId()
        );
    }
}