package com.myohiggins.msmatricula.service;

import com.myohiggins.msmatricula.model.entities.Matricula;
import com.myohiggins.msmatricula.repository.MatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    // 1. Crear una nueva matrícula
    public Matricula crearMatricula(Matricula matricula) {
        return matriculaRepository.save(matricula);
    }
    // 2. Registrar una nueva matrícula
    public Matricula registrarMatricula(Matricula matricula) {
        return crearMatricula(matricula);
    }

    // 3. Obtener todas las matrículas registradas
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
            matriculaExistente.setMatriculaEstado(detallesMatricula.getMatriculaEstado());
            matriculaExistente.setFuncionarioUsuRut(detallesMatricula.getFuncionarioUsuRut());
            matriculaExistente.setAlumnoRut(detallesMatricula.getAlumnoRut());
            
            // Campos nuevos del colegio:
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
}