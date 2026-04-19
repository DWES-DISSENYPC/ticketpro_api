package com.ticketpro.api.config;

import com.ticketpro.api.security.jwt.AuthTokenFilter;
import com.ticketpro.api.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/* ###### CONFIGURACION DE SEGURIDAD ###### */
// ------ Clase Principal Que Configura La Seguridad Integral De La Api ------
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /* ###### DEPENDENCIAS INYECTADAS ###### */

    // ------ Servicio Para Obtener Detalles De Usuario ------
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // ------ Filtro Que Verifica El Token Jwt En Cada Peticion ------
    @Autowired
    private AuthTokenFilter authTokenFilter; // ------ Ahora Si Se Inyecta ------

    /* ###### BEANS DE SEGURIDAD ###### */

    // ------ Proveedor De Autenticacion Que Usa El Servicio De Usuario Y Codificador ------
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // Mantenemos la sintaxis original tal cual estaba
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // ------ Gestor De Autenticacion Del Sistema ------
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // ------ Codificador De Contraseñas Con Bcrypt ------
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ------ Cadena De Filtros De Seguridad Principal ------
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ------ Configura Politicas De Cors ------
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // ------ Desactiva Csrf Ya Que Usamos Tokens ------
                .csrf(csrf -> csrf.disable())
                // ------ Define La Sesion Como Sin Estado Stateless ------
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // ------ Define Permisos De Rutas Http ------
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers("/error").permitAll()
                    .requestMatchers("/images/**").permitAll()
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/eventos/**").permitAll()
                    .requestMatchers("/api/sesiones/**").permitAll()
                    .requestMatchers("/api/compras/ticket/*/qr").permitAll()
                    .requestMatchers("/api/clientes/perfil").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/api/clientes/update").authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/clientes/imagen").authenticated()
                    .requestMatchers(HttpMethod.PATCH, "/api/clientes/password").authenticated()  // ------ Añade Esto ------
                    .anyRequest().authenticated());

        // ------ Añade Proveedor De Autenticacion ------
        http.authenticationProvider(authenticationProvider());
        // ------ Añade Filtro Jwt Antes Del Filtro Por Defecto ------
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /* ###### CONFIGURACION CORS ###### */

    // ------ Fuente De Configuracion De Cors ------
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        
        // ------ Permite Origen Del Frontend De Angular ------
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        // ------ Permite Metodos Especificos ------
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        // ------ Permite Todos Los Encabezados ------
        config.setAllowedHeaders(List.of("*"));
        // ------ Expone Encabezado De Autorizacion ------
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // ------ Aplica Configuracion A Todas Las Rutas ------
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
