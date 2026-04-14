package com.ticketpro.api.config;

import com.ticketpro.api.model.Evento;
import com.ticketpro.api.model.Usuario;
import com.ticketpro.api.repository.EventoRepository;
import com.ticketpro.api.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/* ###### CONFIGURACION DE INICIALIZACION DE DATOS ###### */
// ------ Clase Que Inicializa Datos De Prueba Al Arrancar La Aplicacion ------
@Configuration
public class DataInitializer {

    /* ###### DEPENDENCIAS INYECTADAS ###### */

    // ------ Inyectamos El Codificador De Contraseñas Definido En WebSecurityConfig ------
    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    /* ###### BEANS DE CONFIGURACION ###### */

    // ------ Metodo Que Se Ejecuta Al Iniciar Para Poblar La Base De Datos ------
    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepo, EventoRepository eventoRepo) {
        return args -> {
            /* ###### INICIALIZACION DE USUARIOS ###### */
            if (usuarioRepo.count() == 0) {
                // ------ Creacion Del Usuario Administrador ------
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                // ------ Usamos PasswordEncoder Para Proteger La Clave ------
                admin.setPassword(passwordEncoder.encode("1234")); 
                admin.setEmail("admin@ticketpro.com");
                admin.setNombre("Admin");
                admin.setApellidos("TicketPro");
                admin.setDni("12345678A");
                usuarioRepo.save(admin);
                
                // ------ Creacion Del Usuario Pitufo De Prueba ------
                Usuario pitufo = new Usuario();
                pitufo.setUsername("pitufo");
                // ------ Lo Mismo Para Tu Hermano Pitufo ------
                pitufo.setPassword(passwordEncoder.encode("pitufo123"));
                pitufo.setEmail("pitufo@mail.com");
                pitufo.setNombre("Pitufo");
                pitufo.setApellidos("Brother");
                pitufo.setDni("87654321B");
                usuarioRepo.save(pitufo);
                
                // ------ Mensaje De Confirmacion En Consola ------
                System.out.println(">> Usuarios iniciales creados con BCrypt.");
            }

            /* ###### INICIALIZACION DE EVENTOS ###### */
            if (eventoRepo.count() == 0) {
                // ------ Creacion De Un Evento Por Defecto ------
                Evento cine = new Evento();
                cine.setTitulo("Batman: El Caballero Oscuro");
                cine.setCategoria("Cine");
                cine.setDescripcion("Proyección especial del clásico de Nolan.");
                cine.setDuracionMinutos(152);
                eventoRepo.save(cine);
                
                // ------ Mensaje De Confirmacion En Consola ------
                System.out.println(">> Eventos de prueba creados.");
            }
        };
    }
}
