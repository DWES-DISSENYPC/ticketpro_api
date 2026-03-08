package com.ticketpro.api.service;

import com.ticketpro.api.dto.SesionDTO;
import com.ticketpro.api.exception.RecursoNoEncontrado;
import com.ticketpro.api.model.Sesion;
import com.ticketpro.api.repository.SesionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SesionService {

    @Autowired
    private SesionRepository sesionRepository;

    @Transactional(readOnly = true)
    public List<SesionDTO> obtenerSesionesPorEvento(Long eventoId) {
        List<Sesion> sesiones = sesionRepository.findByEventoId(eventoId);
        return sesiones.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SesionDTO obtenerSesionPorId(Long id) {
        Sesion sesion = sesionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("No se encontró la sesión con ID: " + id));
        return convertToDTO(sesion);
    }

    /**
     * Mapea manualmente la entidad al DTO aplanado.
     * Esto evita que Angular tenga que conocer la estructura interna de Sala y Ubicacion.
     */
    private SesionDTO convertToDTO(Sesion sesion) {
        SesionDTO dto = new SesionDTO();
        dto.setId(sesion.getId());
        dto.setFechaHora(sesion.getFechaHora());
        dto.setPrecioBase(sesion.getPrecioBase());
        dto.setEntradasVendidas(sesion.getEntradasVendidas());
        dto.setEstado(sesion.getEstado());

        // Extraemos datos de la Sala (navegación segura)
        if (sesion.getSala() != null) {
            dto.setNombreSala(sesion.getSala().getNombre());
            dto.setCapacidadSala(sesion.getSala().getCapacidad());
            
            // Extraemos datos de la Ubicación desde la Sala
            if (sesion.getSala().getUbicacion() != null) {
                dto.setNombreUbicacion(sesion.getSala().getUbicacion().getNombre());
                dto.setCiudadUbicacion(sesion.getSala().getUbicacion().getCiudad());
            }
        }
        return dto;
    }
}