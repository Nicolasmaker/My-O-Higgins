package com.cahuinlabs.gestionAcademica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.cahuinlabs.gestionAcademica.dto.FuncionarioDTO;
import com.cahuinlabs.gestionAcademica.dto.ImpartirResponseDTO;
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

    // RestClient para comunicarse con el microservicio de Autenticacion
    private final RestClient autenticacionRestClient;

    public ImpartirService(@Qualifier("autenticacionRestClient") RestClient autenticacionRestClient) {
        this.autenticacionRestClient = autenticacionRestClient;
    }

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
    public List<ImpartirResponseDTO> listarTodos() {
        return enriquecerTodos(impartirRepository.findAll());
    }

 //LISTAR POR DOCENTE
    public List<ImpartirResponseDTO> listarPorDocente(Integer docenteUsuRut) {
        return enriquecerTodos(impartirRepository.findByDocenteUsuRut(docenteUsuRut));
    }

    // Mapea una lista de Impartir crudos a su version enriquecida con RUT+DV y nombre del docente.
    private List<ImpartirResponseDTO> enriquecerTodos(List<Impartir> registros) {
        return registros.stream().map(this::enriquecer).toList();
    }

    // Enriquece un Impartir con DV y nombre del docente, resueltos desde Autenticacion.
    // Si el docente no se encuentra o el microservicio externo falla, se omite ese dato
    // (queda null) en vez de romper el listado completo.
    private ImpartirResponseDTO enriquecer(Impartir impartir) {
        FuncionarioDTO docente = obtenerDocente(impartir.getDocenteUsuRut());
        return new ImpartirResponseDTO(
                impartir.getIdImp(),
                impartir.getDocenteUsuRut(),
                docente != null ? docente.dv() : null,
                docente != null ? docente.nombre() : null,
                docente != null ? docente.apellido() : null,
                impartir.getAsignatura(),
                impartir.getCurso()
        );
    }

    // Obtiene los datos del docente en Autenticacion. Devuelve null si no existe o si el
    // microservicio no responde.
    private FuncionarioDTO obtenerDocente(Integer rut) {
        try {
            return autenticacionRestClient.get()
                    .uri("/funcionarios/{rut}", rut)
                    .retrieve()
                    .body(FuncionarioDTO.class);
        } catch (Exception e) {
            return null;
        }
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
