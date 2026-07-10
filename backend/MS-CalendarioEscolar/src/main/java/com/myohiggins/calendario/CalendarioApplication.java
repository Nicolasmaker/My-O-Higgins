package com.myohiggins.calendario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal que inicializa el microservicio MS-CalendarioEscolar.
 * Contiene el método main que arranca el contexto de Spring Boot.
 */
@SpringBootApplication
public class CalendarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalendarioApplication.class, args);
        System.out.println("====== MS-CalendarioEscolar inicializado correctamente ======");
    }
}
