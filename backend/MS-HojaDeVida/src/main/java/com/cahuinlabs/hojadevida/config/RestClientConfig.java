package com.cahuinlabs.hojadevida.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    // Lee la URL del microservicio de Autenticacion desde application.properties
    @Value("${app.services.autenticacion-url:http://localhost:8080}")
    private String autenticacionUrl;

    // Lee la URL del microservicio de Gestion Matricula desde application.properties
    // Si no está definida, usa localhost:8081 por defecto (coordinar puerto con el equipo)
    @Value("${app.services.matricula-url:http://localhost:8086}")
    private String matriculaUrl;

    @Bean
    public RestClient autenticacionRestClient() {
        return RestClient.builder()
                .baseUrl(autenticacionUrl)
                .build();
    }

    @Bean
    public RestClient matriculaRestClient() {
        return RestClient.builder()
                .baseUrl(matriculaUrl)
                .build();
    }
}