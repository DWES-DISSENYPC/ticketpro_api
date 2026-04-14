package com.ticketpro.api.security.services;

import com.ticketpro.api.exception.AccesoDenegadoException;
import com.ticketpro.api.model.Usuario;
import com.ticketpro.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/* ###### SERVICIO DE DETALLES DE USUARIO ###### */
// ------ Marca Esta Clase Como Un Componente De Servicio Que Spring Debe Gestionar ------
@Service 
public class UserDetailsServiceImpl implements UserDetailsService {

    /* ###### DEPENDENCIAS Y REPOSITORIOS ###### */

    // ------ Inyecta Tu Repositorio Para Poder Consultar La Base De Datos ------
    @Autowired 
    UsuarioRepository usuarioRepository;

    /* ###### SOBRESCRITURA DE CARGA ###### */

    // ------ Carga De Detalles Transaccional Obligatoria Por Spring Security ------
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // ------ 1. Buscamos El Usuario Y Confirmamos Su Existencia O Lanzamos Error ------
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new AccesoDenegadoException("Los datos de acceso son incorrectos. Ponte en contacto con el soporte técnico si crees que esto es un error."));
        
        // ------ Control De Estado Del Usuario Inactivo Y Bloqueado ------
        if(!usuario.isActivo()) {
            throw new AccesoDenegadoException("Los datos de acceso son incorrectos. Ponte en contacto con el soporte técnico si crees que esto es un error.");
        }

        // ------ 2. Cargamos El Rol Real Desde Tu Entidad Concediendo Autorizacion ------
        // ------ Esto Evita El 403 Por Falta De Permisos ------
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(usuario.getRol().name()));

        // ------ 3. Usamos El Constructor Detallado Para Vincular El Campo Activo E Identidades ------
        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(), 
                usuario.getPassword(), 
                usuario.isActivo(), // ------ ESTO ES CLAVE: enabled ------
                true,               // ------ accountNonExpired ------
                true,               // ------ credentialsNonExpired ------
                true,               // ------ accountNonLocked ------
                authorities         // ------ Nuestra Lista Con El Rol Real ------
        );
    }
}
