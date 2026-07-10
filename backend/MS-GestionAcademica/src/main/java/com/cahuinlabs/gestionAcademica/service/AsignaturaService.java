package com.cahuinlabs.gestionAcademica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cahuinlabs.gestionAcademica.models.entities.Asignatura;
import com.cahuinlabs.gestionAcademica.models.request.AsignaturaRequest;
import com.cahuinlabs.gestionAcademica.repository.AsignaturaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AsignaturaService {

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    public Asignatura crearAsignatura(AsignaturaRequest asignaturaRequest) {
        
        Asignatura asignatura = new Asignatura();
        asignatura.setAsiNombre(asignaturaRequest.getAsiNom());
        asignatura.setAsiDescripcion(asignaturaRequest.getAsiDes());
        
        return asignaturaRepository.save(asignatura);
    }

    public Asignatura actualizarAsignatura(Integer id, AsignaturaRequest request) {
     //Busca si existe
        Asignatura asignaturaExistente = asignaturaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Asignatura no encontrada"));
    
     //Actualiza
        asignaturaExistente.setAsiNombre(request.getAsiNom());
        asignaturaExistente.setAsiDescripcion(request.getAsiDes());
    
        return asignaturaRepository.save(asignaturaExistente);
    }


    public List<Asignatura> listarTodas() {
        return asignaturaRepository.findAll();
    }

    public Optional<Asignatura> buscarPorId(Integer id) {
        return asignaturaRepository.findById(id);
    }

    public void eliminar(Integer id) {
        asignaturaRepository.deleteById(id);
    }
}
