package com.ticketpro.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/* ###### CONFIGURACION WEB RECURSOS ###### */
// ------ Clase Para Configurar El Manejo De Recursos Dinamicos Y Estaticos ------
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /* ###### METODOS DE CONFIGURACION ###### */

    // ------ Añade Cadenas De Rutas Personalizadas Para Recursos Como Imagenes ------
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // ------ Exponemos El Directorio De Imagenes Al Entorno Externo ------
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:images/");
    }
}
