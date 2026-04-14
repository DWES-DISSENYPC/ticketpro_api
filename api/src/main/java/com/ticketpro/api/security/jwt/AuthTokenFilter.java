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

/* ###### FILTRO DE AUTENTICACION JWT ###### */
// ------ Este Filtro Se Ejecuta Exactamente Una Vez Por Cada Peticion Que Llega A La Api ------
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    /* ###### DEPENDENCIAS Y CONSTRUCTOR ###### */

    // ------ Imprime Registro Al Instanciarse Para Verificacion ------
    public AuthTokenFilter() {
        System.out.println(">>> CONSTRUCTOR DEL FILTRO JWT CREADO");
    }

    // ------ Nuestra Clase Que Valida El Token Formado ------
    @Autowired
    private JwtUtils jwtUtils; 

    // ------ Nuestra Clase Que Busca Y Valida Al Usuario En La Bd ------
    @Autowired
    private UserDetailsServiceImpl userDetailsService; 

    /* ###### LOGICA INTERNA DEL FILTRO ###### */

    // ------ Captura La Peticion Extrae Autorizacion Y Verifica Identidad ------
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
           throws ServletException, IOException {

        System.out.println(">>> FILTRO JWT EJECUTADO: " + request.getRequestURI());
        System.out.println(">>> URI: " + request.getRequestURI());
        System.out.println(">>> Authorization header: " + request.getHeader("Authorization"));
        System.out.println(">>> Content-Type: " + request.getHeader("Content-Type"));

        try {
            // ------ 1. Extraemos El Token Del Encabezado "Authorization" ------
            String jwt = parseJwt(request);

            // ------ 2. Si El Token Existe Y Es Valido (No Ha Caducado Ni Ha Sido Alterado) ------
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // ------ 3. Cargamos El Usuario De La Base De Datos ------
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // ------ 4. Creamos Una "Tarjeta De Identificacion" (Authentication) De Spring ------
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                
                // ------ Agregamos Mas Detalles Obtenidos Http O Sesion ------
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // ------ 5. Metemos Esa Tarjeta En El Contexto Asi La Api Sabe Que Este Usuario Tiene Permiso ------
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("No se puede establecer la autenticación del usuario: {}", e);
        }

        // ------ 6. Dejamos Que La Peticion Siga Su Camino Hacia El Controlador ------
        filterChain.doFilter(request, response);
    }

    /* ###### METODOS UTILITARIOS ###### */

    // ------ Metodo Auxiliar Para Limpiar El Prefijo "Bearer " Del Token ------
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
