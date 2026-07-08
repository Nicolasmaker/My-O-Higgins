package com.cahuinlabs.gestionAcademica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cahuinlabs.gestionAcademica.models.entities.Nivel;
import com.cahuinlabs.gestionAcademica.models.request.NivelRequest;
import com.cahuinlabs.gestionAcademica.repository.NivelRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NivelService {

    @Autowired
    private NivelRepository nivelRepository;

    public Nivel crearNivel(NivelRequest nivelRequest) {
        Nivel nivel = new Nivel();
        nivel.setNivNum(nivelRequest.getNivNum());
        nivel.setNivTipo(nivelRequest.getNivTipo());

        return nivelRepository.save(nivel);
    }

    public Nivel actualizarNivel(Integer id, NivelRequest request) {
        Nivel nivelExistente = nivelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));
    
        nivelExistente.setNivNum(request.getNivNum());
        nivelExistente.setNivTipo(request.getNivTipo());

        return nivelRepository.save(nivelExistente);
    }

    public List<Nivel> listarTodos() {
        return nivelRepository.findAll();
    }

    public Optional<Nivel> buscarPorId(Integer id) {
        return nivelRepository.findById(id);
    }

    public void eliminar(Integer id) {
        nivelRepository.deleteById(id);
    }
}
