package com.cahuinlabs.autenticacion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cahuinlabs.autenticacion.models.entities.usuarios.Usuario;
import com.cahuinlabs.autenticacion.models.request.AuthRequest;
import com.cahuinlabs.autenticacion.models.response.AuthResponse;
import com.cahuinlabs.autenticacion.security.JwtService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService    userDetailsService;
    private final JwtService            jwtService;

    public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtService jwtService){
        this.authenticationManager = authenticationManager;
        this.userDetailsService    = userDetailsService;
        this.jwtService            = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest){

     //Valida el correo y la contraseña
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

     //Se busca al usuario en la bd para generar el token
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());

     //Extraer datos del usuario para incluirlos en la respuesta
        Usuario usuario = (Usuario) userDetails;
        String rolNombre = usuario.getRol() != null ? usuario.getRol().getRolNombre() : null;

     //Generar token incluyendo el rol como claim, para que los demas microservicios
     //puedan leer el rol del usuario desde el token sin consultar la BD de Autenticacion.
        Map<String, Object> claims = new HashMap<>();
        if (rolNombre != null) {
            claims.put("rol", rolNombre);
        }
        String jwtToken = jwtService.generarToken(claims, userDetails);

        return ResponseEntity.ok(new AuthResponse(
            jwtToken,
            usuario.getUsuRut(),
            usuario.getUsuPNombre(),
            usuario.getUsuApePat(),
            usuario.getUsuEmail(),
            rolNombre,
            usuario.getUsuEstadoActividad()
        ));
    }

}
