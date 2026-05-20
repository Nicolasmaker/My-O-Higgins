package com.cahuinlabs.anotaciones.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cahuinlabs.anotaciones.models.entities.Anotacion;
import com.cahuinlabs.anotaciones.models.request.AnotacionDTO;
import com.cahuinlabs.anotaciones.repository.AnotacionRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AnotacionService {

    @Autowired
    private AnotacionRepository anotacionRepository;

    // crea una nueva anotacion asignando tipo, descripcion, fecha actual y relacionandola con la hoja de vida
    public Anotacion crearAnotacion(AnotacionDTO dto) {
        Anotacion anotacion = new Anotacion();
        anotacion.setAnotTip(dto.getAnotTip());
        anotacion.setAnotDes(dto.getAnotDes());
        anotacion.setAnotFec(LocalDate.now());
        anotacion.setFuncionarioUsuRut(dto.getFuncionarioUsuRut());
        anotacion.setIdHojaVida(dto.getIdHojaVida());
        return anotacionRepository.save(anotacion);
    }

    // consulta todas las anotaciones de un estudiante filtrando por su hoja de vida
    public List<Anotacion> obtenerPorHojaVida(Long idHojaVida) {
        return anotacionRepository.findByIdHojaVida(idHojaVida);
    }

    // edita tipo y descripcion de una anotacion existente, preservando fecha y autor
    public Anotacion modificarAnotacion(Long idAnot, AnotacionDTO dto) {
        Anotacion anotacion = anotacionRepository.findById(idAnot)
                .orElseThrow(() -> new EntityNotFoundException("Anotacion no encontrada: " + idAnot));
        anotacion.setAnotTip(dto.getAnotTip());
        anotacion.setAnotDes(dto.getAnotDes());
        return anotacionRepository.save(anotacion);
    }

    // elimina una anotacion si existe, lanzando excepcion en caso contrario
    public void eliminarAnotacion(Long idAnot) {
        if (!anotacionRepository.existsById(idAnot)) {
            throw new EntityNotFoundException("Anotacion no encontrada: " + idAnot);
        }
        anotacionRepository.deleteById(idAnot);
    }
    // obtiene todas las anotaciones del sistema sin filtros
    public List<Anotacion> obtenerTodas() {
        return anotacionRepository.findAll();
    }   
}
