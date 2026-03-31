package com.ticketpro.api.controller;

import com.ticketpro.api.dto.PasswordResetRequestDTO;
import com.ticketpro.api.dto.RegistroRequestDTO;
import com.ticketpro.api.model.Usuario;
import com.ticketpro.api.security.jwt.JwtUtils;
import com.ticketpro.api.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Esta es la ruta que permitimos en WebSecurityConfig
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager; // El motor que verifica las credenciales

    @Autowired
    UsuarioService usuarioService;

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

    @PostMapping("/logout")
public ResponseEntity<?> logoutUser() {
    // En JWT, el servidor no necesita hacer nada especial para invalidar el token
    // (A menos que uses una "Blacklist" de tokens, que es más avanzado).
    return ResponseEntity.ok("Sesión cerrada con éxito. El token ya no debe ser utilizado.");
}

    @PostMapping("/register")
public ResponseEntity<?> registrarUsuario(@RequestBody RegistroRequestDTO registroDTO) {
 
    usuarioService.crearNuevoUsuario(registroDTO);

    // Creamos un mapa para que la respuesta sea un JSON: {"message": "..."} para que lo coja bien el cliente
    Map<String, String> response = new HashMap<>();
    response.put("message", "Usuario registrado con éxito. ¡Ya puedes iniciar sesión!");

    return ResponseEntity.ok(response);
}

// Añadir en AuthController.java

@PostMapping("/forgot-password")
public ResponseEntity<?> forgotPassword(@RequestBody PasswordResetRequestDTO request) {
    usuarioService.generarTokenRecuperacion(request.getEmail());
    Map<String, String> res = new HashMap<>();
    res.put("message", "Si el email está registrado, recibirás un enlace de recuperación en unos minutos.");
    return ResponseEntity.ok(res);
}

@PostMapping("/reset-password")
public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
    // Recibimos token y nueva password del cuerpo de la petición
    usuarioService.resetearPassword(request.get("token"), request.get("password"));
    Map<String, String> res = new HashMap<>();
    res.put("message", "Contraseña actualizada con éxito.");
    return ResponseEntity.ok(res);
}

@GetMapping("/perfil")
public ResponseEntity<Map<String, Object>> obtenerPerfil(Principal principal) {
    return ResponseEntity.ok(usuarioService.obtenerDatosPerfilCompleto(principal.getName()));
}

}
