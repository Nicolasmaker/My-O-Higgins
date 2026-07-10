package com.cahuinlabs.hojadevida.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI hojaDeVidaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de MS-HojaDeVida")
                        .description("Microservicio para gestionar las hojas de vida de estudiantes y sus antecedentes (médicos, académicos, apoderados).")
                        .version("v0.0.1"));
    }
}
