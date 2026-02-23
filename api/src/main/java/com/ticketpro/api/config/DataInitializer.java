package com.ticketpro.api.config;

import com.ticketpro.api.model.Evento;
import com.ticketpro.api.model.Usuario;
import com.ticketpro.api.repository.EventoRepository;
import com.ticketpro.api.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    // 1. Inyectamos el codificador que definimos en WebSecurityConfig !!
    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepo, EventoRepository eventoRepo) {
        return args -> {
            if (usuarioRepo.count() == 0) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                // 2. Usamos passwordEncoder para proteger la clave !!
                admin.setPassword(passwordEncoder.encode("1234")); 
                admin.setEmail("admin@ticketpro.com");
                admin.setNombre("Admin");
                admin.setApellidos("TicketPro");
                admin.setDni("12345678A");
                usuarioRepo.save(admin);
                
                Usuario pitufo = new Usuario();
                pitufo.setUsername("pitufo");
                // 3. Lo mismo para tu hermano Pitufo !!
                pitufo.setPassword(passwordEncoder.encode("pitufo123"));
                pitufo.setEmail("pitufo@mail.com");
                pitufo.setNombre("Pitufo");
                pitufo.setApellidos("Brother");
                pitufo.setDni("87654321B");
                usuarioRepo.save(pitufo);
                
                System.out.println(">> Usuarios iniciales creados con BCrypt.");
            }

            if (eventoRepo.count() == 0) {
                Evento cine = new Evento();
                cine.setTitulo("Batman: El Caballero Oscuro");
                cine.setCategoria("Cine");
                cine.setDescripcion("Proyección especial del clásico de Nolan.");
                cine.setDuracionMinutos(152);
                eventoRepo.save(cine);
                
                System.out.println(">> Eventos de prueba creados.");
            }
        };
    }
}
