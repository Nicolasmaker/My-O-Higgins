package com.cahuinlabs.GestionReuniones.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    // Lee la URL del microservicio de autenticacion desde application.properties
    // Si no está definida, usa localhost:8080 por defecto
    @Value("${app.services.autenticacion-url:http://localhost:8080}")
    private String autenticacionUrl;

    @Value("${app.services.calendario-url:http://localhost:8085}")
    private String calendarioUrl;

    @Value("${app.services.mensajeria-url:http://localhost:8089}")
    private String mensajeriaUrl;

    @Bean
    public RestClient autenticacionRestClient() {
        return RestClient.builder()
                .baseUrl(autenticacionUrl)
                .build();
    }

    @Bean
    public RestClient calendarioRestClient() {
        return RestClient.builder()
                .baseUrl(calendarioUrl)
                .build();
    }

    @Bean
    public RestClient mensajeriaRestClient() {
        return RestClient.builder()
                .baseUrl(mensajeriaUrl)
                .build();
    }
}
