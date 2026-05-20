package com.cahuinlabs.hojadevida.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cahuinlabs.hojadevida.dto.HojaVidaEstudianteDTO;
import com.cahuinlabs.hojadevida.exception.ResourceNotFoundException;
import com.cahuinlabs.hojadevida.model.HojaVidaEstudiante;
import com.cahuinlabs.hojadevida.repository.HojaVidaRepository;

@Service
public class HojaVidaService {

    private final HojaVidaRepository hojaVidaRepository;

    public HojaVidaService(HojaVidaRepository hojaVidaRepository) {
        this.hojaVidaRepository = hojaVidaRepository;
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
        HojaVidaEstudiante hojaVidaEstudiante = new HojaVidaEstudiante();
        hojaVidaEstudiante.setEstudianteUsuRut(request.getEstudianteUsuRut());
        hojaVidaEstudiante.setMatriculaId(request.getMatriculaId());
        
        HojaVidaEstudiante guardado = hojaVidaRepository.save(hojaVidaEstudiante);
        return mapearADTO(guardado);
    }

    public HojaVidaEstudianteDTO actualizarHojaVida(Long idHojaVida, HojaVidaEstudianteDTO request) {
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

    // Helper
    private HojaVidaEstudianteDTO mapearADTO(HojaVidaEstudiante entidad) {
        return new HojaVidaEstudianteDTO(
                entidad.getIdHojaVida(),
                entidad.getEstudianteUsuRut(),
                entidad.getMatriculaId()
        );
    }
}