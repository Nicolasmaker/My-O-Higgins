package com.cahuinlabs.hojadevida.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Seguridad con interruptor: app.security.enabled=false (por defecto) => todo abierto,
// comportamiento identico al actual (dev). =true => exige token JWT valido.
// Rutas internas abiertas: MS-Anotaciones y MS-GestionMatricula consumen /api/hojas-vida y
// /api/antecedentes-apoderado de servidor a servidor SIN token; se dejan publicas para no
// romper esas llamadas (mismo criterio que las lecturas internas de MS-Autenticacion).
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtValidationFilter jwtFilter;

    @Value("${app.security.enabled:true}")
    private boolean securityEnabled;

    public SecurityConfig(JwtValidationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        if (!securityEnabled) {
            http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/h2-console/**").permitAll()
                // "/**" no matchea la ruta base exacta (ej. POST /api/hojas-vida sin nada
                // despues); hay que declarar tambien la ruta sin comodin para el POST de creacion.
                .requestMatchers("/api/hojas-vida", "/api/hojas-vida/**",
                        "/api/antecedentes-apoderado", "/api/antecedentes-apoderado/**").permitAll()
                .anyRequest().authenticated())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
