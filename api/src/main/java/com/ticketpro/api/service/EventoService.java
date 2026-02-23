package com.ticketpro.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketpro.api.dto.EventoDTO;
import com.ticketpro.api.exception.RecursoNoEncontrado;
import com.ticketpro.api.model.Evento;
import com.ticketpro.api.repository.EventoRepository;

@Service
public class EventoService {


     @Autowired
    private EventoRepository eventoRepository;


    public List<EventoDTO> listarTodos() {
        List<Evento> eventos = eventoRepository.findAll();
        List<EventoDTO> eventoDTOs = new ArrayList<>();
        for (Evento e : eventos) {
            eventoDTOs.add(entityToDto(e));
        }
        return eventoDTOs;
    }

 public EventoDTO eventoPorId(Long id) {
        Optional<Evento> eventoOpt = eventoRepository.findById(id);
        if (eventoOpt.isPresent()) {
            return entityToDto(eventoOpt.get());
        } else {
            throw new RecursoNoEncontrado("Evento no encontrado con ID: " + id); }
    }

    private EventoDTO entityToDto(Evento e) {
    
        return new EventoDTO(
                e.getId(),
                e.getTitulo(),
                e.getDescripcion(),
                e.getCategoria(),
                e.getImagenUrl(),
                e.getDuracionMinutos(),
                e.getEstado()
        );
    }


   
}
