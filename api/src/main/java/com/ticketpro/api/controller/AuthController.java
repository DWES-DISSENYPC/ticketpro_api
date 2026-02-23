package com.ticketpro.api.controller;

import com.ticketpro.api.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Esta es la ruta que permitimos en WebSecurityConfig
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager; // El motor que verifica las credenciales

    @Autowired
    JwtUtils jwtUtils; // Nuestra herramienta para crear el Token

    @PostMapping("/login")
    public Map<String, String> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        
        // 1. Intentamos autenticar al usuario con los datos de Postman
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.get("username"), 
                        loginRequest.get("password")
                )
        );

        // 2. Si las credenciales son correctas, lo guardamos en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generamos el Token JWT usando el nombre del usuario autenticado
        String jwt = jwtUtils.generateJwtToken(authentication.getName());

        // 4. Preparamos la respuesta con el Token para el cliente
        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        
        return response; // Postman recibirá un JSON con el token
    }
}
