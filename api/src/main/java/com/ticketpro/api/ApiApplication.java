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

@SpringBootApplication(scanBasePackages = "com.ticketpro.api")
public class ApiApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Bean
public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    // Esto es vital para que no falle al leer las fechas de Ticketmaster
    mapper.registerModule(new JavaTimeModule()); 
    return mapper;
}

@Bean
public WebClient.Builder webClientBuilder() {
    // Definimos un límite de 10 MB (10 * 1024 * 1024)
    final int size = 10 * 1024 * 1024;
    final ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
            .build();

    return WebClient.builder()
            .exchangeStrategies(strategies);
}

// 	@Bean
// CommandLineRunner init(TicketmasterImportService ticketmasterService) {
//     return args -> {
//         System.out.println("--- Iniciando importación de Ticketmaster ---");
//         ticketmasterService.importarEventosEspana();
//         System.out.println("--- Importación finalizada ---");
//     };
// }

}
