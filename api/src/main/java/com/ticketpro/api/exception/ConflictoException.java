package com.ticketpro.api.exception;

/* ###### EXCEPCION DE CONFLICTO ###### */
// ------ Lanzada Ante Situaciones De Conflicto De Estado O Datos ------
public class ConflictoException extends RuntimeException {

    /* ###### CONSTRUCTOR ###### */

    // ------ Inicializa El Error Con El Mensaje De Conflicto ------
    public ConflictoException(String mensaje) {
        super(mensaje);
    }
}
