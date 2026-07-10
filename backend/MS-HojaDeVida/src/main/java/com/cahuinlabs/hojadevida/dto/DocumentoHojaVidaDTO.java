package com.cahuinlabs.hojadevida.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Sin el campo `contenido` (bytes) a propósito: listar documentos no debe cargar
// los bytes de todos, solo se traen en el endpoint de descarga de un documento puntual.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoHojaVidaDTO {
    private Long idDocumento;
    private Long idHojaVida;
    private String nombreArchivo;
    private String tipoContenido;
    private Long tamanioBytes;
    private LocalDateTime fechaSubida;
}
