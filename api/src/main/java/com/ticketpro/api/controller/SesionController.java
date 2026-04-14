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

/* ###### CONTROLADOR DE SESIONES ###### */
// ------ Administra Y Expone Las Rutas Relacionadas Con Las Sesiones De Eventos ------
@RestController
@RequestMapping("/api/sesiones")
public class SesionController {

    /* ###### DEPENDENCIAS INYECTADAS ###### */

    // ------ Servicio Con La Logica Empresarial De Sesiones ------
    @Autowired
    private SesionService sesionService;

    /* ###### ENDPOINTS DE SESIONES ###### */

    // ------ Escucha Api Sesiones Evento Id Para Que Quede Claro Que El Id Es Del Evento ------
    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<SesionDTO>> listarPorEvento(@PathVariable Long eventoId) {
        List<SesionDTO> sesiones = sesionService.obtenerSesionesPorEvento(eventoId);
        return ResponseEntity.ok(sesiones);
    }
    
    // ------ Detalle De Una Sesion Especifica Para El Formulario De Compra ------
    @GetMapping("/{id}")
    public ResponseEntity<SesionDTO> obtenerDetalle(@PathVariable Long id) {
        SesionDTO sesion = sesionService.obtenerSesionPorId(id);
        return ResponseEntity.ok(sesion);
    }
}
