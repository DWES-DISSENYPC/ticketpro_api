package com.ticketpro.api.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SesionDTO {
    private Long id;
    private LocalDateTime fechaHora;
    private BigDecimal precioBase;
    private Integer entradasVendidas;
    private Integer estado;
    
    // Datos aplanados para que Angular no tenga que navegar por objetos anidados
    private String nombreSala;
    private Integer capacidadSala;
    private String nombreUbicacion;
    private String ciudadUbicacion;
}
