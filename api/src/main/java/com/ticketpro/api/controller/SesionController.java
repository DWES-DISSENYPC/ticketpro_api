package com.ticketpro.api.controller;

import com.ticketpro.api.dto.SesionDTO;
import com.ticketpro.api.service.SesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sesiones")
public class SesionController {

    @Autowired
    private SesionService sesionService;

    // Escucha /api/sesiones/evento/{id} para que quede claro que el ID es del evento
    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<SesionDTO>> listarPorEvento(@PathVariable Long eventoId) {
        List<SesionDTO> sesiones = sesionService.obtenerSesionesPorEvento(eventoId);
        return ResponseEntity.ok(sesiones);
    }
    
    // GET /api/sesiones/{id} - Detalle de una sesión específica para el formulario de compra
    @GetMapping("/{id}")
    public ResponseEntity<SesionDTO> obtenerDetalle(@PathVariable Long id) {
        SesionDTO sesion = sesionService.obtenerSesionPorId(id);
        return ResponseEntity.ok(sesion);
    }
}
