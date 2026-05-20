package com.myohiggins.calendario.service.impl;

import com.myohiggins.calendario.dto.CalendarioEstudiantilDTO;
import com.myohiggins.calendario.entity.CalendarioEstudiantil;
import com.myohiggins.calendario.repository.CalendarioEstudiantilRepository;
import com.myohiggins.calendario.service.CalendarioEstudiantilService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de la lógica de negocio del servicio CalendarioEstudiantilService.
 */
@Service
public class CalendarioEstudiantilServiceImpl implements CalendarioEstudiantilService {

    private final CalendarioEstudiantilRepository repository;

    // Inyección de dependencias mediante constructor (recomendado)
    public CalendarioEstudiantilServiceImpl(CalendarioEstudiantilRepository repository) {
        this.repository = repository;
    }

    @Override
    public CalendarioEstudiantilDTO crear(CalendarioEstudiantilDTO dto) {
        CalendarioEstudiantil entidad = mapearAEntidad(dto);
        CalendarioEstudiantil guardado = repository.save(entidad);
        return mapearADTO(guardado);
    }

    @Override
    public List<CalendarioEstudiantilDTO> obtenerTodos() {
        List<CalendarioEstudiantil> lista = repository.findAll();
        // Convierte la lista de entidades a lista de DTOs
        return lista.stream().map(this::mapearADTO).collect(Collectors.toList());
    }

    @Override
    public CalendarioEstudiantilDTO obtenerPorId(Long id) {
        CalendarioEstudiantil entidad = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calendario no encontrado con ID: " + id));
        return mapearADTO(entidad);
    }

    @Override
    public CalendarioEstudiantilDTO actualizar(Long id, CalendarioEstudiantilDTO dto) {
        CalendarioEstudiantil entidad = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calendario no encontrado con ID: " + id));
        
        // Actualizamos los campos de la entidad existente
        entidad.setTituloEvento(dto.getTituloEvento());
        entidad.setTipoEvento(dto.getTipoEvento());
        entidad.setFechaInicio(dto.getFechaInicio());
        entidad.setFechaFin(dto.getFechaFin());
        entidad.setIdMuralDigital(dto.getIdMuralDigital());
        entidad.setIdAsignatura(dto.getIdAsignatura());
        entidad.setDescripcionEvento(dto.getDescripcionEvento());

        CalendarioEstudiantil actualizado = repository.save(entidad);
        return mapearADTO(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        CalendarioEstudiantil entidad = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calendario no encontrado con ID: " + id));
        repository.delete(entidad);
    }

    // ==========================================
    // Métodos auxiliares (Mappers) Entity <-> DTO
    // ==========================================
    
    private CalendarioEstudiantilDTO mapearADTO(CalendarioEstudiantil entidad) {
        CalendarioEstudiantilDTO dto = new CalendarioEstudiantilDTO();
        dto.setIdCalEst(entidad.getIdCalEst());
        dto.setTituloEvento(entidad.getTituloEvento());
        dto.setTipoEvento(entidad.getTipoEvento());
        dto.setFechaInicio(entidad.getFechaInicio());
        dto.setFechaFin(entidad.getFechaFin());
        dto.setIdMuralDigital(entidad.getIdMuralDigital());
        dto.setIdAsignatura(entidad.getIdAsignatura());
        dto.setDescripcionEvento(entidad.getDescripcionEvento());
        return dto;
    }

    private CalendarioEstudiantil mapearAEntidad(CalendarioEstudiantilDTO dto) {
        CalendarioEstudiantil entidad = new CalendarioEstudiantil();
        entidad.setIdCalEst(dto.getIdCalEst()); // Podria ser nulo al crear
        entidad.setTituloEvento(dto.getTituloEvento());
        entidad.setTipoEvento(dto.getTipoEvento());
        entidad.setFechaInicio(dto.getFechaInicio());
        entidad.setFechaFin(dto.getFechaFin());
        entidad.setIdMuralDigital(dto.getIdMuralDigital());
        entidad.setIdAsignatura(dto.getIdAsignatura());
        entidad.setDescripcionEvento(dto.getDescripcionEvento());
        return entidad;
    }
}
