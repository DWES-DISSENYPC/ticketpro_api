package com.ticketpro.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ###### DTO DE REGISTRO DE USUARIO ###### */
// ------ Captura Los Datos Enviados Al Registrar Una Nueva Cuenta ------
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroRequestDTO {

    /* ###### ATRIBUTOS ###### */

    // ------ Nombre Del Usuario Para Iniciar Sesion ------
    private String username;

    // ------ Contraseña Puesta Por El Usuario ------
    private String password;

    // ------ Direccion De Correo Electronico ------
    private String email;

    // ------ Nombre De Pila Literal ------
    private String nombre;

    // ------ Documento De Identidad ------
    private String dni;

    // ------ Apellidos Del Usuario ------
    private String apellidos;
}
