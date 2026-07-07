package com.cahuinlabs.hojadevida.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import com.cahuinlabs.hojadevida.dto.EstudianteDTO;
import com.cahuinlabs.hojadevida.dto.HojaVidaEstudianteDTO;
import com.cahuinlabs.hojadevida.dto.MatriculaDTO;
import com.cahuinlabs.hojadevida.model.HojaVidaEstudiante;
import com.cahuinlabs.hojadevida.repository.HojaVidaRepository;

@ExtendWith(MockitoExtension.class)
public class HojaVidaServiceTest {

    @Mock
    private HojaVidaRepository hojaVidaRepository;

    @Mock
    private RestClient autenticacionRestClient;

    @Mock
    private RestClient matriculaRestClient;

    private HojaVidaService hojaVidaService;

    @BeforeEach
    void setUp() {
        hojaVidaService = new HojaVidaService(autenticacionRestClient, matriculaRestClient, hojaVidaRepository);
    }

    @Test
    void deberiaCrearHojaVidaCuandoEstudianteYMatriculaExisten() {
        HojaVidaEstudianteDTO request = new HojaVidaEstudianteDTO();
        request.setEstudianteUsuRut(12345678L);
        request.setMatriculaId(99L);

        @SuppressWarnings("rawtypes")
        RestClient.RequestHeadersUriSpec headersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        @SuppressWarnings("rawtypes")
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(autenticacionRestClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) headersUriSpec);
        when(headersUriSpec.uri(anyString(), anyLong())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(EstudianteDTO.class)).thenReturn(new EstudianteDTO(12345678L, "Juan", "Pérez", new EstudianteDTO.RolDTO("ROLE_ESTUDIANTE")));

        when(matriculaRestClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) headersUriSpec);
        when(headersUriSpec.uri(anyString(), anyLong())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(MatriculaDTO.class)).thenReturn(new MatriculaDTO(99L, "ACTIVA", 12345678L));

        HojaVidaEstudiante entidadGuardada = new HojaVidaEstudiante();
        entidadGuardada.setIdHojaVida(1L);
        entidadGuardada.setEstudianteUsuRut(12345678L);
        entidadGuardada.setMatriculaId(99L);
        when(hojaVidaRepository.save(any(HojaVidaEstudiante.class))).thenReturn(entidadGuardada);

        HojaVidaEstudianteDTO resultado = hojaVidaService.crearHojaVida(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdHojaVida());
        assertEquals(12345678L, resultado.getEstudianteUsuRut());
        verify(hojaVidaRepository).save(any(HojaVidaEstudiante.class));
    }
}
