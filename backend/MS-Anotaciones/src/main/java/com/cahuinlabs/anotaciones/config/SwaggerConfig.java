package com.cahuinlabs.anotaciones.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI anotacionesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de MS-Anotaciones")
                        .description("Microservicio para gestionar las anotaciones estudiantiles (anotaciones positivas y negativas de alumnos).")
                        .version("v0.0.1"));
    }
}
