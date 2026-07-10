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

import com.cahuinlabs.gestionAcademica.models.entities.Asignatura;
import com.cahuinlabs.gestionAcademica.models.request.AsignaturaRequest;
import com.cahuinlabs.gestionAcademica.repository.AsignaturaRepository;

@ExtendWith(MockitoExtension.class)
public class AsignaturaServiceTest {

    @Mock
    private AsignaturaRepository asignaturaRepository;

    @InjectMocks
    private AsignaturaService asignaturaService;

    private Asignatura asignatura;

    @BeforeEach
    void setUp() {
        asignatura = new Asignatura();
        asignatura.setIdAsi(1);
        asignatura.setAsiNombre("Matemática");
        asignatura.setAsiDescripcion("Asignatura base");
    }

    @Test
    void deberiaCrearAsignatura() {
        AsignaturaRequest request = new AsignaturaRequest();
        request.setAsiNom("Historia");
        request.setAsiDes("Historia del arte");

        when(asignaturaRepository.save(any(Asignatura.class))).thenReturn(asignatura);

        Asignatura resultado = asignaturaService.crearAsignatura(request);

        ArgumentCaptor<Asignatura> captor = ArgumentCaptor.forClass(Asignatura.class);
        verify(asignaturaRepository).save(captor.capture());

        assertNotNull(resultado);
        assertEquals("Historia", captor.getValue().getAsiNombre());
        assertEquals("Historia del arte", captor.getValue().getAsiDescripcion());
    }

    @Test
    void deberiaActualizarAsignatura() {
        AsignaturaRequest request = new AsignaturaRequest();
        request.setAsiNom("Ciencias");
        request.setAsiDes("Ciencias naturales");

        when(asignaturaRepository.findById(1)).thenReturn(Optional.of(asignatura));
        when(asignaturaRepository.save(any(Asignatura.class))).thenReturn(asignatura);

        Asignatura resultado = asignaturaService.actualizarAsignatura(1, request);

        assertNotNull(resultado);
        assertEquals("Ciencias", resultado.getAsiNombre());
        verify(asignaturaRepository).findById(1);
        verify(asignaturaRepository).save(asignatura);
    }
}
