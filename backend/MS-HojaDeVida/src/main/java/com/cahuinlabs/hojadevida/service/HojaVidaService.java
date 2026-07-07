package com.cahuinlabs.hojadevida.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cahuinlabs.hojadevida.dto.HojaVidaEstudianteDTO;
import com.cahuinlabs.hojadevida.exception.ResourceNotFoundException;
import com.cahuinlabs.hojadevida.model.HojaVidaEstudiante;
import com.cahuinlabs.hojadevida.repository.HojaVidaRepository;

import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import com.cahuinlabs.hojadevida.dto.EstudianteDTO;
import com.cahuinlabs.hojadevida.dto.MatriculaDTO;

@Service
public class HojaVidaService {

    private final HojaVidaRepository hojaVidaRepository;
    private final RestClient autenticacionRestClient;
    private final RestClient matriculaRestClient;

    public HojaVidaService(RestClient autenticacionRestClient,
                           RestClient matriculaRestClient,
                           HojaVidaRepository hojaVidaRepository) {
        this.hojaVidaRepository = hojaVidaRepository;
        this.autenticacionRestClient = autenticacionRestClient;
        this.matriculaRestClient = matriculaRestClient;
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

    // Resuelve la hoja de vida de un estudiante a partir de su RUT (usado por MS-Anotaciones
    // para no requerir que el usuario conozca el idHojaVida al registrar una anotacion)
    public HojaVidaEstudianteDTO obtenerHojaVidaPorRut(Long estudianteUsuRut) {
        HojaVidaEstudiante entidad = hojaVidaRepository.findByEstudianteUsuRut(estudianteUsuRut)
                .orElseThrow(() -> new ResourceNotFoundException("No existe hoja de vida para el estudiante con RUT: " + estudianteUsuRut));
        return mapearADTO(entidad);
    }

    public HojaVidaEstudianteDTO crearHojaVida(HojaVidaEstudianteDTO request) {
        // Validamos que el estudiante exista en Autenticacion
        if (!existeEstudiante(request.getEstudianteUsuRut())) {
            throw new IllegalArgumentException("No se puede crear: el estudiante con RUT " + request.getEstudianteUsuRut() + " no existe o no tiene el rol correcto en Autenticacion.");
        }
        // Validamos que la matricula exista en GestionMatricula
        if (!existeMatricula(request.getMatriculaId())) {
            throw new IllegalArgumentException("No se puede crear: la matricula con ID " + request.getMatriculaId() + " no existe en el sistema.");
        }

        HojaVidaEstudiante hojaVidaEstudiante = new HojaVidaEstudiante();
        hojaVidaEstudiante.setEstudianteUsuRut(request.getEstudianteUsuRut());
        hojaVidaEstudiante.setMatriculaId(request.getMatriculaId());
        hojaVidaEstudiante.setEstado(request.getEstado());

        HojaVidaEstudiante guardado = hojaVidaRepository.save(hojaVidaEstudiante);
        return mapearADTO(guardado);
    }

    public HojaVidaEstudianteDTO actualizarHojaVida(Long idHojaVida, HojaVidaEstudianteDTO request) {
        // Validamos que el estudiante exista en Autenticacion
        if (!existeEstudiante(request.getEstudianteUsuRut())) {
            throw new IllegalArgumentException("No se puede actualizar: el estudiante con RUT " + request.getEstudianteUsuRut() + " no existe en Autenticacion.");
        }
        // Validamos que la matricula exista en GestionMatricula
        if (!existeMatricula(request.getMatriculaId())) {
            throw new IllegalArgumentException("No se puede actualizar: la matricula con ID " + request.getMatriculaId() + " no existe en el sistema.");
        }

        HojaVidaEstudiante hojaVidaEstudiante = hojaVidaRepository.findById(idHojaVida)
                .orElseThrow(() -> new ResourceNotFoundException("Hoja de vida no encontrada: " + idHojaVida));

        hojaVidaEstudiante.setEstudianteUsuRut(request.getEstudianteUsuRut());
        hojaVidaEstudiante.setMatriculaId(request.getMatriculaId());
        hojaVidaEstudiante.setEstado(request.getEstado());

        HojaVidaEstudiante actualizado = hojaVidaRepository.save(hojaVidaEstudiante);
        return mapearADTO(actualizado);
    }

    public void eliminarHojaVida(Long idHojaVida) {
        if (!hojaVidaRepository.existsById(idHojaVida)) {
            throw new ResourceNotFoundException("Hoja de vida no encontrada: " + idHojaVida);
        }
        hojaVidaRepository.deleteById(idHojaVida);
    }

    // Verifica si el estudiante existe en Autenticacion y tiene el rol correcto
    private boolean existeEstudiante(Long rut) {
        try {
            EstudianteDTO estudiante = autenticacionRestClient.get()
                    .uri("/estudiantes/{rut}", rut)
                    .retrieve()
                    .body(EstudianteDTO.class);

            return estudiante != null
                && estudiante.rol() != null
                && "ROLE_ESTUDIANTE".equalsIgnoreCase(estudiante.rol().rolNombre());
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el microservicio de Autenticacion", e);
        }
    }

    // Verifica si la matricula existe en el microservicio de GestionMatricula
    private boolean existeMatricula(Long idMatricula) {
        try {
            MatriculaDTO matricula = matriculaRestClient.get()
                    .uri("/api/matriculas/{id}", idMatricula)
                    .retrieve()
                    .body(MatriculaDTO.class);
            return matricula != null;
        } catch (HttpClientErrorException.NotFound e) {
            // GestionMatricula respondio 404, la matricula no existe
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el microservicio de GestionMatricula", e);
        }
    }

    // Helper
    private HojaVidaEstudianteDTO mapearADTO(HojaVidaEstudiante entidad) {
        return new HojaVidaEstudianteDTO(
                entidad.getIdHojaVida(),
                entidad.getEstudianteUsuRut(),
                entidad.getMatriculaId(),
                entidad.getEstado()
        );
    }
}