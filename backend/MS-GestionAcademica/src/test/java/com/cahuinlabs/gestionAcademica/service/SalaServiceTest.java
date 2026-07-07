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

import com.cahuinlabs.gestionAcademica.models.entities.Sala;
import com.cahuinlabs.gestionAcademica.models.request.SalaRequest;
import com.cahuinlabs.gestionAcademica.repository.SalaRepository;

@ExtendWith(MockitoExtension.class)
public class SalaServiceTest {

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private SalaService salaService;

    private Sala sala;

    @BeforeEach
    void setUp() {
        sala = new Sala();
        sala.setIdSal(1);
        sala.setSalLetra("B");
        sala.setSalaCapacidad(30);
    }

    @Test
    void deberiaCrearSala() {
        SalaRequest request = new SalaRequest();
        request.setSalLetra("A");
        request.setSalaCapacidad(40);

        when(salaRepository.save(any(Sala.class))).thenReturn(sala);

        Sala resultado = salaService.crearSala(request);

        ArgumentCaptor<Sala> captor = ArgumentCaptor.forClass(Sala.class);
        verify(salaRepository).save(captor.capture());

        assertNotNull(resultado);
        assertEquals("A", captor.getValue().getSalLetra());
        assertEquals(40, captor.getValue().getSalaCapacidad());
    }

    @Test
    void deberiaActualizarSala() {
        SalaRequest request = new SalaRequest();
        request.setSalLetra("C");
        request.setSalaCapacidad(35);

        when(salaRepository.findById(1)).thenReturn(Optional.of(sala));
        when(salaRepository.save(any(Sala.class))).thenReturn(sala);

        Sala resultado = salaService.actualizarSala(1, request);

        assertNotNull(resultado);
        assertEquals("C", resultado.getSalLetra());
        assertEquals(35, resultado.getSalaCapacidad());
        verify(salaRepository).findById(1);
        verify(salaRepository).save(sala);
    }
}
