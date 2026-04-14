package com.ticketpro.api.exception;

/* ###### EXCEPCION DE ACCESO DENEGADO ###### */
// ------ Excepcion Lanzada Cuando El Usuario No Tiene Permisos Suficientes ------
public class AccesoDenegadoException extends RuntimeException {

    /* ###### CONSTRUCTOR ###### */
    
    // ------ Crea La Excepcion Con Un Mensaje Descriptivo ------
    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
