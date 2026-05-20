package com.cahuinlabs.autenticacion.security;

import java.io.IOException;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
        @Nonnull HttpServletRequest request,
        @Nonnull HttpServletResponse response,
        @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {

     //Mira el header de la peticion buscando la autorizacion
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        
     //Si no tiene un header o no empieza con el Bearer, se deja pasar la peticion sin hacer nada
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

     //Extrae el token quitando los primeros 7 caracteres del bearer
        jwt = authHeader.substring(7); 

        userEmail = jwtService.extraerUsername(jwt); //Extrae el email del token

     //Si hay un email pero el usuario no esta autenticado, se autentica
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail); //Busca al usuario en la bd

         //Si el token es valido, se autentica al usuario
            if (jwtService.tokenEsValido(jwt, userDetails)) {

             //Pase de acceso oficial del spring security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );

                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );

             //Le da acceso al usuario
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

     //Continuar con el resto de los filtros
        filterChain.doFilter(request, response);
    }
}
