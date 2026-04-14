package com.ticketpro.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ###### DTO DE TELEFONO ###### */
// ------ Estructura De Transferencia Para Numeros Telefonicos ------
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelefonoDTO {

    /* ###### ATRIBUTOS ###### */

    // ------ Los Digitos Del Numero De Telefono ------
    private String numero;

    // ------ Movil Fijo Trabajo U Otro ------
    private String tipo; 
}
