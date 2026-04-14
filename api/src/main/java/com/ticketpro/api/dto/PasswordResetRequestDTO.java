package com.ticketpro.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ###### DTO DE SOLICITUD DE RESTABLECIMIENTO ###### */
// ------ Objeto Que Recibe El Correo Para Enviar Link De Recuperacion ------
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequestDTO {

    /* ###### ATRIBUTOS ###### */

    // ------ Correo Electronico Al Cual Enviar El Enlace ------
    private String email;
}
