package com.cahuinlabs.gestionAcademica.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;

import com.cahuinlabs.gestionAcademica.dto.CalendarioEstudiantilDTO;
import com.cahuinlabs.gestionAcademica.dto.FuncionarioDTO;
import com.cahuinlabs.gestionAcademica.models.entities.Asignatura;
import com.cahuinlabs.gestionAcademica.models.entities.Curso;
import com.cahuinlabs.gestionAcademica.models.entities.Evaluacion;
import com.cahuinlabs.gestionAcademica.models.request.CalendarioEventoRequest;
import com.cahuinlabs.gestionAcademica.models.request.Evaluacion.ActualizarEvaluacionRequest;
import com.cahuinlabs.gestionAcademica.models.request.Evaluacion.CrearEvaluacionRequest;
import com.cahuinlabs.gestionAcademica.repository.AsignaturaRepository;
import com.cahuinlabs.gestionAcademica.repository.CursoRepository;
import com.cahuinlabs.gestionAcademica.repository.EvaluacionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluacionService {

    private static final Logger logger = LoggerFactory.getLogger(EvaluacionService.class);

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    // RestClient para comunicarse con el microservicio de Autenticacion
    private final RestClient autenticacionRestClient;
    // RestClient para sincronizar evaluaciones con MS-CalendarioEscolar
    private final RestClient calendarioRestClient;

    public EvaluacionService(@Qualifier("autenticacionRestClient") RestClient autenticacionRestClient,
                              @Qualifier("calendarioRestClient") RestClient calendarioRestClient) {
        this.autenticacionRestClient = autenticacionRestClient;
        this.calendarioRestClient = calendarioRestClient;
    }

    public Evaluacion crearEvaluacion(CrearEvaluacionRequest request) {
        // Valida que el docente exista en el microservicio de Autenticacion
        if (!existeDocente(request.getDocenteUsuRut())) {
            throw new IllegalArgumentException("No se puede crear la evaluacion: el docente con RUT " + request.getDocenteUsuRut() + " no existe en el sistema.");
        }

        // Valida que la asignatura exista
        Asignatura asignatura = asignaturaRepository.findById(request.getIdAsignatura())
                .orElseThrow(() -> new RuntimeException("Error: La Asignatura con ID " + request.getIdAsignatura() + " no existe."));

        // Valida que el curso exista
        Curso curso = cursoRepository.findById(request.getIdCurso())
                .orElseThrow(() -> new RuntimeException("Error: El Curso con ID " + request.getIdCurso() + " no existe."));

        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setEvaNom(request.getEvaNom());
        evaluacion.setEvaFecha(request.getEvaFec());
        evaluacion.setEvaPeriodoAcad(request.getEvaPerioAcad());
        evaluacion.setEvaTipo(request.getEvaTip());
        evaluacion.setDocenteUsuRut(request.getDocenteUsuRut());
        evaluacion.setAsignatura(asignatura);
        evaluacion.setCurso(curso);

        Evaluacion guardada = evaluacionRepository.save(evaluacion);
        sincronizarConCalendario(guardada);
        return guardada;
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
        if (request.getIdCurso() != null) {
            Curso curso = cursoRepository.findById(request.getIdCurso())
                    .orElseThrow(() -> new RuntimeException("Error: El Curso con ID " + request.getIdCurso() + " no existe."));
            evaluacionExistente.setCurso(curso);
        }

        Evaluacion actualizada = evaluacionRepository.save(evaluacionExistente);
        if (actualizada.getIdCalEst() != null) {
            actualizarEventoCalendario(actualizada);
        } else {
            sincronizarConCalendario(actualizada);
        }
        return actualizada;
    }

    public List<Evaluacion> listarTodas() {
        return evaluacionRepository.findAll();
    }

    public Optional<Evaluacion> buscarPorId(Integer id) {
        return evaluacionRepository.findById(id);
    }

    public void eliminar(Integer id) {
        evaluacionRepository.findById(id).ifPresent(evaluacion -> {
            if (evaluacion.getIdCalEst() != null) {
                eliminarEventoCalendario(evaluacion.getIdCalEst());
            }
        });
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

    // Crea el evento de calendario asociado a la evaluacion y guarda el idCalEst devuelto
    private void sincronizarConCalendario(Evaluacion evaluacion) {
        try {
            CalendarioEventoRequest evento = construirEventoRequest(evaluacion);
            CalendarioEstudiantilDTO creado = calendarioRestClient.post()
                    .uri("/api/calendarios")
                    .body(evento)
                    .retrieve()
                    .body(CalendarioEstudiantilDTO.class);

            if (creado != null) {
                evaluacion.setIdCalEst(creado.getIdCalEst());
                evaluacionRepository.save(evaluacion);
            }
        } catch (Exception e) {
            logger.warn("No se pudo sincronizar la evaluacion con el calendario: {}", e.getMessage());
        }
    }

    // Actualiza el evento de calendario ya existente para esta evaluacion
    private void actualizarEventoCalendario(Evaluacion evaluacion) {
        try {
            CalendarioEventoRequest evento = construirEventoRequest(evaluacion);
            calendarioRestClient.put()
                    .uri("/api/calendarios/{id}", evaluacion.getIdCalEst())
                    .body(evento)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            logger.warn("No se pudo actualizar el evento de calendario de la evaluacion: {}", e.getMessage());
        }
    }

    // Elimina el evento de calendario asociado a una evaluacion que va a ser borrada
    private void eliminarEventoCalendario(Long idCalEst) {
        try {
            calendarioRestClient.delete()
                    .uri("/api/calendarios/{id}", idCalEst)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            logger.warn("No se pudo eliminar el evento de calendario de la evaluacion: {}", e.getMessage());
        }
    }

    private CalendarioEventoRequest construirEventoRequest(Evaluacion evaluacion) {
        CalendarioEventoRequest evento = new CalendarioEventoRequest();
        evento.setTituloEvento(evaluacion.getEvaNom());
        evento.setTipoEvento("Evaluación");
        evento.setFechaInicio(evaluacion.getEvaFecha());
        evento.setFechaFin(evaluacion.getEvaFecha());
        evento.setIdMuralDigital(null);
        evento.setIdAsignatura(evaluacion.getAsignatura().getIdAsi().longValue());
        evento.setDescripcionEvento(evaluacion.getEvaTipo() + " — " + evaluacion.getEvaPeriodoAcad());
        evento.setCursoId(evaluacion.getCurso() != null ? evaluacion.getCurso().getIdCur().longValue() : null);
        evento.setDocenteUsuRut(evaluacion.getDocenteUsuRut().longValue());
        return evento;
    }
}