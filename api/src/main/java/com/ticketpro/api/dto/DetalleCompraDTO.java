package com.ticketpro.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleCompraDTO {
private String localizador;
    private String tituloEvento;
    private String categoriaEvento;
    private LocalDateTime fechaSesion;
    private String nombreSala;
    private String ciudad;
    private Integer numEntradas;
    private BigDecimal precioUnitario;
    private BigDecimal totalPagado;
    private String estado;
    private LocalDateTime fechaCompra;
    private List<String> codigosQr;
}
