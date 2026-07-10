package com.cahuinlabs.hojadevida.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cahuinlabs.hojadevida.dto.AntecedentesAcademicosDTO;
import com.cahuinlabs.hojadevida.exception.ResourceNotFoundException;
import com.cahuinlabs.hojadevida.model.AntecedentesAcademicos;
import com.cahuinlabs.hojadevida.model.HojaVidaEstudiante;
import com.cahuinlabs.hojadevida.repository.AntecedentesAcademicosRepository;
import com.cahuinlabs.hojadevida.repository.HojaVidaRepository;

@Service
public class AntecedentesAcademicosService {

    private final AntecedentesAcademicosRepository repository;
    private final HojaVidaRepository hojaVidaRepository;

    public AntecedentesAcademicosService(AntecedentesAcademicosRepository repository, HojaVidaRepository hojaVidaRepository) {
        this.repository = repository;
        this.hojaVidaRepository = hojaVidaRepository;
    }

    public List<AntecedentesAcademicosDTO> obtenerTodos() {
        return repository.findAll().stream().map(this::mapearADTO).collect(Collectors.toList());
    }

    public AntecedentesAcademicosDTO obtenerPorId(Long id) {
        AntecedentesAcademicos entidad = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Antecedente academico no encontrado con ID: " + id));
        return mapearADTO(entidad);
    }

    public AntecedentesAcademicosDTO crear(AntecedentesAcademicosDTO request) {
        HojaVidaEstudiante hoja = hojaVidaRepository.findById(request.getIdHojaVida())
                .orElseThrow(() -> new ResourceNotFoundException("Hoja de vida no encontrada con ID: " + request.getIdHojaVida()));
        
        AntecedentesAcademicos entidad = new AntecedentesAcademicos();
        entidad.setAnioEscolar(request.getAnioEscolar());
        entidad.setPromedioGeneralActual(request.getPromedioGeneralActual());
        entidad.setSituacionFinalAprobacion(request.getSituacionFinalAprobacion());
        entidad.setHojaVida(hoja);
        
        return mapearADTO(repository.save(entidad));
    }

    public AntecedentesAcademicosDTO actualizar(Long id, AntecedentesAcademicosDTO request) {
        AntecedentesAcademicos entidad = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Antecedente academico no encontrado con ID: " + id));
        
        HojaVidaEstudiante hoja = hojaVidaRepository.findById(request.getIdHojaVida())
                .orElseThrow(() -> new ResourceNotFoundException("Hoja de vida no encontrada con ID: " + request.getIdHojaVida()));
        
        entidad.setAnioEscolar(request.getAnioEscolar());
        entidad.setPromedioGeneralActual(request.getPromedioGeneralActual());
        entidad.setSituacionFinalAprobacion(request.getSituacionFinalAprobacion());
        entidad.setHojaVida(hoja);
        
        return mapearADTO(repository.save(entidad));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Antecedente academico no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    private AntecedentesAcademicosDTO mapearADTO(AntecedentesAcademicos entidad) {
        return new AntecedentesAcademicosDTO(
                entidad.getIdAntAcad(),
                entidad.getAnioEscolar(),
                entidad.getPromedioGeneralActual(),
                entidad.getSituacionFinalAprobacion(),
                entidad.getHojaVida() != null ? entidad.getHojaVida().getIdHojaVida() : null
        );
    }
}
