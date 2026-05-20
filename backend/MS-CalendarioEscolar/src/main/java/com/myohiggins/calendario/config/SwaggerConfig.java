package com.myohiggins.calendario.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI calendarioOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de MS-CalendarioEscolar")
                        .description("Microservicio para la gestión del calendario escolar (eventos, feriados, efemérides).")
                        .version("v1.0.0"));
    }
}
