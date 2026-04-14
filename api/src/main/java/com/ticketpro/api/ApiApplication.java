package com.ticketpro.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ticketpro.api.service.TicketmasterImportService;

import org.springframework.web.reactive.function.client.ExchangeStrategies;

/* ###### CLASE PRINCIPAL DE LA APLICACION ###### */
// ------ Punto De Entrada De Spring Boot Y Configuracion De Beans Globales ------
@SpringBootApplication(scanBasePackages = "com.ticketpro.api")
public class ApiApplication {

    /* ###### METODO MAIN ###### */

    // ------ Lanza El Contexto De Aplicacion De Spring Boot ------
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    /* ###### BEANS DE CONFIGURACION GLOBAL ###### */

    // ------ Configura El Mapeador De Objetos Json ------
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // ------ Esto Es Vital Para Que No Falle Al Leer Las Fechas De Ticketmaster ------
        mapper.registerModule(new JavaTimeModule()); 
        
        return mapper;
    }

    // ------ Configura El Cliente Web Reactivo ------
    @Bean
    public WebClient.Builder webClientBuilder() {
        
        // ------ Definimos Un Limite De 10 Mb En Memoria ------
        final int size = 10 * 1024 * 1024;
        
        // ------ Aplicamos La Estrategia De Tamaño Máximo ------
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();

        return WebClient.builder()
                .exchangeStrategies(strategies);
    }

}
