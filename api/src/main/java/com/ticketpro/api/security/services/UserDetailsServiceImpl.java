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

@Service // Marca esta clase como un componente de servicio que Spring debe gestionar
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired // Inyecta tu repositorio para poder consultar la base de datos de XAMPP
    UsuarioRepository usuarioRepository;

@Override
@Transactional
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    
    // 1. Buscamos el usuario
    Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new AccesoDenegadoException("Los datos de acceso son incorrectos. Ponte en contacto con el soporte técnico si crees que esto es un error."));
    
    if(!usuario.isActivo()) {throw new AccesoDenegadoException
    ("Los datos de acceso son incorrectos. Ponte en contacto con el soporte técnico si crees que esto es un error.");}

    // 2. Cargamos el Rol real desde tu entidad (convertido a GrantedAuthority)
    // Esto evita el 403 por falta de permisos
    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(usuario.getRol().name()));

    // 3. Usamos el constructor detallado para vincular el campo 'activo'
    return new org.springframework.security.core.userdetails.User(
            usuario.getUsername(), 
            usuario.getPassword(), 
            usuario.isActivo(), // <--- ESTO ES CLAVE: enabled
            true,               // accountNonExpired
            true,               // credentialsNonExpired
            true,               // accountNonLocked
            authorities         // Nuestra lista con el ROL real
    );
}
}
