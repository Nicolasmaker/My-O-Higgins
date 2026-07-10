package com.cahuinlabs.anotaciones.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    // Lee la URL del microservicio de Autenticacion desde application.properties
    // Si no está definida, usa localhost:8080 por defecto
    @Value("${app.services.autenticacion-url:http://localhost:8080}")
    private String autenticacionUrl;

    // Lee la URL del microservicio de HojaDeVida desde application.properties
    // Si no está definida, usa localhost:8084 por defecto
    @Value("${app.services.hojadevida-url:http://localhost:8084}")
    private String hojaDeVidaUrl;

    // Lee la URL del microservicio de GestionAcademica desde application.properties
    // Si no está definida, usa localhost:8087 por defecto
    @Value("${app.services.gestionacademica-url:http://localhost:8087}")
    private String gestionAcademicaUrl;

    @Bean
    public RestClient autenticacionRestClient() {
        return RestClient.builder()
                .baseUrl(autenticacionUrl)
                .build();
    }

    @Bean
    public RestClient hojaVidaRestClient() {
        return RestClient.builder()
                .baseUrl(hojaDeVidaUrl)
                .build();
    }

    @Bean
    public RestClient gestionAcademicaRestClient() {
        return RestClient.builder()
                .baseUrl(gestionAcademicaUrl)
                .build();
    }
}
