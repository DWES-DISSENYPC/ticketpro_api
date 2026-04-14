package com.ticketpro.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ###### DTO DE CAMBIO DE CONTRASEÑA ###### */
// ------ Objeto De Transferencia Para Cambiar La Contraseña Del Usuario ------
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambioPasswordDTO {

    /* ###### ATRIBUTOS ###### */

    // ------ Contraseña Actual Del Usuario ------
    private String passwordActual;

    // ------ Nueva Contraseña Deseada ------
    private String passwordNueva;
}
