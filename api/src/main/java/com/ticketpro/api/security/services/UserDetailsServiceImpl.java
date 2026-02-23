package com.ticketpro.api.security.services;

import com.ticketpro.api.model.Usuario;
import com.ticketpro.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service // Marca esta clase como un componente de servicio que Spring debe gestionar
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired // Inyecta tu repositorio para poder consultar la base de datos de XAMPP
    UsuarioRepository usuarioRepository;

    @Override
    @Transactional // Asegura que la consulta a la base de datos sea segura y completa
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // 1. Buscamos el usuario en la tabla 'usuarios' por su nombre de usuario
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username: " + username));

        // 2. Convertimos nuestro objeto 'Usuario' en un objeto 'UserDetails' que entiende Spring.
        // Por ahora le pasamos una lista vacía de permisos (ArrayList), ya los afinaremos luego.
        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(), 
                usuario.getPassword(), 
                new ArrayList<>()
        );
    }
}
