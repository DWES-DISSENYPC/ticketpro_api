package com.ticketpro.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ###### DTO DE DETALLE DE COMPRA ###### */
// ------ Objeto Que Contiene Toda La Informacion De Una Compra Especifica ------
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleCompraDTO {

    /* ###### ATRIBUTOS ###### */
    private Long id;

    // ------ Localizador Unico De La Compra ------
    private String localizador;

    // ------ Titulo Del Evento De La Compra ------
    private String tituloEvento;

    // ------ Categoria Del Evento De La Compra ------
    private String categoriaEvento;

    // ------ Fecha Y Hora De La Sesion ------
    private LocalDateTime fechaSesion;

    // ------ Nombre De La Sala Del Evento ------
    private String nombreSala;

    // ------ Ciudad Donde Se Realiza El Evento ------
    private String ciudad;

    // ------ Numero De Entradas Adquiridas ------
    private Integer numEntradas;

    // ------ Precio Individual De Cada Entrada ------
    private BigDecimal precioUnitario;

    // ------ Total Pagado Por La Compra ------
    private BigDecimal totalPagado;

    // ------ Estado Actual Del Pago Y Compra ------
    private String estado;

    // ------ Fecha En La Que Se Realizo La Compra ------
    private LocalDateTime fechaCompra;

    // ------ Listado De Tickets Individuales De La Compra ------
    private List<TicketDTO> tickets;
}
