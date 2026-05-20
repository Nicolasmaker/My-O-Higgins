package com.cahuinlabs.GestionReuniones.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI reunionesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de MS-GestionReuniones")
                        .description("Microservicio para la gestión de reuniones de apoderados, docentes y el consejo escolar.")
                        .version("v0.0.1"));
    }
}
