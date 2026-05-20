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
import com.cahuinlabs.autenticacion.models.request.AuthRequest;
import com.cahuinlabs.autenticacion.models.response.AuthResponse;
import com.cahuinlabs.autenticacion.security.JwtService;

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

     //Generar token
        String jwtToken = jwtService.generarToken(userDetails);

     //Retornar token
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }

}
