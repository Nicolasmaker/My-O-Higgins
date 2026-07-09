package com.cahuinlabs.gestionAcademica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cahuinlabs.gestionAcademica.models.entities.Evaluacion;
import com.cahuinlabs.gestionAcademica.models.entities.Notas;
import com.cahuinlabs.gestionAcademica.models.request.Nota.ActualizarNotaRequest;
import com.cahuinlabs.gestionAcademica.models.request.Nota.CrearNotaRequest;
import com.cahuinlabs.gestionAcademica.repository.EvaluacionRepository;
import com.cahuinlabs.gestionAcademica.repository.NotasRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NotaService {

    @Autowired
    private NotasRepository notasRepository;

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    public Notas crearNota(CrearNotaRequest request) {
        Evaluacion evaluacion = evaluacionRepository.findById(request.getIdEvaluacion())
                .orElseThrow(() -> new RuntimeException("Error: La Evaluación con ID " + request.getIdEvaluacion() + " no existe."));

        Notas nota = new Notas();
        nota.setNotCalif(request.getNotCalif());
        nota.setNotFechaRegistrada(request.getNotFechaReg());
        nota.setEvaluacion(evaluacion);
        nota.setEstudianteUsuRut(request.getEstudianteUsuRut());

        return notasRepository.save(nota);
    }

    public Notas actualizarNota(Integer id, ActualizarNotaRequest request) {
        Notas notaExistente = notasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: La Nota con ID " + id + " no existe."));

        notaExistente.setNotCalif(request.getNotCalif());
        notaExistente.setNotFechaRegistrada(request.getNotFechaReg());

        return notasRepository.save(notaExistente);
    }

    public List<Notas> listarTodas() {
        return notasRepository.findAll();
    }

    public Optional<Notas> buscarPorId(Integer id) {
        return notasRepository.findById(id);
    }

    public List<Notas> listarPorEstudiante(Integer estudianteUsuRut) {
        return notasRepository.findByEstudianteUsuRut(estudianteUsuRut);
    }

    public List<Notas> listarPorEvaluacion(Integer idEva) {
        return notasRepository.findByEvaluacion_IdEva(idEva);
    }

    public void eliminar(Integer id) {
        notasRepository.deleteById(id);
    }
}
