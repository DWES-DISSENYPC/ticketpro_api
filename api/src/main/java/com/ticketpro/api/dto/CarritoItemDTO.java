package com.ticketpro.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoItemDTO {
    private Long id;
    private Long sesionId;
    private String eventoTitulo;
    private String imagenUrl;
    private LocalDateTime fechaHora;
    private String nombreSala;
    private String nombreUbicacion;
    private BigDecimal precioBase;
    private Integer cantidad;
    private BigDecimal subtotal;
}
