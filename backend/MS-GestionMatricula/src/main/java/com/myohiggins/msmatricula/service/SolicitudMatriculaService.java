package com.myohiggins.msmatricula.service;

import com.myohiggins.msmatricula.dto.EstudianteDTO;
import com.myohiggins.msmatricula.dto.ApoderadoDTO;
import com.myohiggins.msmatricula.dto.FuncionarioDTO;
import com.myohiggins.msmatricula.model.entities.Matricula;
import com.myohiggins.msmatricula.model.entities.SolicitudMatricula;
import com.myohiggins.msmatricula.repository.MatriculaRepository;
import com.myohiggins.msmatricula.repository.SolicitudMatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import java.util.List;

@Service
public class SolicitudMatriculaService {

    @Autowired
    private SolicitudMatriculaRepository solicitudRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    // RestClient para comunicarse con el microservicio de Autenticacion
    private final RestClient autenticacionRestClient;

    public SolicitudMatriculaService(RestClient autenticacionRestClient) {
        this.autenticacionRestClient = autenticacionRestClient;
    }

    // El apoderado crea la solicitud (sin funcionario: aun no hay matricula real, solo la solicitud)
    public SolicitudMatricula crearSolicitud(SolicitudMatricula solicitud) {
        if (!existeEstudiante(solicitud.getAlumnoRut())) {
            throw new IllegalArgumentException(
                    "El estudiante con RUT " + solicitud.getAlumnoRut()
                            + " no tiene una cuenta registrada. Debe crearse su cuenta antes de solicitar matrícula.");
        }
        if (!existeApoderado(solicitud.getApoderadoRut())) {
            throw new IllegalArgumentException(
                    "El apoderado con RUT " + solicitud.getApoderadoRut() + " no existe en el sistema.");
        }
        solicitud.setEstado("PENDIENTE");
        return solicitudRepository.save(solicitud);
    }

    public List<SolicitudMatricula> listarTodas() {
        return solicitudRepository.findAll();
    }

    public List<SolicitudMatricula> listarPorApoderado(Long apoderadoRut) {
        return solicitudRepository.findByApoderadoRut(apoderadoRut);
    }

    // El Directivo aprueba: recien aqui se crea la Matricula real, con SU rut como funcionario
    public Matricula aprobarSolicitud(Long idSolicitud, Long funcionarioUsuRut, Long cursoIdConfirmado) {
        SolicitudMatricula solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada: " + idSolicitud));
        if (!"PENDIENTE".equals(solicitud.getEstado())) {
            throw new IllegalArgumentException("La solicitud ya fue procesada (estado actual: " + solicitud.getEstado() + ")");
        }
        if (!existeFuncionario(funcionarioUsuRut)) {
            throw new IllegalArgumentException("El funcionario con RUT " + funcionarioUsuRut + " no existe en el sistema.");
        }

        Long cursoId = cursoIdConfirmado != null ? cursoIdConfirmado : solicitud.getCursoId();

        Matricula matricula = new Matricula();
        matricula.setAlumnoRut(solicitud.getAlumnoRut());
        matricula.setApoderadoRut(solicitud.getApoderadoRut());
        matricula.setCursoId(cursoId);
        matricula.setTipoAlumno(solicitud.getTipoAlumno());
        matricula.setFuncionarioUsuRut(funcionarioUsuRut);
        Matricula matriculaCreada = matriculaRepository.save(matricula);

        solicitud.setEstado("APROBADA");
        solicitudRepository.save(solicitud);

        return matriculaCreada;
    }

    public SolicitudMatricula rechazarSolicitud(Long idSolicitud, String motivo) {
        SolicitudMatricula solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada: " + idSolicitud));
        if (!"PENDIENTE".equals(solicitud.getEstado())) {
            throw new IllegalArgumentException("La solicitud ya fue procesada (estado actual: " + solicitud.getEstado() + ")");
        }
        solicitud.setEstado("RECHAZADA");
        solicitud.setMotivoRechazo(motivo);
        return solicitudRepository.save(solicitud);
    }

    // Verifica si el estudiante existe en el microservicio de Autenticacion
    private boolean existeEstudiante(Long rut) {
        try {
            EstudianteDTO estudiante = autenticacionRestClient.get()
                    .uri("/estudiantes/{rut}", rut)
                    .retrieve()
                    .body(EstudianteDTO.class);
            return estudiante != null;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el microservicio de Autenticacion: " + e.getMessage());
        }
    }

    // Verifica si el apoderado existe en el microservicio de Autenticacion
    private boolean existeApoderado(Long rut) {
        try {
            ApoderadoDTO apoderado = autenticacionRestClient.get()
                    .uri("/apoderados/{rut}", rut)
                    .retrieve()
                    .body(ApoderadoDTO.class);
            return apoderado != null;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el microservicio de Autenticacion: " + e.getMessage());
        }
    }

    // Verifica si el funcionario existe en el microservicio de Autenticacion
    private boolean existeFuncionario(Long rut) {
        try {
            FuncionarioDTO funcionario = autenticacionRestClient.get()
                    .uri("/funcionarios/{rut}", rut)
                    .retrieve()
                    .body(FuncionarioDTO.class);
            return funcionario != null;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el microservicio de Autenticacion: " + e.getMessage());
        }
    }
}
