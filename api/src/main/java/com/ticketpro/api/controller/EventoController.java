package com.ticketpro.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticketpro.api.dto.EventoDTO;
import com.ticketpro.api.service.EventoService;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    // Obtener todos los eventos para la cartelera
    @GetMapping
    public List<EventoDTO> listarTodo() {
        return eventoService.listarTodos();
    }

    // Buscar por ID para ver los detalles de un evento concreto
    @GetMapping("/{id}")
    public ResponseEntity<EventoDTO> obtenerPorId(@PathVariable Long id) {
        EventoDTO eventoDTO = eventoService.eventoPorId(id);
        
        return ResponseEntity.ok(eventoDTO); // Status 200 OK con el evento DTO
    }
}
