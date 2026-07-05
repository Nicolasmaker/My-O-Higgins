package com.cahuinlabs.anotaciones.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;

import com.cahuinlabs.anotaciones.dto.AnotacionResponseDTO;
import com.cahuinlabs.anotaciones.dto.CursoDTO;
import com.cahuinlabs.anotaciones.dto.EstudianteDTO;
import com.cahuinlabs.anotaciones.dto.FuncionarioDTO;
import com.cahuinlabs.anotaciones.dto.HojaVidaDTO;
import com.cahuinlabs.anotaciones.models.entities.Anotacion;
import com.cahuinlabs.anotaciones.models.request.AnotacionDTO;
import com.cahuinlabs.anotaciones.repository.AnotacionRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AnotacionService {

    @Autowired
    private AnotacionRepository anotacionRepository;

    // RestClient para comunicarse con el microservicio de Autenticacion
    private final RestClient autenticacionRestClient;

    // RestClient para comunicarse con el microservicio de HojaDeVida
    private final RestClient hojaVidaRestClient;

    // RestClient para comunicarse con el microservicio de GestionAcademica
    private final RestClient gestionAcademicaRestClient;

    public AnotacionService(@Qualifier("autenticacionRestClient") RestClient autenticacionRestClient,
                            @Qualifier("hojaVidaRestClient") RestClient hojaVidaRestClient,
                            @Qualifier("gestionAcademicaRestClient") RestClient gestionAcademicaRestClient) {
        this.autenticacionRestClient = autenticacionRestClient;
        this.hojaVidaRestClient = hojaVidaRestClient;
        this.gestionAcademicaRestClient = gestionAcademicaRestClient;
    }

    // crea una nueva anotacion asignando tipo, descripcion, fecha actual y relacionandola con la hoja de vida
    public Anotacion crearAnotacion(AnotacionDTO dto) {
        // Validamos que el funcionario (docente o inspector) exista en Autenticacion
        if (!existeFuncionario(dto.getFuncionarioUsuRut())) {
            throw new IllegalArgumentException("No se puede crear la anotacion: el funcionario con RUT " + dto.getFuncionarioUsuRut() + " no existe en el sistema.");
        }
        // Validamos que la hoja de vida exista en HojaDeVida
        if (!existeHojaVida(dto.getIdHojaVida())) {
            throw new IllegalArgumentException("No se puede crear la anotacion: la hoja de vida con ID " + dto.getIdHojaVida() + " no existe en el sistema.");
        }

        Anotacion anotacion = new Anotacion();
        anotacion.setAnotTip(dto.getAnotTip());
        anotacion.setAnotDes(dto.getAnotDes());
        anotacion.setAnotFec(LocalDate.now());
        anotacion.setFuncionarioUsuRut(dto.getFuncionarioUsuRut());
        anotacion.setIdHojaVida(dto.getIdHojaVida());
        return anotacionRepository.save(anotacion);
    }

    // consulta todas las anotaciones de un estudiante filtrando por su hoja de vida
    public List<AnotacionResponseDTO> obtenerPorHojaVida(Long idHojaVida) {
        return enriquecerTodas(anotacionRepository.findByIdHojaVida(idHojaVida));
    }

    // consulta las anotaciones de un estudiante a partir de su RUT: resuelve la hoja de
    // vida en MS-HojaDeVida y luego filtra por su id. Si el estudiante no tiene hoja de
    // vida registrada, se devuelve una lista vacia en vez de un error.
    public List<AnotacionResponseDTO> obtenerPorEstudianteRut(Long estudianteUsuRut) {
        Long idHojaVida = resolverIdHojaVidaPorRut(estudianteUsuRut);
        if (idHojaVida == null) {
            return List.of();
        }
        return enriquecerTodas(anotacionRepository.findByIdHojaVida(idHojaVida));
    }

    // edita tipo y descripcion de una anotacion existente, preservando fecha y autor
    public Anotacion modificarAnotacion(Long idAnot, AnotacionDTO dto) {
        Anotacion anotacion = anotacionRepository.findById(idAnot)
                .orElseThrow(() -> new EntityNotFoundException("Anotacion no encontrada: " + idAnot));
        anotacion.setAnotTip(dto.getAnotTip());
        anotacion.setAnotDes(dto.getAnotDes());
        return anotacionRepository.save(anotacion);
    }

    // elimina una anotacion si existe, lanzando excepcion en caso contrario
    public void eliminarAnotacion(Long idAnot) {
        if (!anotacionRepository.existsById(idAnot)) {
            throw new EntityNotFoundException("Anotacion no encontrada: " + idAnot);
        }
        anotacionRepository.deleteById(idAnot);
    }
    // obtiene todas las anotaciones del sistema sin filtros
    public List<AnotacionResponseDTO> obtenerTodas() {
        return enriquecerTodas(anotacionRepository.findAll());
    }

    // Verifica si el funcionario (docente o inspector) existe en el microservicio de Autenticacion
    private boolean existeFuncionario(Long rut) {
        try {
            FuncionarioDTO funcionario = autenticacionRestClient.get()
                    .uri("/funcionarios/{rut}", rut)
                    .retrieve()
                    .body(FuncionarioDTO.class);
            return funcionario != null;
        } catch (HttpClientErrorException.NotFound e) {
            // El microservicio de Autenticacion respondio 404, el funcionario no existe
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el microservicio de Autenticacion: " + e.getMessage());
        }
    }

    // Verifica si la hoja de vida existe en el microservicio de HojaDeVida
    private boolean existeHojaVida(Long idHojaVida) {
        try {
            HojaVidaDTO hojaVida = hojaVidaRestClient.get()
                    .uri("/api/hojas-vida/{id}", idHojaVida)
                    .retrieve()
                    .body(HojaVidaDTO.class);
            return hojaVida != null;
        } catch (HttpClientErrorException.NotFound e) {
            // El microservicio de HojaDeVida respondio 404, la hoja de vida no existe
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el microservicio de HojaDeVida: " + e.getMessage());
        }
    }

    // Resuelve el idHojaVida de un estudiante a partir de su RUT consultando MS-HojaDeVida.
    // Devuelve null si el estudiante no tiene hoja de vida registrada.
    private Long resolverIdHojaVidaPorRut(Long estudianteUsuRut) {
        try {
            HojaVidaDTO hojaVida = hojaVidaRestClient.get()
                    .uri("/api/hojas-vida/estudiante/{rut}", estudianteUsuRut)
                    .retrieve()
                    .body(HojaVidaDTO.class);
            return hojaVida != null ? hojaVida.idHojaVida() : null;
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el microservicio de HojaDeVida: " + e.getMessage());
        }
    }

    // Mapea una lista de anotaciones crudas a su version enriquecida con datos de
    // funcionario, estudiante y curso.
    private List<AnotacionResponseDTO> enriquecerTodas(List<Anotacion> anotaciones) {
        return anotaciones.stream().map(this::enriquecer).toList();
    }

    // Enriquece una anotacion con nombre/apellido del funcionario que la creo y con
    // rut/nombre/apellido/curso del estudiante dueno de la hoja de vida asociada.
    // Si algun microservicio externo falla o no encuentra el dato, se omite ese dato
    // (queda null) en vez de romper el listado completo.
    private AnotacionResponseDTO enriquecer(Anotacion anotacion) {
        FuncionarioDTO funcionario = obtenerFuncionario(anotacion.getFuncionarioUsuRut());
        Long estudianteUsuRut = resolverEstudianteRutPorIdHojaVida(anotacion.getIdHojaVida());
        EstudianteDTO estudiante = estudianteUsuRut != null ? obtenerEstudiante(estudianteUsuRut) : null;
        String curso = estudiante != null && estudiante.cursoId() != null ? obtenerNombreCurso(estudiante.cursoId()) : null;

        return new AnotacionResponseDTO(
                anotacion.getIdAnot(),
                anotacion.getAnotTip(),
                anotacion.getAnotDes(),
                anotacion.getAnotFec(),
                anotacion.getFuncionarioUsuRut(),
                funcionario != null ? funcionario.dv() : null,
                funcionario != null ? funcionario.nombre() : null,
                funcionario != null ? funcionario.apellido() : null,
                funcionario != null && funcionario.rol() != null ? funcionario.rol().rolNombre() : null,
                anotacion.getIdHojaVida(),
                estudianteUsuRut,
                estudiante != null ? estudiante.dv() : null,
                estudiante != null ? estudiante.nombre() : null,
                estudiante != null ? estudiante.apellido() : null,
                curso
        );
    }

    // Obtiene los datos del funcionario en Autenticacion. Devuelve null si no existe
    // o si el microservicio no responde.
    private FuncionarioDTO obtenerFuncionario(Long rut) {
        try {
            return autenticacionRestClient.get()
                    .uri("/funcionarios/{rut}", rut)
                    .retrieve()
                    .body(FuncionarioDTO.class);
        } catch (Exception e) {
            return null;
        }
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

    // Resuelve el RUT del estudiante a partir del ID de una hoja de vida consultando
    // MS-HojaDeVida. Devuelve null si la hoja de vida no existe o si falla la llamada.
    private Long resolverEstudianteRutPorIdHojaVida(Long idHojaVida) {
        try {
            HojaVidaDTO hojaVida = hojaVidaRestClient.get()
                    .uri("/api/hojas-vida/{id}", idHojaVida)
                    .retrieve()
                    .body(HojaVidaDTO.class);
            return hojaVida != null ? hojaVida.estudianteUsuRut() : null;
        } catch (Exception e) {
            return null;
        }
    }

    // Obtiene el nombre legible del curso (ej. "3°A") en GestionAcademica. Devuelve
    // null si no existe o si el microservicio no responde.
    private String obtenerNombreCurso(Integer cursoId) {
        try {
            CursoDTO curso = gestionAcademicaRestClient.get()
                    .uri("/curso/{id}", cursoId)
                    .retrieve()
                    .body(CursoDTO.class);
            if (curso == null || curso.nivel() == null) {
                return null;
            }
            return curso.nivel().nivNum() + "°" + curso.curLetraSeccion();
        } catch (Exception e) {
            return null;
        }
    }
}
