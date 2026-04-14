package com.ticketpro.api.controller;

import com.ticketpro.api.dto.CambioPasswordDTO;
import com.ticketpro.api.dto.UsuarioPerfilDTO;
import com.ticketpro.api.dto.UsuarioUpdateDTO;
import com.ticketpro.api.service.UsuarioService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/* ###### CONTROLADOR DE CLIENTES USUARIOS ###### */
// ------ Proporciona Endpoints Para Manejar El Perfil De Usuario Registrado ------
@RestController
@RequestMapping("/api/clientes")
public class UsuarioController {

    /* ###### DEPENDENCIAS INYECTADAS ###### */

    // ------ Servicio Encaragado De La Logica De Gestion De Usuarios ------
    @Autowired
    private UsuarioService usuarioService;

    /* ###### ENDPOINTS DE GESTION DE PERFIL ###### */

    // ------ Obtiene Los Datos Del Perfil Del Usuario Autenticado ------
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioPerfilDTO> obtenerPerfil(@AuthenticationPrincipal UserDetails userDetails) {
        // ------ Cambiamos El Retorno A UsuarioUpdateDto Para Que Angular Reciba Todos Los Campos ------
        return ResponseEntity.ok(usuarioService.obtenerPerfilCompleto(userDetails.getUsername()));
    }

    // ------ Actualiza Los Datos Del Perfil Del Usuario ------
    @PutMapping("/update")
    public ResponseEntity<UsuarioUpdateDTO> actualizarPerfil(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UsuarioUpdateDTO updateDTO) {

        UsuarioUpdateDTO actualizado = usuarioService.actualizarPerfil(userDetails.getUsername(), updateDTO);
        return ResponseEntity.ok(actualizado);
    }

    // ------ Permite Al Usuario Darse De Baja Del Sistema ------
    @PatchMapping("/baja")
    public ResponseEntity<?> darDeBaja(@AuthenticationPrincipal UserDetails userDetails) {
        usuarioService.desactivarUsuario(userDetails.getUsername());
        return ResponseEntity.ok("Tu cuenta ha sido desactivada correctamente.");
    }

    /* ###### ENDPOINTS ESPECIALES DE CUENTA ###### */

    // ------ Actualiza La Contraseña Del Usuario ------
    @PatchMapping("/password")
    public ResponseEntity<String> cambiarPassword(
            // ------ Recibimos De Principal El Usuario Que Esta Autenticado ------
            @AuthenticationPrincipal UserDetails userDetails, 
            @RequestBody CambioPasswordDTO passwordDTO) {

        usuarioService.actualizarPassword(userDetails.getUsername(), passwordDTO);
        return ResponseEntity.ok("Contraseña actualizada correctamente.");
    }

    // ------ Sube Una Imagen De Perfil Y Retorna Su Url ------
    @PostMapping("/imagen")
    public ResponseEntity<Map<String, String>> subirImagen(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("imagen") MultipartFile imagen) {

        String url = usuarioService.guardarImagenPerfil(userDetails.getUsername(), imagen);

        Map<String, String> response = new HashMap<>();
        response.put("url", url);

        return ResponseEntity.ok(response);
    }

}
