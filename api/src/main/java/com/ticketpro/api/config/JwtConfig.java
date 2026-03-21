package com.ticketpro.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "ticketpro.app")
@Data
public class JwtConfig {
    private String jwtSecret;
    private Long jwtExpirationMs;

}
