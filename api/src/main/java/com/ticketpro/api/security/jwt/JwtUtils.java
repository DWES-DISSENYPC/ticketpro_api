package com.ticketpro.api.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // 1. Traemos la clave secreta desde el application.properties
    @Value("${ticketpro.app.jwtSecret}")
    private String jwtSecret;

    // 2. Traemos el tiempo de duración (ej: 24 horas)
    @Value("${ticketpro.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // Generar el token cuando el usuario se loguea con éxito
    public String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Metemos el nombre de usuario en el token
                .setIssuedAt(new Date()) // Fecha de creación
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Fecha de caducidad
                .signWith(key(), SignatureAlgorithm.HS256) // Firma digital para que no lo falsifiquen
                .compact();
    }

    // Convertir el String secreto del properties en una llave real
    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Obtener el nombre de usuario de dentro de un token
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
               .parseClaimsJws(token).getBody().getSubject();
    }

    // Verificar si el token es válido o si alguien lo ha manipulado
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
