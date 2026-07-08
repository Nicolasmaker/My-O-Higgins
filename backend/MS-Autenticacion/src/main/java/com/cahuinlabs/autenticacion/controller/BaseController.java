package com.cahuinlabs.autenticacion.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import com.cahuinlabs.autenticacion.models.request.InfoVersion;
import org.springframework.beans.factory.annotation.Value;


@RequestMapping
@RestController
public class BaseController {

    @Value("${app.nombre}")
    private String nombreApp;

    @Value("${app.version}")
    private String versionApp;

    @GetMapping("")
    public InfoVersion base() {
        return new InfoVersion(nombreApp, versionApp);
    } 
}
