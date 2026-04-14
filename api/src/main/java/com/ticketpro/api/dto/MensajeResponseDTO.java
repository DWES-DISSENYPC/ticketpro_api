package com.ticketpro.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/* ###### DTO DE RESPUESTA DE MENSAJE ###### */
// ------ Simple Objeto Para Devolver Respuestas De Texto Formateadas En Json ------
@Data
@AllArgsConstructor
public class MensajeResponseDTO {

    /* ###### ATRIBUTOS ###### */

    // ------ El Texto Del Mensaje A Devolver Al Cliente ------
    private String mensaje;
}
