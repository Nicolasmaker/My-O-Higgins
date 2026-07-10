package com.myohiggins.msmensajeria.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI mensajeriaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de MS-Mensajeria")
                        .description("Microservicio para la gestión de mensajería (comunicaciones entre apoderados, docentes y administración).")
                        .version("v0.0.1"));
    }
}
