package com.myohiggins.calendario.service;

import com.myohiggins.calendario.dto.CalendarioEstudiantilDTO;
import java.util.List;

/**
 * Interfaz que define los métodos de negocio para CalendarioEstudiantil.
 * Permite mantener desacoplado el controlador de la implementación real.
 */
public interface CalendarioEstudiantilService {

    CalendarioEstudiantilDTO crear(CalendarioEstudiantilDTO dto);

    List<CalendarioEstudiantilDTO> obtenerTodos();

    CalendarioEstudiantilDTO obtenerPorId(Long id);

    CalendarioEstudiantilDTO actualizar(Long id, CalendarioEstudiantilDTO dto);

    void eliminar(Long id);
}
