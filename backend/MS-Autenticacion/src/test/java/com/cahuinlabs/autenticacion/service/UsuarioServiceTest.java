package com.cahuinlabs.autenticacion.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cahuinlabs.autenticacion.models.entities.usuarios.Usuario;
import com.cahuinlabs.autenticacion.repository.RolRepository;
import com.cahuinlabs.autenticacion.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioSimulado;

    @BeforeEach
    void setUp() {
        usuarioSimulado = new Usuario();
        usuarioSimulado.setUsuRut(12345678);
        usuarioSimulado.setUsuDvRut('9');
        usuarioSimulado.setUsuPNombre("Juan");
        usuarioSimulado.setUsuApePat("Perez");
        usuarioSimulado.setUsuEmail("juan@perez.com");
        usuarioSimulado.setUsuEstadoActividad(true);
    }

    @Test
    void deberiaListarUsuarios() {
        // ARRANGE
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuarioSimulado));

        // ACT
        List<Usuario> resultado = usuarioService.listarUsuarios();

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(12345678, resultado.get(0).getUsuRut());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void deberiaBuscarPorRutExistente() {
        // ARRANGE
        when(usuarioRepository.findById(12345678)).thenReturn(Optional.of(usuarioSimulado));

        // ACT
        Usuario resultado = usuarioService.buscarPorRut(12345678);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(12345678, resultado.getUsuRut());
        verify(usuarioRepository, times(1)).findById(12345678);
    }

    @Test
    void deberiaLanzarExcepcionAlBuscarPorRutInexistente() {
        // ARRANGE
        when(usuarioRepository.findById(99999999)).thenReturn(Optional.empty());

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.buscarPorRut(99999999);
        });

        assertEquals("No se encontro un usuario con el rut: 99999999", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(99999999);
    }

    @Test
    void deberiaDesactivarUsuario() {
        // ARRANGE
        when(usuarioRepository.findById(12345678)).thenReturn(Optional.of(usuarioSimulado));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSimulado);

        // ACT
        usuarioService.desactivarUsuario(12345678);

        // ASSERT
        assertFalse(usuarioSimulado.getUsuEstadoActividad());
        verify(usuarioRepository, times(1)).save(usuarioSimulado);
    }

    @Test
    void deberiaEliminarUsuario() {
        // ARRANGE
        when(usuarioRepository.findById(12345678)).thenReturn(Optional.of(usuarioSimulado));
        doNothing().when(rolRepository).deleteByUsuarioUsuRut(12345678);
        doNothing().when(usuarioRepository).delete(usuarioSimulado);

        // ACT
        usuarioService.eliminarUsuario(12345678);

        // ASSERT
        verify(rolRepository, times(1)).deleteByUsuarioUsuRut(12345678);
        verify(usuarioRepository, times(1)).delete(usuarioSimulado);
    }

    @Test
    void deberiaVerificarSiExisteUsuario() {
        // ARRANGE
        when(usuarioRepository.existsById(12345678)).thenReturn(true);

        // ACT
        boolean existe = usuarioService.existeUsuario(12345678);

        // ASSERT
        assertTrue(existe);
        verify(usuarioRepository, times(1)).existsById(12345678);
    }
}
