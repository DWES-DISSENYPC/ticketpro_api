package com.ticketpro.api.exception;

/* ###### EXCEPCION DE CONTRASEÑA INCORRECTA ###### */
// ------ Error Activado Cuando La Clave Proporcionada No Coincide ------
public class ContrasenaIncorrectaException extends RuntimeException {

    /* ###### CONSTRUCTOR ###### */

    // ------ Establece El Mensaje Explicativo Del Error ------
    public ContrasenaIncorrectaException(String mensaje) {
        super(mensaje);
    }
}
