package com.ticketpro.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ticketpro.api.dto.EventoDTO;
import com.ticketpro.api.service.EventoService;

/* ###### CONTROLADOR DE EVENTOS ###### */
// ------ Expone Los Endpoints Relacionados Con La Gestion De Eventos ------
@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    /* ###### DEPENDENCIAS INYECTADAS ###### */

    // ------ Servicio Para Gestionar La Logica De Eventos ------
    @Autowired
    private EventoService eventoService;

    /* ###### ENDPOINTS DE LECTURA DE EVENTOS ###### */

    // ------ Obtener Todos Los Eventos Para Mostrar En La Cartelera ------
    @GetMapping
    public List<EventoDTO> listarTodo() {
        return eventoService.listarTodos();
    }

    // ------ Buscar Por Id Para Ver Los Detalles De Un Evento Concreto ------
    @GetMapping("/{id}")
    public ResponseEntity<EventoDTO> obtenerPorId(@PathVariable Long id) {
        EventoDTO eventoDTO = eventoService.eventoPorId(id);

        // ------ Status 200 Ok Con El Evento Dto En El Cuerpo ------
        return ResponseEntity.ok(eventoDTO);
    }

    // ------ Obtener Un Numero Determinado De Eventos Aleatorios ------
    @GetMapping("/aleatorios")
    public List<EventoDTO> obtenerEventosAleatorios(@RequestParam(defaultValue = "8") Integer cantidad) {
        return eventoService.obtenerEventosAleatorios(cantidad);
    }

    // ------ Nuevo Endpoint Obtener Todas Las Categorias Unicas Disponibles ------
    @GetMapping("/categorias")
    public List<String> obtenerCategorias() {
        return eventoService.obtenerCategoriasUnicas();
    }

    // ------ Nuevo Endpoint Buscar Eventos Por Titulo O Ciudad ------
    @GetMapping("/buscar")
    public List<EventoDTO> buscarEventos(@RequestParam String termino) {
        return eventoService.buscarEventos(termino);
    }
}
