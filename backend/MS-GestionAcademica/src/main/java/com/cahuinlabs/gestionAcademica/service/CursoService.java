package com.cahuinlabs.gestionAcademica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cahuinlabs.gestionAcademica.models.entities.Curso;
import com.cahuinlabs.gestionAcademica.models.entities.Nivel;
import com.cahuinlabs.gestionAcademica.models.entities.Sala;
import com.cahuinlabs.gestionAcademica.models.request.Curso.ActualizarCursoRequest;
import com.cahuinlabs.gestionAcademica.models.request.Curso.CrearCursoRequest;
import com.cahuinlabs.gestionAcademica.repository.CursoRepository;
import com.cahuinlabs.gestionAcademica.repository.NivelRepository;
import com.cahuinlabs.gestionAcademica.repository.SalaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private NivelRepository nivelRepository;

 //CREAR
    public Curso crearCurso(CrearCursoRequest cursoRequest) {
     //Busca las dependencias en la BD para asegurar que existen
        Sala sala = salaRepository.findById(cursoRequest.getIdSala())
                .orElseThrow(() -> new RuntimeException("Error: La Sala con ID " + cursoRequest.getIdSala() + " no existe."));
                
        Nivel nivel = nivelRepository.findById(cursoRequest.getIdNivel())
                .orElseThrow(() -> new RuntimeException("Error: El Nivel con ID " + cursoRequest.getIdNivel() + " no existe."));

     //Arma la entidad Curso
        Curso curso = new Curso();
        curso.setCurLetraSeccion(cursoRequest.getCurLetraSec());
        curso.setCurAnioEscolar(cursoRequest.getCurAnioEscolar());
        curso.setCupos(cursoRequest.getCupos());
        curso.setSala(sala);
        curso.setNivel(nivel);

        return cursoRepository.save(curso);
    }

 //ACTUALIZAR
    public Curso actualizarCurso(Integer id, ActualizarCursoRequest request) {
     //Busca el curso a actualizar
        Curso cursoExistente = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: El Curso con ID " + id + " no existe."));

     //Buscamos las nuevas dependencias
        Sala sala = salaRepository.findById(request.getIdSala())
                .orElseThrow(() -> new RuntimeException("Error: La Sala no existe."));

     //Actualiza los datos
        cursoExistente.setCurLetraSeccion(request.getCurLetraSec());
        if (request.getCupos() != null) {
            cursoExistente.setCupos(request.getCupos());
        }
        cursoExistente.setSala(sala);

        return cursoRepository.save(cursoExistente);
    }

 //LISTAR TODOS
    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

 //BUSCAR POR ID
    public Optional<Curso> buscarPorId(Integer id) {
        return cursoRepository.findById(id);
    }

 //ELIMINAR
    public void eliminar(Integer id) {
        cursoRepository.deleteById(id);
    }
}
