package com.ticketpro.api.config;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// Asegúrate de que no haya ningún import que empiece por 'com.sun...' o similar para esta clase
import com.ticketpro.api.security.jwt.AuthTokenFilter;
import com.ticketpro.api.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService; // El que busca en MySQL

    // 1. Definimos el "Portero" (Filtro de Token) como un Bean
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    // 2. Configuramos el motor que validará usuario y contraseña
   @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // Pasamos directamente el userDetailsService al constructor
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        
        // Configuramos el codificador de contraseñas
        authProvider.setPasswordEncoder(passwordEncoder());
        
               
        return authProvider;
    }

    // 3. El mánager de autenticación (necesario para el login)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 4. El codificador de contraseñas (para no guardar "1234" en texto plano)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactivamos CSRF (estándar en APIs REST)
            
            // 5. ¡CLAVE JWT!: Decimos que no guarde sesiones en el servidor.
            // Cada petición debe venir con su propio Token.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()    // Rutas para login/registro libres
                .requestMatchers("/api/eventos/**").permitAll() // Ver eventos es libre
                .requestMatchers("/api/sesiones/**").permitAll()    // Vers sesines es libre
                .anyRequest().authenticated()                  // El resto, con Token
            );

        http.authenticationProvider(authenticationProvider());

        // 6. Añadimos nuestro filtro JWT antes del filtro de usuario/contraseña de Spring
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
