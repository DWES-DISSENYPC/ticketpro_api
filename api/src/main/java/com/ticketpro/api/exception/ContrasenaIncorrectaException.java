package com.ticketpro.api.exception;

public class ContrasenaIncorrectaException extends RuntimeException {
    public ContrasenaIncorrectaException(String mensaje) {
        super(mensaje);
    }
}

