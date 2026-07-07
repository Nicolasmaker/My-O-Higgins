package com.cahuinlabs.anotaciones.models.request;

import lombok.Data;

@Data
public class AnotacionDTO {

    private String anotTip;
    private String anotGravedad;
    private String anotDes;
    private Long funcionarioUsuRut;
    private Long idHojaVida;
}