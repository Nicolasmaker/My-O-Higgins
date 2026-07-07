package com.cahuinlabs.hojadevida.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cahuinlabs.hojadevida.dto.DocumentoHojaVidaDTO;
import com.cahuinlabs.hojadevida.exception.ResourceNotFoundException;
import com.cahuinlabs.hojadevida.model.DocumentoHojaVida;
import com.cahuinlabs.hojadevida.model.HojaVidaEstudiante;
import com.cahuinlabs.hojadevida.repository.DocumentoHojaVidaRepository;
import com.cahuinlabs.hojadevida.repository.HojaVidaRepository;

@Service
public class DocumentoHojaVidaService {

    private static final long TAMANIO_MAXIMO_BYTES = 5L * 1024 * 1024; // 5MB
    private static final Set<String> EXTENSIONES_PERMITIDAS = Set.of("pdf", "jpg", "jpeg", "png", "doc", "docx");

    private final DocumentoHojaVidaRepository documentoRepository;
    private final HojaVidaRepository hojaVidaRepository;

    public DocumentoHojaVidaService(DocumentoHojaVidaRepository documentoRepository,
                                     HojaVidaRepository hojaVidaRepository) {
        this.documentoRepository = documentoRepository;
        this.hojaVidaRepository = hojaVidaRepository;
    }

    public DocumentoHojaVidaDTO subirDocumento(Long idHojaVida, MultipartFile file) {
        HojaVidaEstudiante hojaVida = hojaVidaRepository.findById(idHojaVida)
                .orElseThrow(() -> new ResourceNotFoundException("Hoja de vida no encontrada: " + idHojaVida));

        validarArchivo(file);

        DocumentoHojaVida documento = new DocumentoHojaVida();
        documento.setHojaVida(hojaVida);
        documento.setNombreArchivo(file.getOriginalFilename());
        documento.setTipoContenido(file.getContentType());
        documento.setTamanioBytes(file.getSize());
        documento.setFechaSubida(LocalDateTime.now());
        try {
            documento.setContenido(file.getBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException("No se pudo leer el archivo enviado.");
        }

        return mapearADTO(documentoRepository.save(documento));
    }

    public List<DocumentoHojaVidaDTO> listarPorHojaVida(Long idHojaVida) {
        return documentoRepository.findByHojaVida_IdHojaVida(idHojaVida).stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    public DocumentoHojaVida obtenerParaDescarga(Long idDocumento) {
        return documentoRepository.findById(idDocumento)
                .orElseThrow(() -> new ResourceNotFoundException("Documento no encontrado: " + idDocumento));
    }

    public void eliminarDocumento(Long idDocumento) {
        if (!documentoRepository.existsById(idDocumento)) {
            throw new ResourceNotFoundException("Documento no encontrado: " + idDocumento);
        }
        documentoRepository.deleteById(idDocumento);
    }

    private void validarArchivo(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Debes adjuntar un archivo.");
        }
        if (file.getSize() > TAMANIO_MAXIMO_BYTES) {
            throw new IllegalArgumentException("El archivo supera el tamaño máximo permitido (5MB).");
        }
        String nombre = file.getOriginalFilename();
        String extension = nombre != null && nombre.contains(".")
                ? nombre.substring(nombre.lastIndexOf('.') + 1).toLowerCase()
                : "";
        if (!EXTENSIONES_PERMITIDAS.contains(extension)) {
            throw new IllegalArgumentException(
                    "Tipo de archivo no permitido. Solo se aceptan: " + String.join(", ", EXTENSIONES_PERMITIDAS));
        }
    }

    private DocumentoHojaVidaDTO mapearADTO(DocumentoHojaVida entidad) {
        return new DocumentoHojaVidaDTO(
                entidad.getIdDocumento(),
                entidad.getHojaVida().getIdHojaVida(),
                entidad.getNombreArchivo(),
                entidad.getTipoContenido(),
                entidad.getTamanioBytes(),
                entidad.getFechaSubida()
        );
    }
}
