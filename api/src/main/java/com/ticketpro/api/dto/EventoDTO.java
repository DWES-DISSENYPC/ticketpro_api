package com.ticketpro.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String imagenUrl;
    private Integer duracionMinutos;
    private String estado;
    // Omitimos la descripción larga para la lista general si queremos ahorrar ancho de banda
}
