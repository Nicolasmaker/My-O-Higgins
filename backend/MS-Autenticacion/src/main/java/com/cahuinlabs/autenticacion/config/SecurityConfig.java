package com.cahuinlabs.autenticacion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.cahuinlabs.autenticacion.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider  authenticationProvider;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

 //Reglas de acceso
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF porque se esta usando jwt
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login").permitAll() //Ruta del login publica
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll() // Rutas de Swagger públicas

             //Solo los usuarios con ROLE_DOCENTE pueden acceder a /funcionarios/docente y sus subrutas
                .requestMatchers("/funcionarios/docente/**").hasAuthority("ROLE_DOCENTE")
            
             //Solo los administradores (o directores) pueden registrar a otros funcionarios
                .requestMatchers(HttpMethod.POST, "/funcionarios/**").hasAuthority("ROLE_DIRECTIVO")
            
             //Solo los apoderados pueden ver las rutas de apoderados
                .requestMatchers("/apoderados/**").hasAuthority("ROLE_APODERADO")

             //Solo los inspectores pueden ver las rutas de inspectores
                .requestMatchers("/funcionarios/inspector/**").hasAuthority("ROLE_INSPECTOR")

             //Solo los estudiantes pueden ver las rutas de estudiantes
                .requestMatchers("/estudiantes/**").hasAuthority("ROLE_ESTUDIANTE")

                .anyRequest().authenticated() //Todas las demas rutas requerien un toekn valido
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //Esto es para q no use cookies pq la peticion sera validada por el token
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); //Se cambia el filtro por defecto por el que se creo 
        return http.build();
    }
}
