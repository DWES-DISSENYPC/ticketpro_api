package com.ticketpro.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ticketpro.api")
public class ApiApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
