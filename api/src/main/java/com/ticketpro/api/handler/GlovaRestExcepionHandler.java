package com.ticketpro.api.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ticketpro.api.exception.ConflictoException;
import com.ticketpro.api.exception.RecursoNoEncontrado;

@RestControllerAdvice
public class GlovaRestExcepionHandler {

    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<String> error409(ConflictoException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(RecursoNoEncontrado.class)
    public ResponseEntity<String> error404(RecursoNoEncontrado ex){

        return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ex.getMessage());
    }

}

