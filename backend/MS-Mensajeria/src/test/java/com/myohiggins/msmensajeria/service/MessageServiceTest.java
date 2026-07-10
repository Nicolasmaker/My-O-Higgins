package com.myohiggins.msmensajeria.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import com.myohiggins.msmensajeria.dto.UsuarioDTO;
import com.myohiggins.msmensajeria.models.entities.Message;
import com.myohiggins.msmensajeria.models.request.MessageDTO;
import com.myohiggins.msmensajeria.repository.MessageRepository;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private RestClient autenticacionRestClient;

    private MessageService messageService;

    @BeforeEach
    void setUp() {
        messageService = new MessageService(autenticacionRestClient);
        ReflectionTestUtils.setField(messageService, "messageRepository", messageRepository);
    }

    @Test
    void deberiaEnviarMensajeCuandoAmbosUsuariosExisten() {
        MessageDTO request = new MessageDTO();
        request.setRemitenteRut(1L);
        request.setDestinatarioRut(2L);
        request.setAsunto("Hola");
        request.setContenido("Contenido");

        @SuppressWarnings("rawtypes")
        RestClient.RequestHeadersUriSpec headersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        @SuppressWarnings("rawtypes")
        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(autenticacionRestClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) (Object) headersUriSpec);
        when(headersUriSpec.uri(anyString(), anyLong())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(UsuarioDTO.class)).thenReturn(new UsuarioDTO(1L, "Juan", "Pérez"));

        Message mensajeGuardado = new Message();
        mensajeGuardado.setIdMensaje(10L);
        mensajeGuardado.setRemitenteRut(1L);
        mensajeGuardado.setDestinatarioRut(2L);
        mensajeGuardado.setAsunto("Hola");
        mensajeGuardado.setContenido("Contenido");
        mensajeGuardado.setEstadoLectura(false);
        when(messageRepository.save(any(Message.class))).thenReturn(mensajeGuardado);

        Message resultado = messageService.enviarMensaje(request);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getIdMensaje());
        assertEquals("Hola", resultado.getAsunto());
        assertFalse(resultado.getEstadoLectura());
        verify(messageRepository).save(any(Message.class));
    }
}
