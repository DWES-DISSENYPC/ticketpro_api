package com.ticketpro.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticketpro.api.dto.CompraEntradasDTO;
import com.ticketpro.api.dto.DetalleCompraDTO;
import com.ticketpro.api.dto.HistorialCompraDTO;
import com.ticketpro.api.dto.MensajeResponseDTO;
import com.ticketpro.api.service.CompraService;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @PostMapping
    public ResponseEntity<String> realizarCompra(
            @RequestBody CompraEntradasDTO compraDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        // El userDetails contiene el username (email en tu caso) del token JWT
        String username = userDetails.getUsername();

        // Llamamos al servicio pasando el email en lugar del ID
        compraService.realizarCompra(username, compraDTO);

        return ResponseEntity.ok("Compra realizada con éxito. ¡Entradas reservadas!");
    }

    // Endpoint para la tabla general
    @GetMapping("/mis-compras")
    public ResponseEntity<List<HistorialCompraDTO>> listarMisCompras(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(compraService.obtenerHistorial(userDetails.getUsername()));
    }

    // Endpoint para el botón "Ver Detalle"
    @GetMapping("/{id}")
    public ResponseEntity<DetalleCompraDTO> verDetalle(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(compraService.obtenerDetalle(id, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<MensajeResponseDTO> cancelar(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        compraService.cancelarCompra(id, userDetails.getUsername());
        return ResponseEntity
                .ok(new MensajeResponseDTO("Compra cancelada correctamente. El importe será devuelto a su tarjeta."));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<DetalleCompraDTO>> listarPendientes(@RequestParam Long usuarioId) {
        List<DetalleCompraDTO> pendientes = compraService.obtenerComprasPendientes(usuarioId);
        
        if (pendientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(pendientes);
    }
}
