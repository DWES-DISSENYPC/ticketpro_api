package com.ticketpro.api.controller;

import com.ticketpro.api.dto.PasswordResetRequestDTO;
import com.ticketpro.api.dto.RegistroRequestDTO;
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

/* ###### CONTROLADOR DE AUTENTICACION ###### */
// ------ Expone Los Endpoints Relacionados Con El Login Y Registro ------
@RestController
@RequestMapping("/api/auth") // ------ Esta Es La Ruta Que Permitimos En WebSecurityConfig ------
public class AuthController {

    /* ###### DEPENDENCIAS INYECTADAS ###### */

    // ------ El Motor Que Verifica Las Credenciales ------
    @Autowired
    AuthenticationManager authenticationManager;

    // ------ Servicio Para La Gestion De Usuarios ------
    @Autowired
    UsuarioService usuarioService;

    // ------ Nuestra Herramienta Para Crear El Token ------
    @Autowired
    JwtUtils jwtUtils;

    /* ###### ENDPOINTS DE AUTENTICACION ###### */

    // ------ Endpoint Para El Inicio De Sesion ------
    @PostMapping("/login")
    public Map<String, String> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        
        // ------ Intentamos Autenticar Al Usuario Con Los Datos Recibidos ------
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.get("username"), 
                        loginRequest.get("password")
                )
        );

        // ------ Si Las Credenciales Son Correctas Lo Guardamos En El Contexto De Seguridad ------
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ------ Generamos El Token Jwt Usando El Nombre Del Usuario Autenticado ------
        String jwt = jwtUtils.generateJwtToken(authentication.getName());

        // ------ Preparamos La Respuesta Con El Token Para El Cliente ------
        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        
        // ------ El Cliente Recibira Un Json Con El Token ------
        return response;
    }

    // ------ Endpoint Para Cerrar Sesion ------
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        // ------ En Jwt El Servidor No Necesita Hacer Nada Especial Para Invalidar El Token ------
        // ------ A Menos Que Usaras Una Blacklist De Tokens ------
        return ResponseEntity.ok("Sesión cerrada con éxito. El token ya no debe ser utilizado.");
    }

    // ------ Endpoint Para Registrar Un Nuevo Usuario ------
    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroRequestDTO registroDTO) {
     
        usuarioService.crearNuevoUsuario(registroDTO);

        // ------ Creamos Un Mapa Para Que La Respuesta Sea Un Json Y El Cliente Lo Procese Bien ------
        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuario registrado con éxito. ¡Ya puedes iniciar sesión!");

        return ResponseEntity.ok(response);
    }

    /* ###### ENDPOINTS DE RECUPERACION DE CONTRASEÑA ###### */

    // ------ Endpoint Para Solicitar El Restablecimiento De Contraseña ------
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody PasswordResetRequestDTO request) {
        usuarioService.generarTokenRecuperacion(request.getEmail());
        Map<String, String> res = new HashMap<>();
        res.put("message", "Si el email está registrado, recibirás un enlace de recuperación en unos minutos.");
        return ResponseEntity.ok(res);
    }

    // ------ Endpoint Para Procesar El Restablecimiento De Contraseña ------
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        // ------ Recibimos Token Y Nueva Contraseña Del Cuerpo De La Peticion ------
        usuarioService.resetearPassword(request.get("token"), request.get("password"));
        Map<String, String> res = new HashMap<>();
        res.put("message", "Contraseña actualizada con éxito.");
        return ResponseEntity.ok(res);
    }

    /* ###### ENDPOINTS DE USUARIO ###### */

    // ------ Endpoint Para Obtener Los Datos Del Perfil Del Usuario Logueado ------
    @GetMapping("/perfil")
    public ResponseEntity<Map<String, Object>> obtenerPerfil(Principal principal) {
        return ResponseEntity.ok(usuarioService.obtenerDatosPerfilCompleto(principal.getName()));
    }
}
