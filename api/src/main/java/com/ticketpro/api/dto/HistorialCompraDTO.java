package com.ticketpro.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistorialCompraDTO {
    
    // Identificador único de la reserva para el cliente (ej: 7A2B9C)
    private String localizador;
    
    // Datos del espectáculo
    private String tituloEvento;
    private String categoriaEvento;
    
    // Datos de cuándo y dónde
    private LocalDateTime fechaSesion;
    private String nombreSala;
    private String ciudad;
    
    // Datos económicos y de cantidad
    private Integer numEntradas;
    private BigDecimal precioUnitario;
    private BigDecimal totalPagado;
    
    // Estado (PAGADA, CANCELADA, etc.)
    private String estado;
}
