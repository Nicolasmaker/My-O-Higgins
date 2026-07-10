package com.myohiggins.calendario.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class RestClientConfig {

    // Lee la URL del microservicio de Gestion Academica desde application.properties
    // Si no está definida, usa localhost:8087 por defecto
    @Value("${app.services.gestionacademica-url:http://localhost:8087}")
    private String gestionAcademicaUrl;

    @Bean
    public RestClient gestionAcademicaRestClient() {
        return RestClient.builder()
                .baseUrl(gestionAcademicaUrl)
                // MS-GestionAcademica exige JWT en /asignatura/**; sin este interceptor la llamada
                // interna sale sin Authorization y ese servicio la rechaza con 403.
                .requestInterceptor((request, body, execution) -> {
                    ServletRequestAttributes attrs =
                            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    if (attrs != null) {
                        String authHeader = attrs.getRequest().getHeader("Authorization");
                        if (authHeader != null) {
                            request.getHeaders().set("Authorization", authHeader);
                        }
                    }
                    return execution.execute(request, body);
                })
                .build();
    }
}
