package com.cahuinlabs.anotaciones.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import com.cahuinlabs.anotaciones.dto.AnotacionResponseDTO;
import com.cahuinlabs.anotaciones.models.entities.Anotacion;
import com.cahuinlabs.anotaciones.models.request.AnotacionDTO;
import com.cahuinlabs.anotaciones.dto.FuncionarioDTO;
import com.cahuinlabs.anotaciones.dto.HojaVidaDTO;
import com.cahuinlabs.anotaciones.repository.AnotacionRepository;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AnotacionServiceTest {

    @Mock
    private AnotacionRepository anotacionRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RestClient autenticacionRestClient;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RestClient hojaVidaRestClient;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RestClient gestionAcademicaRestClient;

    @InjectMocks
    private AnotacionService anotacionService;

    @Test
    void deberiaCrearAnotacionCorrectamente() {
        // ==========================================
        // ARRANGE (Preparar los datos falsos)
        // ==========================================
        AnotacionDTO nuevoDto = new AnotacionDTO();
        nuevoDto.setFuncionarioUsuRut(12345678L);
        nuevoDto.setIdHojaVida(1L);

        Anotacion anotacionSimulada = new Anotacion();
        anotacionSimulada.setIdAnot(1L);
        anotacionSimulada.setAnotTip("Positiva");

        when(anotacionRepository.save(any(Anotacion.class))).thenReturn(anotacionSimulada);

        // Le enseñamos a Mockito a responder con los DTOs que espera el servicio en lugar de Boolean
        lenient().when(autenticacionRestClient.get().uri(anyString(), any(Object[].class)).retrieve().body(FuncionarioDTO.class))
            .thenReturn(mock(FuncionarioDTO.class));
        lenient().when(hojaVidaRestClient.get().uri(anyString(), any(Object[].class)).retrieve().body(HojaVidaDTO.class))
            .thenReturn(mock(HojaVidaDTO.class));

            
        // ==========================================
        // ACT (Ejecutar el método que estamos probando)
        // ==========================================
        Anotacion resultado = anotacionService.crearAnotacion(nuevoDto);

        // ==========================================
        // ASSERT (Verificar que funcionó)
        // ==========================================
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(1L, resultado.getIdAnot(), "El ID debería ser 1");
        
        verify(anotacionRepository, times(1)).save(any(Anotacion.class));
    }

    @Test
    void deberiaObtenerTodasLasAnotaciones() {
        // ARRANGE
        Anotacion anotacion1 = new Anotacion();
        Anotacion anotacion2 = new Anotacion();
        List<Anotacion> listaSimulada = Arrays.asList(anotacion1, anotacion2);

        when(anotacionRepository.findAll()).thenReturn(listaSimulada);

        // ACT
        List<AnotacionResponseDTO> resultado = anotacionService.obtenerTodas();

        // ASSERT
        assertNotNull(resultado);
        assertEquals(2, resultado.size(), "Debería retornar una lista con 2 anotaciones");
        verify(anotacionRepository, times(1)).findAll();
    }
}