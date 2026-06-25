package com.cahuinlabs.gestionAcademica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;

import com.cahuinlabs.gestionAcademica.dto.FuncionarioDTO;
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

    // RestClient para comunicarse con el microservicio de Autenticacion
    private final RestClient autenticacionRestClient;

    public EvaluacionService(RestClient autenticacionRestClient) {
        this.autenticacionRestClient = autenticacionRestClient;
    }

    public Evaluacion crearEvaluacion(CrearEvaluacionRequest request) {
        // Valida que el docente exista en el microservicio de Autenticacion
        if (!existeDocente(request.getDocenteUsuRut())) {
            throw new IllegalArgumentException("No se puede crear la evaluacion: el docente con RUT " + request.getDocenteUsuRut() + " no existe en el sistema.");
        }

        // Valida que la asignatura exista
        Asignatura asignatura = asignaturaRepository.findById(request.getIdAsignatura())
                .orElseThrow(() -> new RuntimeException("Error: La Asignatura con ID " + request.getIdAsignatura() + " no existe."));

        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setEvaNom(request.getEvaNom());
        evaluacion.setEvaFecha(request.getEvaFec());
        evaluacion.setEvaPeriodoAcad(request.getEvaPerioAcad());
        evaluacion.setEvaTipo(request.getEvaTip());
        evaluacion.setDocenteUsuRut(request.getDocenteUsuRut());
        evaluacion.setAsignatura(asignatura);

        return evaluacionRepository.save(evaluacion);
    }

    public Evaluacion actualizarEvaluacion(Integer id, ActualizarEvaluacionRequest request) {
        // Valida que el docente exista en el microservicio de Autenticacion
        if (!existeDocente(request.getDocenteUsuRut())) {
            throw new IllegalArgumentException("No se puede actualizar la evaluacion: el docente con RUT " + request.getDocenteUsuRut() + " no existe en el sistema.");
        }

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

    // Metodo privado que pregunta al microservicio de Autenticacion si el docente/funcionario existe
    // Devuelve true si existe, false si no fue encontrado
    private boolean existeDocente(Integer rut) {
        try {
            FuncionarioDTO funcionario = autenticacionRestClient.get()
                    .uri("/funcionarios/{rut}", rut)
                    .retrieve()
                    .body(FuncionarioDTO.class);

            return funcionario != null;
        } catch (HttpClientErrorException.NotFound e) {
            // El microservicio de Autenticacion respondio 404, el docente no existe
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el microservicio de Autenticacion: " + e.getMessage());
        }
    }
}