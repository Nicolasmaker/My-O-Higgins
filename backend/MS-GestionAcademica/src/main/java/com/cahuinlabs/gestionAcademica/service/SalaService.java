package com.cahuinlabs.gestionAcademica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cahuinlabs.gestionAcademica.models.entities.Sala;
import com.cahuinlabs.gestionAcademica.models.request.SalaRequest;
import com.cahuinlabs.gestionAcademica.repository.SalaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    public Sala crearSala(SalaRequest salaRequest) {
        Sala sala = new Sala();
        sala.setSalLetra(salaRequest.getSalLetra());
        sala.setSalaCapacidad(salaRequest.getSalaCapacidad());
        
        return salaRepository.save(sala);
    }

    public Sala actualizarSala(Integer id, SalaRequest request) {
        Sala salaExistente = salaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sala no encontrada"));
    
        salaExistente.setSalLetra(request.getSalLetra());
        salaExistente.setSalaCapacidad(request.getSalaCapacidad());
    
        return salaRepository.save(salaExistente);
    }

    public List<Sala> listarTodas() {
        return salaRepository.findAll();
    }

    public Optional<Sala> buscarPorId(Integer id) {
        return salaRepository.findById(id);
    }

    public void eliminar(Integer id) {
        salaRepository.deleteById(id);
    }
}
