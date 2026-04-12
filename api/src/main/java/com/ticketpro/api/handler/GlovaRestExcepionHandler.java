package com.ticketpro.api.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ticketpro.api.exception.AccesoDenegadoException;
import com.ticketpro.api.exception.ConflictoException;
import com.ticketpro.api.exception.ContrasenaIncorrectaException;
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

    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<String> error403(AccesoDenegadoException ex){

        return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(ex.getMessage());
    }

  // Capturamos tanto tu excepción personalizada como la de contraseña incorrecta de Spring
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> manejarErroresLogin(Exception ex) {
        // Devolvemos el mensaje ambiguo que querías
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Los datos de acceso son incorrectos. Ponte en contacto con el soporte técnico si crees que esto es un error.");
    }

    @ExceptionHandler(ContrasenaIncorrectaException.class)
    public ResponseEntity<Map<String, String>> handleContrasenaIncorrecta(ContrasenaIncorrectaException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        // o HttpStatus.UNAUTHORIZED si prefieres 401
    }


}

