package com.cahuinlabs.gestionAcademica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cahuinlabs.gestionAcademica.models.entities.Asignatura;
import com.cahuinlabs.gestionAcademica.models.entities.Evaluacion;
import com.cahuinlabs.gestionAcademica.models.request.Evaluacion.ActualizarEvaluacionRequest;
import com.cahuinlabs.gestionAcademica.models.request.Evaluacion.CrearEvaluacionRequest;
import com.cahuinlabs.gestionAcademica.repository.AsignaturaRepository;
import com.cahuinlabs.gestionAcademica.repository.EvaluacionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    public Evaluacion crearEvaluacion(CrearEvaluacionRequest request) {
     //Valida que la asignatura exista
        Asignatura asignatura = asignaturaRepository.findById(request.getIdAsignatura())
                .orElseThrow(() -> new RuntimeException("Error: La Asignatura con ID " + request.getIdAsignatura() + " no existe."));

        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setEvaNom(request.getEvaNom());
        evaluacion.setEvaFecha(request.getEvaFec());
        evaluacion.setEvaPeriodoAcad(request.getEvaPerioAcad());
        evaluacion.setEvaTipo(request.getEvaTip());
        evaluacion.setDocenteUsuRut(request.getDocenteUsuRut()); // Se guarda directo el número
        evaluacion.setAsignatura(asignatura);

        return evaluacionRepository.save(evaluacion);
    }

    public Evaluacion actualizarEvaluacion(Integer id, ActualizarEvaluacionRequest request) {
        Evaluacion evaluacionExistente = evaluacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: La Evaluacion con ID " + id + " no existe."));

        evaluacionExistente.setEvaNom(request.getEvaNom());
        evaluacionExistente.setEvaFecha(request.getEvaFec());
        evaluacionExistente.setEvaPeriodoAcad(request.getEvaPerioAcad());
        evaluacionExistente.setEvaTipo(request.getEvaTip());
        evaluacionExistente.setDocenteUsuRut(request.getDocenteUsuRut());

        return evaluacionRepository.save(evaluacionExistente);
    }

    public List<Evaluacion> listarTodas() {
        return evaluacionRepository.findAll();
    }

    public Optional<Evaluacion> buscarPorId(Integer id) {
        return evaluacionRepository.findById(id);
    }

    public void eliminar(Integer id) {
        evaluacionRepository.deleteById(id);
    }
}