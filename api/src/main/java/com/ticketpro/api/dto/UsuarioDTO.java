package com.ticketpro.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ###### DTO BASICO DE USUARIO ###### */
// ------ Datos Principales A Devolver Sin Exponer Informacion Sensible ------
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    /* ###### ATRIBUTOS ###### */

    // ------ Identificador Unico Intrinseco ------
    private Long id;

    // ------ Alias De Autenticacion ------
    private String username;

    // ------ Nombre Real Del Usuario ------
    private String nombre;

    // ------ Apellidos Reales Del Usuario ------
    private String apellidos;

    // ------ E-mail Principal De Contacto ------
    private String email;
}
