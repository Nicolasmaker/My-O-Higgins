package com.cahuinlabs.gestionAcademica.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cahuinlabs.gestionAcademica.models.entities.Evaluacion;
import com.cahuinlabs.gestionAcademica.models.entities.Notas;
import com.cahuinlabs.gestionAcademica.models.request.Nota.ActualizarNotaRequest;
import com.cahuinlabs.gestionAcademica.models.request.Nota.CrearNotaRequest;
import com.cahuinlabs.gestionAcademica.repository.EvaluacionRepository;
import com.cahuinlabs.gestionAcademica.repository.NotasRepository;

@ExtendWith(MockitoExtension.class)
public class NotaServiceTest {

    @Mock
    private NotasRepository notasRepository;

    @Mock
    private EvaluacionRepository evaluacionRepository;

    @InjectMocks
    private NotaService notaService;

    private Evaluacion evaluacion;
    private Notas nota;

    @BeforeEach
    void setUp() {
        evaluacion = new Evaluacion();
        evaluacion.setIdEva(3);
        evaluacion.setEvaNom("Prueba 1");
        evaluacion.setEvaFecha(LocalDate.now());
        evaluacion.setEvaPeriodoAcad("2024");
        evaluacion.setEvaTipo("Parcial");
        evaluacion.setDocenteUsuRut(1001);

        nota = new Notas();
        nota.setIdNot(1);
        nota.setNotCalif(6.5);
        nota.setNotFechaRegistrada(LocalDate.of(2024, 1, 15));
        nota.setEvaluacion(evaluacion);
        nota.setEstudianteUsuRut(2002);
    }

    @Test
    void deberiaCrearNota() {
        CrearNotaRequest request = new CrearNotaRequest();
        request.setNotCalif(6.0);
        request.setNotFechaReg(LocalDate.of(2024, 1, 16));
        request.setIdEvaluacion(3);
        request.setEstudianteUsuRut(2002);

        when(evaluacionRepository.findById(3)).thenReturn(Optional.of(evaluacion));
        when(notasRepository.save(any(Notas.class))).thenReturn(nota);

        Notas resultado = notaService.crearNota(request);

        ArgumentCaptor<Notas> captor = ArgumentCaptor.forClass(Notas.class);
        verify(notasRepository).save(captor.capture());

        assertNotNull(resultado);
        assertEquals(6.0, captor.getValue().getNotCalif());
        assertEquals(2002, captor.getValue().getEstudianteUsuRut());
        assertSame(evaluacion, captor.getValue().getEvaluacion());
        verify(evaluacionRepository).findById(3);
    }

    @Test
    void deberiaActualizarNota() {
        ActualizarNotaRequest request = new ActualizarNotaRequest();
        request.setNotCalif(7.0);
        request.setNotFechaReg(LocalDate.of(2024, 1, 20));

        when(notasRepository.findById(1)).thenReturn(Optional.of(nota));
        when(notasRepository.save(any(Notas.class))).thenReturn(nota);

        Notas resultado = notaService.actualizarNota(1, request);

        assertNotNull(resultado);
        assertEquals(7.0, resultado.getNotCalif());
        verify(notasRepository).findById(1);
        verify(notasRepository).save(nota);
    }
}
