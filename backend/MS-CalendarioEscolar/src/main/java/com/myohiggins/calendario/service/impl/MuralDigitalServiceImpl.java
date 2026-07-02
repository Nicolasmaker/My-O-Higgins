package com.myohiggins.calendario.service.impl;

import com.myohiggins.calendario.dto.MuralDigitalDTO;
import com.myohiggins.calendario.entity.MuralDigital;
import com.myohiggins.calendario.repository.MuralDigitalRepository;
import com.myohiggins.calendario.service.MuralDigitalService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación de la lógica de negocio del Mural Digital.
 * CRUD simple: las publicaciones se corrigen en título/contenido,
 * pero autoría y fecha de publicación no se modifican (trazabilidad).
 */
@Service
public class MuralDigitalServiceImpl implements MuralDigitalService {

    private final MuralDigitalRepository repository;

    public MuralDigitalServiceImpl(MuralDigitalRepository repository) {
        this.repository = repository;
    }

    @Override
    public MuralDigitalDTO crear(MuralDigitalDTO dto) {
        MuralDigital mural = new MuralDigital();
        mural.setTitulo(dto.getTitulo());
        mural.setContenido(dto.getContenido());
        mural.setFechaPublicacion(dto.getFechaPublicacion()); // null → @PrePersist pone hoy
        mural.setFuncionarioUsuRut(dto.getFuncionarioUsuRut());
        return toDto(repository.save(mural));
    }

    @Override
    public List<MuralDigitalDTO> obtenerTodos() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public MuralDigitalDTO obtenerPorId(Long id) {
        return toDto(buscar(id));
    }

    @Override
    public MuralDigitalDTO actualizar(Long id, MuralDigitalDTO dto) {
        MuralDigital mural = buscar(id);
        // Solo se corrige el contenido de la publicación: no se toca autoría ni fecha
        mural.setTitulo(dto.getTitulo());
        mural.setContenido(dto.getContenido());
        return toDto(repository.save(mural));
    }

    @Override
    public void eliminar(Long id) {
        repository.delete(buscar(id));
    }

    private MuralDigital buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publicación de mural no encontrada: " + id));
    }

    private MuralDigitalDTO toDto(MuralDigital m) {
        return new MuralDigitalDTO(
                m.getIdMurDig(),
                m.getTitulo(),
                m.getContenido(),
                m.getFechaPublicacion(),
                m.getFuncionarioUsuRut()
        );
    }
}
