package com.cahuinlabs.gestionAcademica.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentCaptor;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cahuinlabs.gestionAcademica.models.entities.Curso;
import com.cahuinlabs.gestionAcademica.models.entities.Nivel;
import com.cahuinlabs.gestionAcademica.models.entities.Sala;
import com.cahuinlabs.gestionAcademica.models.request.Curso.ActualizarCursoRequest;
import com.cahuinlabs.gestionAcademica.models.request.Curso.CrearCursoRequest;
import com.cahuinlabs.gestionAcademica.repository.CursoRepository;
import com.cahuinlabs.gestionAcademica.repository.NivelRepository;
import com.cahuinlabs.gestionAcademica.repository.SalaRepository;

@ExtendWith(MockitoExtension.class)
public class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private NivelRepository nivelRepository;

    @InjectMocks
    private CursoService cursoService;

    private Sala sala;
    private Nivel nivel;
    private Curso curso;

    @BeforeEach
    void setUp() {
        sala = new Sala();
        sala.setIdSal(10);
        sala.setSalLetra("B");
        sala.setSalaCapacidad(25);

        nivel = new Nivel();
        nivel.setIdNiv(20);
        nivel.setNivNum(3);

        curso = new Curso();
        curso.setIdCur(1);
        curso.setCurLetraSeccion("A");
        curso.setCurAnioEscolar(2024);
        curso.setSala(sala);
        curso.setNivel(nivel);
    }

    @Test
    void deberiaCrearCurso() {
        CrearCursoRequest request = new CrearCursoRequest();
        request.setCurLetraSec("B");
        request.setCurAnioEscolar(2025);
        request.setIdSala(10);
        request.setIdNivel(20);

        when(salaRepository.findById(10)).thenReturn(Optional.of(sala));
        when(nivelRepository.findById(20)).thenReturn(Optional.of(nivel));
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        Curso resultado = cursoService.crearCurso(request);

        ArgumentCaptor<Curso> captor = ArgumentCaptor.forClass(Curso.class);
        verify(cursoRepository).save(captor.capture());

        assertNotNull(resultado);
        assertEquals("B", captor.getValue().getCurLetraSeccion());
        assertEquals(2025, captor.getValue().getCurAnioEscolar());
        assertSame(sala, captor.getValue().getSala());
        assertSame(nivel, captor.getValue().getNivel());
        verify(salaRepository).findById(10);
        verify(nivelRepository).findById(20);
    }

    @Test
    void deberiaActualizarCurso() {
        ActualizarCursoRequest request = new ActualizarCursoRequest();
        request.setCurLetraSec("C");
        request.setIdSala(10);

        when(cursoRepository.findById(1)).thenReturn(Optional.of(curso));
        when(salaRepository.findById(10)).thenReturn(Optional.of(sala));
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        Curso resultado = cursoService.actualizarCurso(1, request);

        assertNotNull(resultado);
        assertEquals("C", resultado.getCurLetraSeccion());
        assertSame(sala, resultado.getSala());
        verify(cursoRepository).findById(1);
        verify(salaRepository).findById(10);
        verify(cursoRepository).save(curso);
    }
}
