package com.cahuinlabs.hojadevida.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cahuinlabs.hojadevida.dto.AntecedentesMedicosDTO;
import com.cahuinlabs.hojadevida.exception.ResourceNotFoundException;
import com.cahuinlabs.hojadevida.model.AntecedentesMedicos;
import com.cahuinlabs.hojadevida.model.HojaVidaEstudiante;
import com.cahuinlabs.hojadevida.repository.AntecedentesMedicosRepository;
import com.cahuinlabs.hojadevida.repository.HojaVidaRepository;

@Service
public class AntecedentesMedicosService {

    private final AntecedentesMedicosRepository antecedentesRepository;
    private final HojaVidaRepository hojaVidaRepository;

    public AntecedentesMedicosService(AntecedentesMedicosRepository antecedentesRepository, HojaVidaRepository hojaVidaRepository) {
        this.antecedentesRepository = antecedentesRepository;
        this.hojaVidaRepository = hojaVidaRepository;
    }

    public List<AntecedentesMedicosDTO> obtenerTodos() {
        return antecedentesRepository.findAll().stream().map(this::mapearADTO).collect(Collectors.toList());
    }

    public AntecedentesMedicosDTO obtenerPorId(Long id) {
        AntecedentesMedicos entidad = antecedentesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Antecedente medico no encontrado con ID: " + id));
        return mapearADTO(entidad);
    }

    public AntecedentesMedicosDTO crear(AntecedentesMedicosDTO request) {
        HojaVidaEstudiante hoja = hojaVidaRepository.findById(request.getIdHojaVida())
                .orElseThrow(() -> new ResourceNotFoundException("Hoja de vida no encontrada con ID: " + request.getIdHojaVida()));
        
        AntecedentesMedicos entidad = new AntecedentesMedicos();
        entidad.setAlergias(request.getAlergias());
        entidad.setCondicionesMedicas(request.getCondicionesMedicas());
        entidad.setMedicamentos(request.getMedicamentos());
        entidad.setTipoSangre(request.getTipoSangre());
        entidad.setObservaciones(request.getObservaciones());
        entidad.setHojaVida(hoja);
        
        return mapearADTO(antecedentesRepository.save(entidad));
    }

    public AntecedentesMedicosDTO actualizar(Long id, AntecedentesMedicosDTO request) {
        AntecedentesMedicos entidad = antecedentesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Antecedente medico no encontrado con ID: " + id));
        
        HojaVidaEstudiante hoja = hojaVidaRepository.findById(request.getIdHojaVida())
                .orElseThrow(() -> new ResourceNotFoundException("Hoja de vida no encontrada con ID: " + request.getIdHojaVida()));
        
        entidad.setAlergias(request.getAlergias());
        entidad.setCondicionesMedicas(request.getCondicionesMedicas());
        entidad.setMedicamentos(request.getMedicamentos());
        entidad.setTipoSangre(request.getTipoSangre());
        entidad.setObservaciones(request.getObservaciones());
        entidad.setHojaVida(hoja);
        
        return mapearADTO(antecedentesRepository.save(entidad));
    }

    public void eliminar(Long id) {
        if (!antecedentesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Antecedente medico no encontrado con ID: " + id);
        }
        antecedentesRepository.deleteById(id);
    }

    private AntecedentesMedicosDTO mapearADTO(AntecedentesMedicos entidad) {
        return new AntecedentesMedicosDTO(
                entidad.getIdAntMed(),
                entidad.getAlergias(),
                entidad.getCondicionesMedicas(),
                entidad.getMedicamentos(),
                entidad.getTipoSangre(),
                entidad.getObservaciones(),
                entidad.getHojaVida() != null ? entidad.getHojaVida().getIdHojaVida() : null
        );
    }
}
