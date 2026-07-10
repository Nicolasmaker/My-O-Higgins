package com.cahuinlabs.hojadevida.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cahuinlabs.hojadevida.dto.AntecedentesApoderadoDTO;
import com.cahuinlabs.hojadevida.exception.ResourceNotFoundException;
import com.cahuinlabs.hojadevida.model.AntecedentesApoderado;
import com.cahuinlabs.hojadevida.model.HojaVidaEstudiante;
import com.cahuinlabs.hojadevida.repository.AntecedentesApoderadoRepository;
import com.cahuinlabs.hojadevida.repository.HojaVidaRepository;

@Service
public class AntecedentesApoderadoService {

    private final AntecedentesApoderadoRepository repository;
    private final HojaVidaRepository hojaVidaRepository;

    public AntecedentesApoderadoService(AntecedentesApoderadoRepository repository, HojaVidaRepository hojaVidaRepository) {
        this.repository = repository;
        this.hojaVidaRepository = hojaVidaRepository;
    }

    public List<AntecedentesApoderadoDTO> obtenerTodos() {
        return repository.findAll().stream().map(this::mapearADTO).collect(Collectors.toList());
    }

    public AntecedentesApoderadoDTO obtenerPorId(Long id) {
        AntecedentesApoderado entidad = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Antecedente apoderado no encontrado con ID: " + id));
        return mapearADTO(entidad);
    }

    public AntecedentesApoderadoDTO crear(AntecedentesApoderadoDTO request) {
        HojaVidaEstudiante hoja = hojaVidaRepository.findById(request.getIdHojaVida())
                .orElseThrow(() -> new ResourceNotFoundException("Hoja de vida no encontrada con ID: " + request.getIdHojaVida()));
        
        AntecedentesApoderado entidad = new AntecedentesApoderado();
        entidad.setNombre(request.getNombre());
        entidad.setProfesion(request.getProfesion());
        entidad.setTelefono(request.getTelefono());
        entidad.setDireccion(request.getDireccion());
        entidad.setLugarTrabajo(request.getLugarTrabajo());
        entidad.setDisponibilidadHoraria(request.getDisponibilidadHoraria());
        entidad.setHojaVida(hoja);
        
        return mapearADTO(repository.save(entidad));
    }

    public AntecedentesApoderadoDTO actualizar(Long id, AntecedentesApoderadoDTO request) {
        AntecedentesApoderado entidad = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Antecedente apoderado no encontrado con ID: " + id));
        
        HojaVidaEstudiante hoja = hojaVidaRepository.findById(request.getIdHojaVida())
                .orElseThrow(() -> new ResourceNotFoundException("Hoja de vida no encontrada con ID: " + request.getIdHojaVida()));
        
        entidad.setNombre(request.getNombre());
        entidad.setProfesion(request.getProfesion());
        entidad.setTelefono(request.getTelefono());
        entidad.setDireccion(request.getDireccion());
        entidad.setLugarTrabajo(request.getLugarTrabajo());
        entidad.setDisponibilidadHoraria(request.getDisponibilidadHoraria());
        entidad.setHojaVida(hoja);
        
        return mapearADTO(repository.save(entidad));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Antecedente apoderado no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    private AntecedentesApoderadoDTO mapearADTO(AntecedentesApoderado entidad) {
        return new AntecedentesApoderadoDTO(
                entidad.getIdAntApo(),
                entidad.getNombre(),
                entidad.getProfesion(),
                entidad.getTelefono(),
                entidad.getDireccion(),
                entidad.getLugarTrabajo(),
                entidad.getDisponibilidadHoraria(),
                entidad.getHojaVida() != null ? entidad.getHojaVida().getIdHojaVida() : null
        );
    }
}
