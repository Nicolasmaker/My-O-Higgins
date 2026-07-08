package com.myohiggins.msmatricula.service;

import com.myohiggins.msmatricula.dto.EstudianteDTO;
import com.myohiggins.msmatricula.dto.ApoderadoDTO;
import com.myohiggins.msmatricula.dto.FuncionarioDTO;
import com.myohiggins.msmatricula.dto.HojaVidaDTO;
import com.myohiggins.msmatricula.dto.SolicitudMatriculaResponseDTO;
import com.myohiggins.msmatricula.model.entities.Matricula;
import com.myohiggins.msmatricula.model.entities.SolicitudMatricula;
import com.myohiggins.msmatricula.repository.MatriculaRepository;
import com.myohiggins.msmatricula.repository.SolicitudMatriculaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import java.util.List;

@Service
public class SolicitudMatriculaService {

    private static final Logger logger = LoggerFactory.getLogger(SolicitudMatriculaService.class);

    @Autowired
    private SolicitudMatriculaRepository solicitudRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    // RestClient para comunicarse con el microservicio de Autenticacion
    private final RestClient autenticacionRestClient;
    // RestClient para sincronizar la hoja de vida del estudiante en MS-HojaDeVida
    private final RestClient hojaVidaRestClient;

    public SolicitudMatriculaService(@Qualifier("autenticacionRestClient") RestClient autenticacionRestClient,
                                      @Qualifier("hojaVidaRestClient") RestClient hojaVidaRestClient) {
        this.autenticacionRestClient = autenticacionRestClient;
        this.hojaVidaRestClient = hojaVidaRestClient;
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

    public List<SolicitudMatriculaResponseDTO> listarTodas() {
        return enriquecerTodas(solicitudRepository.findAll());
    }

    public List<SolicitudMatriculaResponseDTO> listarPorApoderado(Long apoderadoRut) {
        return enriquecerTodas(solicitudRepository.findByApoderadoRut(apoderadoRut));
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
        matricula.setParentesco(solicitud.getParentesco());
        matricula.setFuncionarioUsuRut(funcionarioUsuRut);
        Matricula matriculaCreada = matriculaRepository.save(matricula);
        sincronizarHojaVida(matriculaCreada);

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

    // Mapea una lista de solicitudes crudas a su version enriquecida con RUT+DV y nombre.
    private List<SolicitudMatriculaResponseDTO> enriquecerTodas(List<SolicitudMatricula> solicitudes) {
        return solicitudes.stream().map(this::enriquecer).toList();
    }

    // Enriquece una solicitud con DV y nombre del alumno y del apoderado, resueltos desde
    // Autenticacion. Si algun dato no se encuentra o el microservicio externo falla, se
    // omite ese dato (queda null) en vez de romper el listado completo.
    private SolicitudMatriculaResponseDTO enriquecer(SolicitudMatricula solicitud) {
        EstudianteDTO alumno = obtenerEstudiante(solicitud.getAlumnoRut());
        ApoderadoDTO apoderado = obtenerApoderado(solicitud.getApoderadoRut());

        return new SolicitudMatriculaResponseDTO(
                solicitud.getIdSolicitud(),
                solicitud.getAlumnoRut(),
                alumno != null ? alumno.dv() : null,
                alumno != null ? alumno.nombre() : null,
                alumno != null ? alumno.apellido() : null,
                solicitud.getApoderadoRut(),
                apoderado != null ? apoderado.dv() : null,
                apoderado != null ? apoderado.nombre() : null,
                apoderado != null ? apoderado.apellido() : null,
                solicitud.getCursoId(),
                solicitud.getTipoAlumno(),
                solicitud.getParentesco(),
                solicitud.getObservaciones(),
                solicitud.getEstado(),
                solicitud.getFechaSolicitud(),
                solicitud.getMotivoRechazo()
        );
    }

    // Obtiene los datos del estudiante en Autenticacion. Devuelve null si no existe
    // o si el microservicio no responde.
    private EstudianteDTO obtenerEstudiante(Long rut) {
        try {
            return autenticacionRestClient.get()
                    .uri("/estudiantes/{rut}", rut)
                    .retrieve()
                    .body(EstudianteDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

    // Obtiene los datos del apoderado en Autenticacion. Devuelve null si no existe
    // o si el microservicio no responde.
    private ApoderadoDTO obtenerApoderado(Long rut) {
        try {
            return autenticacionRestClient.get()
                    .uri("/apoderados/{rut}", rut)
                    .retrieve()
                    .body(ApoderadoDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

    // Sincroniza la hoja de vida del estudiante en MS-HojaDeVida tras aprobar la solicitud (mismo
    // criterio que MatriculaService.sincronizarHojaVida): crea si no existe, actualiza el
    // matriculaId si ya existe. Best-effort, no bloquea la aprobación.
    private void sincronizarHojaVida(Matricula matricula) {
        try {
            HojaVidaDTO existente = buscarHojaVidaPorRut(matricula.getAlumnoRut());

            HojaVidaDTO body = new HojaVidaDTO();
            body.setEstudianteUsuRut(matricula.getAlumnoRut());
            body.setMatriculaId(matricula.getIdMatricula());
            body.setEstado(existente != null ? existente.getEstado() : "ACTIVA");

            if (existente == null) {
                hojaVidaRestClient.post()
                        .uri("/api/hojas-vida")
                        .body(body)
                        .retrieve()
                        .toBodilessEntity();
            } else {
                hojaVidaRestClient.put()
                        .uri("/api/hojas-vida/{id}", existente.getIdHojaVida())
                        .body(body)
                        .retrieve()
                        .toBodilessEntity();
            }
        } catch (Exception e) {
            logger.warn("No se pudo sincronizar la hoja de vida del estudiante {}: {}", matricula.getAlumnoRut(), e.getMessage());
        }
    }

    private HojaVidaDTO buscarHojaVidaPorRut(Long estudianteUsuRut) {
        try {
            return hojaVidaRestClient.get()
                    .uri("/api/hojas-vida/estudiante/{rut}", estudianteUsuRut)
                    .retrieve()
                    .body(HojaVidaDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }
}
