package com.ticketpro.api.security.jwt;

import com.ticketpro.api.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Este filtro se ejecuta EXACTAMENTE una vez por cada petición que llega a la API
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

     public AuthTokenFilter() {
        System.out.println(">>> CONSTRUCTOR DEL FILTRO JWT CREADO");
    }

    @Autowired
    private JwtUtils jwtUtils; // Nuestra clase que valida el token

    @Autowired
    private UserDetailsServiceImpl userDetailsService; // Nuestra clase que busca en la BD

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
           throws ServletException, IOException {

 System.out.println(">>> FILTRO JWT EJECUTADO: " + request.getRequestURI());

             System.out.println(">>> URI: " + request.getRequestURI());
    System.out.println(">>> Authorization header: " + request.getHeader("Authorization"));
    System.out.println(">>> Content-Type: " + request.getHeader("Content-Type"));

        try {
            // 1. Extraemos el token del encabezado "Authorization"
            String jwt = parseJwt(request);

            // 2. Si el token existe y es válido (no ha caducado ni ha sido alterado)
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // 3. Cargamos el usuario de la base de datos
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // 4. Creamos una "tarjeta de identificación" (Authentication) de Spring
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 5. Metemos esa tarjeta en el "Contexto", así la API sabe que este usuario tiene permiso
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("No se puede establecer la autenticación del usuario: {}", e);
        }

        // 6. Dejamos que la petición siga su camino hacia el controlador
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para limpiar el prefijo "Bearer " del token
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
