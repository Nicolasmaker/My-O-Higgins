package com.myohiggins.msmatricula.service;

import com.myohiggins.msmatricula.dto.EstudianteDTO;
import com.myohiggins.msmatricula.dto.ApoderadoDTO;
import com.myohiggins.msmatricula.dto.FuncionarioDTO;
import com.myohiggins.msmatricula.model.entities.Matricula;
import com.myohiggins.msmatricula.repository.MatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import java.util.List;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    // RestClient para comunicarse con el microservicio de Autenticacion
    private final RestClient autenticacionRestClient;

    public MatriculaService(RestClient autenticacionRestClient) {
        this.autenticacionRestClient = autenticacionRestClient;
    }

    // 1. Crear una nueva matrícula
    public Matricula crearMatricula(Matricula matricula) {
        // Validamos que el alumno exista en Autenticacion
        if (!existeEstudiante(matricula.getAlumnoRut())) {
            throw new IllegalArgumentException("No se puede registrar la matricula: el estudiante con RUT " + matricula.getAlumnoRut() + " no existe en el sistema.");
        }
        // Validamos que el apoderado exista en Autenticacion
        if (!existeApoderado(matricula.getApoderadoRut())) {
            throw new IllegalArgumentException("No se puede registrar la matricula: el apoderado con RUT " + matricula.getApoderadoRut() + " no existe en el sistema.");
        }
        // Validamos que el funcionario que registra exista en Autenticacion
        if (!existeFuncionario(matricula.getFuncionarioUsuRut())) {
            throw new IllegalArgumentException("No se puede registrar la matricula: el funcionario con RUT " + matricula.getFuncionarioUsuRut() + " no existe en el sistema.");
        }

        return matriculaRepository.save(matricula);
    }

    // 2. Obtener todas las matrículas registradas
    public List<Matricula> listarTodas() {
        return matriculaRepository.findAll();
    }

    // 4. Buscar matrícula por su ID
    public Matricula buscarPorId(Long id) {
        return matriculaRepository.findById(id).orElse(null);
    }
    // 4. Actualizar una matrícula existente por su ID
    public Matricula actualizarMatricula(Long id, Matricula detallesMatricula) {
        Matricula matriculaExistente = matriculaRepository.findById(id).orElse(null);

        if (matriculaExistente != null) {
            // Validamos que el alumno exista en Autenticacion
            if (!existeEstudiante(detallesMatricula.getAlumnoRut())) {
                throw new IllegalArgumentException("No se puede actualizar la matricula: el estudiante con RUT " + detallesMatricula.getAlumnoRut() + " no existe en el sistema.");
            }
            // Validamos que el apoderado exista en Autenticacion
            if (!existeApoderado(detallesMatricula.getApoderadoRut())) {
                throw new IllegalArgumentException("No se puede actualizar la matricula: el apoderado con RUT " + detallesMatricula.getApoderadoRut() + " no existe en el sistema.");
            }
            // Validamos que el funcionario exista en Autenticacion
            if (!existeFuncionario(detallesMatricula.getFuncionarioUsuRut())) {
                throw new IllegalArgumentException("No se puede actualizar la matricula: el funcionario con RUT " + detallesMatricula.getFuncionarioUsuRut() + " no existe en el sistema.");
            }

            matriculaExistente.setMatriculaEstado(detallesMatricula.getMatriculaEstado());
            matriculaExistente.setFuncionarioUsuRut(detallesMatricula.getFuncionarioUsuRut());
            matriculaExistente.setAlumnoRut(detallesMatricula.getAlumnoRut());
            matriculaExistente.setCursoId(detallesMatricula.getCursoId());
            matriculaExistente.setApoderadoRut(detallesMatricula.getApoderadoRut());
            matriculaExistente.setTipoAlumno(detallesMatricula.getTipoAlumno());

            return matriculaRepository.save(matriculaExistente);
        }
        return null;
    }

    // 6. Eliminar una matrícula por su ID
    public void eliminarMatricula(Long id) {
        matriculaRepository.deleteById(id);
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