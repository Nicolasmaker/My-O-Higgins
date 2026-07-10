package com.cahuinlabs.gestionAcademica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.cahuinlabs.gestionAcademica.dto.AsistenciaResponseDTO;
import com.cahuinlabs.gestionAcademica.dto.EstudianteDTO;
import com.cahuinlabs.gestionAcademica.models.entities.Asistencia;
import com.cahuinlabs.gestionAcademica.models.entities.Impartir;
import com.cahuinlabs.gestionAcademica.models.request.Asistencia.ActualizarAsistenciaRequest;
import com.cahuinlabs.gestionAcademica.models.request.Asistencia.CrearAsistenciaRequest;
import com.cahuinlabs.gestionAcademica.repository.AsistenciaRepository;
import com.cahuinlabs.gestionAcademica.repository.ImpartirRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private ImpartirRepository impartirRepository;

    // RestClient para comunicarse con el microservicio de Autenticacion
    private final RestClient autenticacionRestClient;

    public AsistenciaService(@Qualifier("autenticacionRestClient") RestClient autenticacionRestClient) {
        this.autenticacionRestClient = autenticacionRestClient;
    }

    public Asistencia crear(CrearAsistenciaRequest request) {
        Impartir impartir = impartirRepository.findById(request.getIdImpartir())
                .orElseThrow(() -> new RuntimeException("Error: El registro de Impartir con ID " + request.getIdImpartir() + " no existe."));

        Asistencia asistencia = new Asistencia();
        asistencia.setAsisFecha(request.getAsisFecha());
        asistencia.setAsisEstado(request.getAsisEstado());
        asistencia.setEstudianteUsuRut(request.getEstudianteUsuRut());
        asistencia.setImpartir(impartir);

        return asistenciaRepository.save(asistencia);
    }

    public Asistencia actualizar(Integer id, ActualizarAsistenciaRequest request) {
        Asistencia existente = asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: La Asistencia con ID " + id + " no existe."));
        existente.setAsisEstado(request.getAsisEstado());
        return asistenciaRepository.save(existente);
    }

    public List<AsistenciaResponseDTO> listarTodas() {
        return enriquecerTodas(asistenciaRepository.findAll());
    }

    public List<AsistenciaResponseDTO> listarPorEstudiante(Integer estudianteUsuRut) {
        return enriquecerTodas(asistenciaRepository.findByEstudianteUsuRut(estudianteUsuRut));
    }

    public List<AsistenciaResponseDTO> listarPorImpartirYFecha(Integer idImpartir, LocalDate fecha) {
        return enriquecerTodas(asistenciaRepository.findByImpartir_IdImpAndAsisFecha(idImpartir, fecha));
    }

    public Optional<Asistencia> buscarPorId(Integer id) {
        return asistenciaRepository.findById(id);
    }

    public void eliminar(Integer id) {
        asistenciaRepository.deleteById(id);
    }

    private List<AsistenciaResponseDTO> enriquecerTodas(List<Asistencia> lista) {
        return lista.stream().map(this::enriquecer).toList();
    }

    // Enriquece una asistencia con DV y nombre del estudiante, resueltos desde Autenticacion.
    // Si el estudiante no se encuentra o el microservicio externo falla, se omite ese dato
    // (queda null) en vez de romper el listado completo.
    private AsistenciaResponseDTO enriquecer(Asistencia asistencia) {
        EstudianteDTO estudiante = obtenerEstudiante(asistencia.getEstudianteUsuRut());
        return new AsistenciaResponseDTO(
                asistencia.getIdAsis(),
                asistencia.getAsisFecha(),
                asistencia.getAsisEstado(),
                asistencia.getEstudianteUsuRut(),
                estudiante != null ? estudiante.dv() : null,
                estudiante != null ? estudiante.nombre() : null,
                estudiante != null ? estudiante.apellido() : null,
                asistencia.getImpartir()
        );
    }

    private EstudianteDTO obtenerEstudiante(Integer rut) {
        try {
            return autenticacionRestClient.get()
                    .uri("/estudiantes/{rut}", rut)
                    .retrieve()
                    .body(EstudianteDTO.class);
        } catch (Exception e) {
            return null;
        }
    }
}
