package com.ticketpro.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/* ###### CONFIGURACION DE JWT ###### */
// ------ Clase Que Mapea Las Propiedades De Jwt Desde El Archivo Properties ------
@Configuration
@ConfigurationProperties(prefix = "ticketpro.app")
@Data
public class JwtConfig {

    /* ###### ATRIBUTOS ###### */

    // ------ Clave Secreta Para Firmar Los Tokens ------
    private String jwtSecret;

    // ------ Tiempo De Expiracion Del Token En Milisegundos ------
    private Long jwtExpirationMs;
}
