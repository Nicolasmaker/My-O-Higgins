package com.cahuinlabs.GestionReuniones.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import com.cahuinlabs.GestionReuniones.models.entities.BitReunionApoderado;
import com.cahuinlabs.GestionReuniones.models.entities.BitReunionIndividual;
import com.cahuinlabs.GestionReuniones.models.request.ActualizarFirmasRequest;
import com.cahuinlabs.GestionReuniones.models.request.ReunionIndividualRequest;
import com.cahuinlabs.GestionReuniones.repository.BitReunionApoderadoRepository;
import com.cahuinlabs.GestionReuniones.repository.BitReunionGeneralRepository;
import com.cahuinlabs.GestionReuniones.repository.BitReunionIndividualRepository;
import com.cahuinlabs.GestionReuniones.dto.FuncionarioDTO;

@ExtendWith(MockitoExtension.class)
public class GestionReunionesServiceTest {

    @Mock
    private BitReunionApoderadoRepository baseRepository;

    @Mock
    private BitReunionIndividualRepository individualRepository;

    @Mock
    private BitReunionGeneralRepository generalRepository;

    @Mock
    private RestClient autenticacionRestClient;

    @Mock
    private RestClient calendarioRestClient;

    @Mock
    private RestClient mensajeriaRestClient;

    private GestionReunionesService gestionReunionesService;

    private BitReunionApoderado base;

    @BeforeEach
    void setUp() {
        base = new BitReunionApoderado();
        base.setIdBitReu(1L);
        base.setBitReuFec(LocalDate.of(2024, 1, 10));
        base.setBitReuCompromisos("Compromiso");
        base.setBitReuObs("Obs");
        base.setDocenteUsuRut(12345678L);

        gestionReunionesService = new GestionReunionesService(autenticacionRestClient, calendarioRestClient, mensajeriaRestClient);
        ReflectionTestUtils.setField(gestionReunionesService, "baseRepository", baseRepository);
        ReflectionTestUtils.setField(gestionReunionesService, "individualRepository", individualRepository);
        ReflectionTestUtils.setField(gestionReunionesService, "generalRepository", generalRepository);
    }

    @Test
    void deberiaRegistrarReunionIndividual() {
        ReunionIndividualRequest request = new ReunionIndividualRequest();
        request.setBitReuFec(LocalDate.of(2024, 1, 10));
        request.setBitReuCompromisos("Compromiso");
        request.setBitReuObs("Obs");
        request.setDocenteUsuRut(12345678L);
        request.setBitReuIndMotivReu("Motivo");
        request.setBitReuIndTemTrat("Tema");

        @SuppressWarnings("rawtypes")
        RestClient.RequestHeadersUriSpec headersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        @SuppressWarnings("rawtypes")
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(autenticacionRestClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) headersUriSpec);
        when(headersUriSpec.uri(anyString(), anyLong())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(FuncionarioDTO.class)).thenReturn(new FuncionarioDTO(12345678L,"k", "Juan", "Pérez", null));
        when(baseRepository.save(any(BitReunionApoderado.class))).thenReturn(base);
        when(individualRepository.save(any(BitReunionIndividual.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BitReunionIndividual resultado = gestionReunionesService.registrarReunionIndividual(request);

        assertNotNull(resultado);
        assertEquals("Motivo", resultado.getBitReuIndMotivReu());
        assertEquals("Tema", resultado.getBitReuIndTemTrat());
        assertEquals(0, resultado.getBitReuIndFirmaDoc());
        verify(baseRepository).save(any(BitReunionApoderado.class));
        verify(individualRepository).save(any(BitReunionIndividual.class));
    }

    @Test
    void deberiaActualizarFirmas() {
        ActualizarFirmasRequest request = new ActualizarFirmasRequest();
        request.setFirmaDoc(1);
        request.setFirmaApo(1);

        BitReunionIndividual individual = new BitReunionIndividual();
        individual.setIdBitReuInd(5L);
        individual.setBitReuIndFirmaDoc(0);
        individual.setBitReuIndFirmaApo(0);

        when(individualRepository.findById(5L)).thenReturn(Optional.of(individual));
        when(individualRepository.save(any(BitReunionIndividual.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BitReunionIndividual resultado = gestionReunionesService.actualizarFirmas(5L, request);

        assertNotNull(resultado);
        assertEquals(1, resultado.getBitReuIndFirmaDoc());
        assertEquals(1, resultado.getBitReuIndFirmaApo());
        verify(individualRepository).findById(5L);
        verify(individualRepository).save(individual);
    }
}
