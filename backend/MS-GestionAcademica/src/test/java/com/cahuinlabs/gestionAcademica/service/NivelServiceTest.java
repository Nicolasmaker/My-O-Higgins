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

import com.cahuinlabs.gestionAcademica.models.entities.Nivel;
import com.cahuinlabs.gestionAcademica.models.request.NivelRequest;
import com.cahuinlabs.gestionAcademica.repository.NivelRepository;

@ExtendWith(MockitoExtension.class)
public class NivelServiceTest {

    @Mock
    private NivelRepository nivelRepository;

    @InjectMocks
    private NivelService nivelService;

    private Nivel nivel;

    @BeforeEach
    void setUp() {
        nivel = new Nivel();
        nivel.setIdNiv(1);
        nivel.setNivNum(4);
    }

    @Test
    void deberiaCrearNivel() {
        NivelRequest request = new NivelRequest();
        request.setNivNum(5);

        when(nivelRepository.save(any(Nivel.class))).thenReturn(nivel);

        Nivel resultado = nivelService.crearNivel(request);

        ArgumentCaptor<Nivel> captor = ArgumentCaptor.forClass(Nivel.class);
        verify(nivelRepository).save(captor.capture());

        assertNotNull(resultado);
        assertEquals(5, captor.getValue().getNivNum());
    }

    @Test
    void deberiaActualizarNivel() {
        NivelRequest request = new NivelRequest();
        request.setNivNum(6);

        when(nivelRepository.findById(1)).thenReturn(Optional.of(nivel));
        when(nivelRepository.save(any(Nivel.class))).thenReturn(nivel);

        Nivel resultado = nivelService.actualizarNivel(1, request);

        assertNotNull(resultado);
        assertEquals(6, resultado.getNivNum());
        verify(nivelRepository).findById(1);
        verify(nivelRepository).save(nivel);
    }
}
