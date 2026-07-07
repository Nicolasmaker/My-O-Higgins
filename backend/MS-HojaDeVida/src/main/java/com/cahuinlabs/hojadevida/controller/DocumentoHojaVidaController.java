package com.cahuinlabs.hojadevida.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cahuinlabs.hojadevida.dto.DocumentoHojaVidaDTO;
import com.cahuinlabs.hojadevida.model.DocumentoHojaVida;
import com.cahuinlabs.hojadevida.service.DocumentoHojaVidaService;

@RestController
@RequestMapping("/api/hojas-vida")
public class DocumentoHojaVidaController {

    private final DocumentoHojaVidaService service;

    public DocumentoHojaVidaController(DocumentoHojaVidaService service) {
        this.service = service;
    }

    @PostMapping("/{idHojaVida}/documentos")
    public ResponseEntity<DocumentoHojaVidaDTO> subirDocumento(
            @PathVariable Long idHojaVida,
            @RequestParam("file") MultipartFile file) {
        DocumentoHojaVidaDTO creado = service.subirDocumento(idHojaVida, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/{idHojaVida}/documentos")
    public ResponseEntity<List<DocumentoHojaVidaDTO>> listarPorHojaVida(@PathVariable Long idHojaVida) {
        return ResponseEntity.ok(service.listarPorHojaVida(idHojaVida));
    }

    @GetMapping("/documentos/{id}/descarga")
    public ResponseEntity<byte[]> descargarDocumento(@PathVariable Long id) {
        DocumentoHojaVida documento = service.obtenerParaDescarga(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(documento.getTipoContenido()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + documento.getNombreArchivo() + "\"")
                .body(documento.getContenido());
    }

    @DeleteMapping("/documentos/{id}")
    public ResponseEntity<Void> eliminarDocumento(@PathVariable Long id) {
        service.eliminarDocumento(id);
        return ResponseEntity.noContent().build();
    }
}
