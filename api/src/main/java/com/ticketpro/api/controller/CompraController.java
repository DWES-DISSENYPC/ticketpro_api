package com.ticketpro.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticketpro.api.dto.CompraDTO;
import com.ticketpro.api.dto.HistorialCompraDTO;
import com.ticketpro.api.service.CompraService;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    @Autowired private CompraService compraService;

    @PostMapping
    public ResponseEntity<String> realizarCompra(
            @RequestBody CompraDTO compraDTO, 
            @AuthenticationPrincipal UserDetails userDetails) {
        
        // El userDetails contiene el username (email en tu caso) del token JWT
        String username = userDetails.getUsername();
        
        // Llamamos al servicio pasando el email en lugar del ID
        compraService.procesarCompra(compraDTO, username);
        
        return ResponseEntity.ok("Compra realizada con éxito. ¡Entradas reservadas!");
    }

    // 2. Endpoint para que Pitufo vea sus entradas en su perfil
    @GetMapping("/mis-compras")
    public ResponseEntity<List<HistorialCompraDTO>> listarMisCompras(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        String username = userDetails.getUsername();
        List<HistorialCompraDTO> historial = compraService.obtenerHistorial(username);
        
        return ResponseEntity.ok(historial);
    }
}
