package com.cahuinlabs.hojadevida;

import com.cahuinlabs.hojadevida.repository.HojaVidaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HojaDeVidaApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HojaVidaRepository hojaVidaRepository;

    @BeforeEach
    void setUp() {
        hojaVidaRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        // Verifica que la aplicación levanta el contexto de Spring sin errores.
    }

    @Test
    void crearHojaDeVida_debeRetornar201YObjetoConId() throws Exception {
        String requestBody = objectMapper.writeValueAsString(
                new HojaVidaRequest(12345678L, 1L)
        );

        mockMvc.perform(post("/api/hojas-vida")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idHojaVida").isNumber())
                .andExpect(jsonPath("$.estudianteUsuRut", is(12345678)))
                .andExpect(jsonPath("$.matriculaId", is(1)));
    }

    @Test
    void obtenerTodasLasHojasDeVida_debeRetornarLista() throws Exception {
        crearHojaDeVidaDirecta(11111111L, 1L);
        crearHojaDeVidaDirecta(22222222L, 2L);

        mockMvc.perform(get("/api/hojas-vida"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void obtenerHojaVidaPorId_debeRetornarElRecursoCorrecto() throws Exception {
        Long idHojaVida = crearHojaDeVidaDirecta(33333333L, 3L);

        mockMvc.perform(get("/api/hojas-vida/{idHojaVida}", idHojaVida))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idHojaVida", is(idHojaVida.intValue())))
                .andExpect(jsonPath("$.estudianteUsuRut", is(33333333)))
                .andExpect(jsonPath("$.matriculaId", is(3)));
    }

    @Test
    void actualizarHojaVida_debeModificarLosDatosExistentes() throws Exception {
        Long idHojaVida = crearHojaDeVidaDirecta(44444444L, 4L);

        String requestBody = objectMapper.writeValueAsString(
                new HojaVidaRequest(44444444L, 5L)
        );

        mockMvc.perform(put("/api/hojas-vida/{idHojaVida}", idHojaVida)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idHojaVida", is(idHojaVida.intValue())))
                .andExpect(jsonPath("$.estudianteUsuRut", is(44444444)))
                .andExpect(jsonPath("$.matriculaId", is(5)));
    }

    @Test
    void eliminarHojaVida_debeRetornarNoContent() throws Exception {
        Long idHojaVida = crearHojaDeVidaDirecta(55555555L, 6L);

        mockMvc.perform(delete("/api/hojas-vida/{idHojaVida}", idHojaVida))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/hojas-vida/{idHojaVida}", idHojaVida))
                .andExpect(status().isNotFound());
    }

    private Long crearHojaDeVidaDirecta(Long rut, Long matriculaId) {
        com.cahuinlabs.hojadevida.model.HojaVidaEstudiante nuevaHoja = new com.cahuinlabs.hojadevida.model.HojaVidaEstudiante();
        nuevaHoja.setEstudianteUsuRut(rut);
        nuevaHoja.setMatriculaId(matriculaId);
        return hojaVidaRepository.save(nuevaHoja).getIdHojaVida();
    }

    private static class HojaVidaRequest {
        public Long estudianteUsuRut;
        public Long matriculaId;

        public HojaVidaRequest(Long estudianteUsuRut, Long matriculaId) {
            this.estudianteUsuRut = estudianteUsuRut;
            this.matriculaId = matriculaId;
        }
    }
}
