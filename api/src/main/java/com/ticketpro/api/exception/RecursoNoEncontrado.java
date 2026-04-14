package com.ticketpro.api.exception;

/* ###### EXCEPCION DE RECURSO NO ENCONTRADO ###### */
// ------ Emitida Cuando Un Elemento Solicitado No Existe En Base De Datos ------
public class RecursoNoEncontrado extends RuntimeException {

    /* ###### CONSTRUCTOR ###### */

    // ------ Pasa El Mensaje Informando Que Falta El Recurso ------
    public RecursoNoEncontrado(String mensaje) {
        super(mensaje);
    }
}
