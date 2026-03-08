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
    
    private Long id; // Necesario para el botón "Ver Detalle" en Angular
    
    private String localizador;
    private String tituloEvento;
    private LocalDateTime fechaSesion;
    private Integer numEntradas;
    private BigDecimal totalPagado;
    private String estado;
}
