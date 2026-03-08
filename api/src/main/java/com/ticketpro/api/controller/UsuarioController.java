package com.ticketpro.api.controller;

import com.ticketpro.api.dto.CambioPasswordDTO;
import com.ticketpro.api.dto.UsuarioUpdateDTO;
import com.ticketpro.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioUpdateDTO> obtenerPerfil(@AuthenticationPrincipal UserDetails userDetails) {
        // Cambiamos el retorno a UsuarioUpdateDTO para que Angular reciba todos los
        // campos
        return ResponseEntity.ok(usuarioService.obtenerPerfilCompleto(userDetails.getUsername()));
    }

    @PutMapping("/update")
    public ResponseEntity<UsuarioUpdateDTO> actualizarPerfil(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UsuarioUpdateDTO updateDTO) {

        UsuarioUpdateDTO actualizado = usuarioService.actualizarPerfil(userDetails.getUsername(), updateDTO);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/baja")
    public ResponseEntity<?> darDeBaja(@AuthenticationPrincipal UserDetails userDetails) {
        usuarioService.desactivarUsuario(userDetails.getUsername());
        return ResponseEntity.ok("Tu cuenta ha sido desactivada correctamente.");
    }

    @PatchMapping("/password")
    public ResponseEntity<String> cambiarPassword(
            @AuthenticationPrincipal UserDetails userDetails, // REcibimos de Principal el usuario que está autenticado
            @RequestBody CambioPasswordDTO passwordDTO) {

        usuarioService.actualizarPassword(userDetails.getUsername(), passwordDTO);
        return ResponseEntity.ok("Contraseña actualizada correctamente.");
    }
}
