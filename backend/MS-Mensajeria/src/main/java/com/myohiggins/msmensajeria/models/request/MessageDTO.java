package com.myohiggins.msmensajeria.models.request;

import lombok.Data;

@Data
public class MessageDTO {

    private Long remitenteRut;
    private Long destinatarioRut;
    private String asunto;
    private String contenido;
}
