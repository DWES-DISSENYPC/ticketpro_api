package com.ticketpro.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ###### DTO DE TICKET INDIVIDUAL ###### */
// ------ Objeto Que Contiene La Informacion De Un Ticket Para Su Visualizacion ------
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {

    // ------ Identificador Del Ticket ------
    private Long id;

    // ------ Codigo Alfanumerico Del Ticket ------
    private String codigo;

    // ------ URL De La Imagen Del Codigo Qr ------
    private String qrCode;

    // ------ Indica Si El Ticket Ya Ha Sido Utilizado ------
    private boolean usado;
}
