package com.ticketpro.api.service;

import com.ticketpro.api.dto.SesionDTO;
import com.ticketpro.api.exception.RecursoNoEncontrado;
import com.ticketpro.api.model.Sesion;
import com.ticketpro.api.repository.SesionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SesionService {

    @Autowired
    private SesionRepository sesionRepo;

    public List<SesionDTO> obtenerSesionesPorEvento(Long eventoId) {
        List<Sesion> sesiones = sesionRepo.findByEventoId(eventoId);
        
        if (sesiones.isEmpty()) {
            throw new RecursoNoEncontrado("No hay sesiones disponibles para el evento: " + eventoId);
        }

        List<SesionDTO> sesionesDTO = new ArrayList<>();

        for (Sesion s : sesiones) {
            sesionesDTO.add(entityDto(s));
        }

        return sesionesDTO;
    }

    private SesionDTO entityDto(Sesion s) {
        SesionDTO dto = new SesionDTO();
        dto.setId(s.getId());
        dto.setFechaHora(s.getFechaHora());
        dto.setPrecioBase(s.getPrecioBase());
        dto.setEntradasVendidas(s.getEntradasVendidas());
        dto.setEstado(s.getEstado());
        
        // Accedemos a las relaciones para "aplanar" el objeto
        if (s.getSala() != null) {
            dto.setNombreSala(s.getSala().getNombre());
            dto.setCapacidadSala(s.getSala().getCapacidad());
            
            if (s.getSala().getUbicacion() != null) {
                dto.setNombreUbicacion(s.getSala().getUbicacion().getNombre());
                dto.setCiudadUbicacion(s.getSala().getUbicacion().getCiudad());
            }
        }
        return dto;
    }
}
