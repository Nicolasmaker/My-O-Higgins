package com.cahuinlabs.autenticacion.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {

    // Secreto de firma JWT externalizado: en producción se DEBE inyectar vía la variable
    // de entorno JWT_SECRET (o la propiedad jwt.secret). El valor por defecto es solo para
    // desarrollo local; NO debe usarse en producción. Rotar el secreto invalida los tokens
    // emitidos (los usuarios vuelven a iniciar sesión).
    @Value("${jwt.secret:TXlPaGlnZ2lucyEyMDI2IU1pY3JvU2VydmljaW9BdXRlbnRpY2FjaW9u}")
    private String secretKey;

    // Tiempo de expiración del token en milisegundos (por defecto 24h). Externalizado para
    // poder acortarlo en producción.
    @Value("${jwt.expiration-ms:86400000}")
    private long expirationMs;

 //Generacion de token
    public String generarToken(UserDetails userDetails){
        return generarToken(new HashMap<>(), userDetails);
    }

    public String generarToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername()) //sera el email del usuario
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
    }

 //Leer / extraer datos del token
    public String extraerUsername(String token){
        return extraerClaim(token, Claims::getSubject);
    }

    public <T> T extraerClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extraerTodosLosClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extraerTodosLosClaims(String token){
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

 //Validar token
    public boolean tokenEsValido(String token, UserDetails userDetails){
        final String username = extraerUsername(token);
        return (username.equals(userDetails.getUsername()) && !tokenHaExpirado(token));
    }

    private boolean tokenHaExpirado(String token){
        return extraerExpiracion(token).before(new Date());
    }

    private Date extraerExpiracion(String token){
        return extraerClaim(token, Claims::getExpiration);
    }

 //Metodo para convertir el String en una clave criptografica
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
