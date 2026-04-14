package com.ticketpro.api.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

/* ###### UTILIDADES DE MANEJO JWT ###### */
// ------ Contiene Los Metodos Para Crear Procesar Y Validar Nuestros Tokens ------
@Component
public class JwtUtils {

    /* ###### PROPIEDADES DE CONFIGURACION ###### */

    // ------ 1. Traemos La Clave Secreta Desde El Application.properties ------
    @Value("${ticketpro.app.jwtSecret}")
    private String jwtSecret;

    // ------ 2. Traemos El Tiempo De Duracion (Ej: 24 Horas) ------
    @Value("${ticketpro.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /* ###### METODOS DE GENERACION ###### */

    // ------ Generar El Token Cuando El Usuario Se Loguea Con Exito ------
    public String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username) // ------ Metemos El Nombre De Usuario En El Token ------
                .setIssuedAt(new Date()) // ------ Fecha De Creacion ------
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // ------ Fecha De Caducidad ------
                .signWith(key(), SignatureAlgorithm.HS256) // ------ Firma Digital Para Que No Lo Falsifiquen ------
                .compact();
    }

    /* ###### METODOS CRIPTOGRAFICOS ###### */

    // ------ Convertir El String Secreto Del Properties En Una Llave Real ------
    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /* ###### METODOS DE EXTRACCION ###### */

    // ------ Obtener El Nombre De Usuario De Dentro De Un Token Recuperado ------
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
               .parseClaimsJws(token).getBody().getSubject();
    }

    /* ###### METODOS DE VALIDACION ###### */

    // ------ Verificar Si El Token Es Valido O Si Alguien Lo Ha Manipulado O Caducado ------
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("Token inválido");
        } catch (ExpiredJwtException e) {
            System.out.println("El token ha caducado");
        }
        return false;
    }
}
