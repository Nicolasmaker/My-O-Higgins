package com.myohiggins.msmatricula.config;

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

    @Bean
    public RestClient autenticacionRestClient() {
        return RestClient.builder()
                .baseUrl(autenticacionUrl)
                .build();
    }
}
