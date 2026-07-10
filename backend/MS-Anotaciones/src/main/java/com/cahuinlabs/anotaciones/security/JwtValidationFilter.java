package com.cahuinlabs.anotaciones.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.List;

// Filtro compartido: valida la firma/expiracion del JWT emitido por MS-Autenticacion
// (mismo secreto) y coloca la autenticacion con el rol que viene en el claim "rol".
// No consulta ninguna BD. Si el token es invalido/expirado, deja la peticion sin autenticar.
@Component
public class JwtValidationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret:TXlPaGlnZ2lucyEyMDI2IU1pY3JvU2VydmljaW9BdXRlbnRpY2FjaW9u}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(header.substring(7))
                    .getBody();
            String email = claims.getSubject();
            Object rol = claims.get("rol");
            List<SimpleGrantedAuthority> authorities = rol != null
                    ? List.of(new SimpleGrantedAuthority(rol.toString()))
                    : List.of();
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
