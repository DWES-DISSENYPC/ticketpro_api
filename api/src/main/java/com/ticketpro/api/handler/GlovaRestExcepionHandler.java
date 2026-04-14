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

/* ###### MANEJADOR GLOBAL DE EXCEPCIONES ###### */
// ------ Intercepta Errores En Todo El Backend Para Responder Con Codigos Http Adecuados ------
@RestControllerAdvice
public class GlovaRestExcepionHandler {

    /* ###### MANEJADORES DE ERRORES DE LOGICA ###### */

    // ------ Resuelve Un Error 409 Conflict ------
    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<String> error409(ConflictoException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    // ------ Resuelve Un Error 404 Not Found ------
    @ExceptionHandler(RecursoNoEncontrado.class)
    public ResponseEntity<String> error404(RecursoNoEncontrado ex){

        return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ex.getMessage());
    }

    /* ###### MANEJADORES DE ERRORES DE SEGURIDAD ###### */

    // ------ Resuelve Un Error 403 Forbidden Ante Falta De Privilegios ------
    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<String> error403(AccesoDenegadoException ex){

        return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(ex.getMessage());
    }

    // ------ Capturamos La Excepcion De Contraseña Incorrecta Y Fallos De Login De Spring ------
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> manejarErroresLogin(Exception ex) {
        // ------ Devolvemos Un Mensaje Ambiguo Por Razones De Seguridad ------
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Los datos de acceso son incorrectos. Ponte en contacto con el soporte técnico si crees que esto es un error.");
    }

    // ------ Maneja La Variante Interna Personalizada De Contraseña Equivalente Al Status 400 O 401 ------
    @ExceptionHandler(ContrasenaIncorrectaException.class)
    public ResponseEntity<Map<String, String>> handleContrasenaIncorrecta(ContrasenaIncorrectaException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        // ------ O HttpStatus.UNAUTHORIZED Si Prefieres 401 ------
    }
}
