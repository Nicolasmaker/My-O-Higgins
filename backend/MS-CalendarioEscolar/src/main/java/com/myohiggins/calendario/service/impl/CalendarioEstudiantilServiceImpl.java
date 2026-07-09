package com.myohiggins.calendario.service.impl;

import com.myohiggins.calendario.dto.AsignaturaDTO;
import com.myohiggins.calendario.dto.CalendarioEstudiantilDTO;
import com.myohiggins.calendario.entity.CalendarioEstudiantil;
import com.myohiggins.calendario.repository.CalendarioEstudiantilRepository;
import com.myohiggins.calendario.service.CalendarioEstudiantilService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de la lógica de negocio del servicio CalendarioEstudiantilService.
 */
@Service
public class CalendarioEstudiantilServiceImpl implements CalendarioEstudiantilService {

    private final CalendarioEstudiantilRepository repository;

    // RestClient para comunicarse con el microservicio de Gestion Academica
    private final RestClient gestionAcademicaRestClient;

    // Inyección de dependencias mediante constructor (recomendado)
    public CalendarioEstudiantilServiceImpl(CalendarioEstudiantilRepository repository,
                                            RestClient gestionAcademicaRestClient) {
        this.repository = repository;
        this.gestionAcademicaRestClient = gestionAcademicaRestClient;
    }

    @Override
    public CalendarioEstudiantilDTO crear(CalendarioEstudiantilDTO dto) {
        // Institucional/Actividad no llevan asignatura ("No aplica"): se omite la validación.
        if (dto.getIdAsignatura() != null && !existeAsignatura(dto.getIdAsignatura())) {
            throw new IllegalArgumentException("No se puede crear el evento: la asignatura con ID " + dto.getIdAsignatura() + " no existe en el sistema.");
        }

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
        // Institucional/Actividad no llevan asignatura ("No aplica"): se omite la validación.
        if (dto.getIdAsignatura() != null && !existeAsignatura(dto.getIdAsignatura())) {
            throw new IllegalArgumentException("No se puede actualizar el evento: la asignatura con ID " + dto.getIdAsignatura() + " no existe en el sistema.");
        }

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
        entidad.setCursoId(dto.getCursoId());
        entidad.setDocenteUsuRut(dto.getDocenteUsuRut());
        entidad.setApoderadoUsuRut(dto.getApoderadoUsuRut());
        entidad.setAlumnoRut(dto.getAlumnoRut());

        CalendarioEstudiantil actualizado = repository.save(entidad);
        return mapearADTO(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        CalendarioEstudiantil entidad = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calendario no encontrado con ID: " + id));
        repository.delete(entidad);
    }

    // Verifica si la asignatura existe en el microservicio de Gestion Academica
    private boolean existeAsignatura(Long idAsignatura) {
        try {
            AsignaturaDTO asignatura = gestionAcademicaRestClient.get()
                    .uri("/asignatura/{id}", idAsignatura)
                    .retrieve()
                    .body(AsignaturaDTO.class);
            return asignatura != null;
        } catch (HttpClientErrorException.NotFound e) {
            // El microservicio de Gestion Academica respondio 404, la asignatura no existe
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el microservicio de Gestion Academica: " + e.getMessage());
        }
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
        dto.setCursoId(entidad.getCursoId());
        dto.setDocenteUsuRut(entidad.getDocenteUsuRut());
        dto.setApoderadoUsuRut(entidad.getApoderadoUsuRut());
        dto.setAlumnoRut(entidad.getAlumnoRut());
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
        entidad.setCursoId(dto.getCursoId());
        entidad.setDocenteUsuRut(dto.getDocenteUsuRut());
        entidad.setApoderadoUsuRut(dto.getApoderadoUsuRut());
        entidad.setAlumnoRut(dto.getAlumnoRut());
        return entidad;
    }
}
