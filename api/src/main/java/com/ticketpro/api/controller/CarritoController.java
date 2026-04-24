package com.ticketpro.api.controller;

import com.ticketpro.api.dto.CarritoAddRequestDTO;
import com.ticketpro.api.dto.CarritoItemDTO;
import com.ticketpro.api.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public ResponseEntity<List<CarritoItemDTO>> obtenerCarrito(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(userDetails.getUsername()));
    }

    @PostMapping("/add")
    public ResponseEntity<String> añadirAlCarrito(@RequestBody CarritoAddRequestDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        carritoService.añadirAlCarrito(userDetails.getUsername(), dto);
        return ResponseEntity.ok("Artículo añadido al carrito");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarDelCarrito(@PathVariable Long id) {
        carritoService.eliminarDelCarrito(id);
        return ResponseEntity.ok("Artículo eliminado del carrito");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> vaciarCarrito(@AuthenticationPrincipal UserDetails userDetails) {
        carritoService.vaciarCarrito(userDetails.getUsername());
        return ResponseEntity.ok("Carrito vaciado");
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> finalizarCompra(@AuthenticationPrincipal UserDetails userDetails) {
        carritoService.finalizarCompra(userDetails.getUsername());
        return ResponseEntity.ok("Compra realizada con éxito desde el carrito");
    }
}
