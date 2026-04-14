package com.ticketpro.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/* ###### DTO DE HISTORIAL DE COMPRA ###### */
// ------ Objeto Dedicado A Mostrar Resumen De Compras En Listados ------
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistorialCompraDTO {
    
    /* ###### ATRIBUTOS ###### */

    // ------ Necesario Para El Boton Ver Detalle En Angular ------
    private Long id; 
    
    // ------ Codigo De Localizador ------
    private String localizador;

    // ------ Nombre Del Evento ------
    private String tituloEvento;

    // ------ Fecha Y Hora De La Sesion Comprada ------
    private LocalDateTime fechaSesion;

    // ------ Cantidad Total De Entradas ------
    private Integer numEntradas;

    // ------ Importe Total Abonado ------
    private BigDecimal totalPagado;

    // ------ Situacion Actual De La Compra ------
    private String estado;
}
