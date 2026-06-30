package com.myohiggins.calendario.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    // Lee la URL del microservicio de Gestion Academica desde application.properties
    // Si no está definida, usa localhost:8083 por defecto (coordinar puerto con el equipo)
    @Value("${app.services.gestionacademica-url:http://localhost:8087}")
    private String gestionAcademicaUrl;

    @Bean
    public RestClient gestionAcademicaRestClient() {
        return RestClient.builder()
                .baseUrl(gestionAcademicaUrl)
                .build();
    }
}
