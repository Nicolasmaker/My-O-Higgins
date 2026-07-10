package com.myohiggins.calendario.service;

import com.myohiggins.calendario.dto.MuralDigitalDTO;
import java.util.List;

/**
 * Contrato de la lógica de negocio del Mural Digital.
 */
public interface MuralDigitalService {
    MuralDigitalDTO crear(MuralDigitalDTO dto);
    List<MuralDigitalDTO> obtenerTodos();
    MuralDigitalDTO obtenerPorId(Long id);
    MuralDigitalDTO actualizar(Long id, MuralDigitalDTO dto);
    void eliminar(Long id);
}
