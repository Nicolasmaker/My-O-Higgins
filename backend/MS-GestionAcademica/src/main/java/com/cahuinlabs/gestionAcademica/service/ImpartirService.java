package com.cahuinlabs.gestionAcademica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cahuinlabs.gestionAcademica.models.entities.Asignatura;
import com.cahuinlabs.gestionAcademica.models.entities.Curso;
import com.cahuinlabs.gestionAcademica.models.entities.Impartir;
import com.cahuinlabs.gestionAcademica.models.request.Impartir.CrearImpartirRequest;
import com.cahuinlabs.gestionAcademica.repository.AsignaturaRepository;
import com.cahuinlabs.gestionAcademica.repository.CursoRepository;
import com.cahuinlabs.gestionAcademica.repository.ImpartirRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ImpartirService {

    @Autowired
    private ImpartirRepository impartirRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private CursoRepository cursoRepository;

 //CREAR (asigna un docente a una asignatura dictada en un curso)
    public Impartir crear(CrearImpartirRequest request) {
        Asignatura asignatura = asignaturaRepository.findById(request.getIdAsignatura())
                .orElseThrow(() -> new RuntimeException("Error: La Asignatura con ID " + request.getIdAsignatura() + " no existe."));

        Curso curso = cursoRepository.findById(request.getIdCurso())
                .orElseThrow(() -> new RuntimeException("Error: El Curso con ID " + request.getIdCurso() + " no existe."));

        Impartir impartir = new Impartir();
        impartir.setDocenteUsuRut(request.getDocenteUsuRut());
        impartir.setAsignatura(asignatura);
        impartir.setCurso(curso);

        return impartirRepository.save(impartir);
    }

 //LISTAR TODOS
    public List<Impartir> listarTodos() {
        return impartirRepository.findAll();
    }

 //LISTAR POR DOCENTE
    public List<Impartir> listarPorDocente(Integer docenteUsuRut) {
        return impartirRepository.findByDocenteUsuRut(docenteUsuRut);
    }

 //BUSCAR POR ID
    public Optional<Impartir> buscarPorId(Integer id) {
        return impartirRepository.findById(id);
    }

 //ELIMINAR
    public void eliminar(Integer id) {
        impartirRepository.deleteById(id);
    }
}
