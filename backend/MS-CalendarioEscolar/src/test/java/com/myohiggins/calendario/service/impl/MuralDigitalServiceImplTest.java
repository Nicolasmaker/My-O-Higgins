package com.myohiggins.calendario.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.myohiggins.calendario.dto.MuralDigitalDTO;
import com.myohiggins.calendario.entity.MuralDigital;
import com.myohiggins.calendario.repository.MuralDigitalRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class MuralDigitalServiceImplTest {

    @Mock
    private MuralDigitalRepository repository;

    @InjectMocks
    private MuralDigitalServiceImpl service;

    private MuralDigital muralSimulado;
    private MuralDigitalDTO dtoSimulado;

    @BeforeEach
    void setUp() {
        muralSimulado = new MuralDigital();
        muralSimulado.setIdMurDig(1L);
        muralSimulado.setTitulo("Aviso Importante");
        muralSimulado.setContenido("Contenido del aviso");
        muralSimulado.setFechaPublicacion(LocalDate.now());
        muralSimulado.setFuncionarioUsuRut(12345678L);

        dtoSimulado = new MuralDigitalDTO(1L, "Aviso Importante", "Contenido del aviso", LocalDate.now(), 12345678L);
    }

    @Test
    void deberiaCrearMuralDigital() {
        // ARRANGE
        when(repository.save(any(MuralDigital.class))).thenReturn(muralSimulado);

        // ACT
        MuralDigitalDTO resultado = service.crear(dtoSimulado);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Aviso Importante", resultado.getTitulo());
        assertEquals(1L, resultado.getIdMurDig());
        verify(repository, times(1)).save(any(MuralDigital.class));
    }

    @Test
    void deberiaObtenerTodos() {
        // ARRANGE
        when(repository.findAll()).thenReturn(Arrays.asList(muralSimulado));

        // ACT
        List<MuralDigitalDTO> resultado = service.obtenerTodos();

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getIdMurDig());
        verify(repository, times(1)).findAll();
    }

    @Test
    void deberiaObtenerPorIdExistente() {
        // ARRANGE
        when(repository.findById(1L)).thenReturn(Optional.of(muralSimulado));

        // ACT
        MuralDigitalDTO resultado = service.obtenerPorId(1L);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdMurDig());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void deberiaLanzarExcepcionAlObtenerPorIdInexistente() {
        // ARRANGE
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            service.obtenerPorId(99L);
        });

        assertTrue(exception.getMessage().contains("Publicación de mural no encontrada"));
        verify(repository, times(1)).findById(99L);
    }

    @Test
    void deberiaActualizarMuralDigital() {
        // ARRANGE
        MuralDigitalDTO updateDto = new MuralDigitalDTO(null, "Nuevo Titulo", "Nuevo Contenido", null, null);
        
        when(repository.findById(1L)).thenReturn(Optional.of(muralSimulado));
        when(repository.save(any(MuralDigital.class))).thenReturn(muralSimulado); // muralSimulado ya habrá mutado

        // ACT
        MuralDigitalDTO resultado = service.actualizar(1L, updateDto);

        // ASSERT
        assertNotNull(resultado);
        // Note: the service updates the entity in-place then saves.
        assertEquals("Nuevo Titulo", muralSimulado.getTitulo());
        assertEquals("Nuevo Contenido", muralSimulado.getContenido());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(MuralDigital.class));
    }

    @Test
    void deberiaEliminarMuralDigital() {
        // ARRANGE
        when(repository.findById(1L)).thenReturn(Optional.of(muralSimulado));
        doNothing().when(repository).delete(muralSimulado);

        // ACT
        service.eliminar(1L);

        // ASSERT
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(muralSimulado);
    }
}
