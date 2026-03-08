package com.ticketpro.api.service;

import com.ticketpro.api.dto.CambioPasswordDTO;
import com.ticketpro.api.dto.RegistroRequestDTO;
import com.ticketpro.api.dto.UsuarioUpdateDTO;
import com.ticketpro.api.exception.AccesoDenegadoException;
import com.ticketpro.api.exception.ConflictoException;
import com.ticketpro.api.exception.RecursoNoEncontrado;
import com.ticketpro.api.model.Direccion;
import com.ticketpro.api.model.Rol;
import com.ticketpro.api.model.Telefono;
import com.ticketpro.api.model.Usuario;
import com.ticketpro.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioUpdateDTO obtenerPerfilCompleto(String username) {
        Usuario u = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado con username: " + username));
        return entityToUpdateDto(u);
    }

    @Transactional
    public UsuarioUpdateDTO actualizarPerfil(String username, UsuarioUpdateDTO dto) {
        Usuario usuario = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado"));

        // 1. Campos básicos de Usuario
        usuario.setNombre(dto.getNombre());
        usuario.setApellidos(dto.getApellidos());
        usuario.setEmail(dto.getEmail());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setImagenUrl(dto.getImagenPerfil());
        usuario.setMetodoPagoPref(dto.getMetodoPagoPreferido());

        // 2. Gestión de Dirección
        Direccion dir = usuario.getDireccion();
        if (dir == null) {
            dir = new Direccion();
            dir.setUsuario(usuario);
        }
        dir.setCalle(dto.getDireccion()); // Mapeamos 'direccion' del DTO a 'calle'
        dir.setCiudad(dto.getCiudad());
        dir.setCodigoPostal(dto.getCodigoPostal());
        usuario.setDireccion(dir);

        // 3. Gestión de Teléfonos
        // Limpiamos los anteriores (orphanRemoval = true se encargará en la DB)
        usuario.getTelefonos().clear();
        if (dto.getTelefonos() != null) {
            for (String num : dto.getTelefonos()) {
                Telefono t = new Telefono();
                t.setNumero(num);
                t.setUsuario(usuario);
                usuario.getTelefonos().add(t);
            }
        }

        Usuario actualizado = usuarioRepo.save(usuario);
        return entityToUpdateDto(actualizado);
    }

    @Transactional
    public void desactivarUsuario(String username) {
        Usuario usuario = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setActivo(false); // Pasamos de true a false
        usuarioRepo.save(usuario);
    }

    @Transactional
    public void crearNuevoUsuario(RegistroRequestDTO dto) {

        // 1. COMPROBACIONES (Lógica de negocio)
        if (usuarioRepo.existsByUsername(dto.getUsername())) {
            throw new ConflictoException("El nombre de usuario '" + dto.getUsername() + "' ya está en uso.");
        }

        if (usuarioRepo.existsByEmail(dto.getEmail())) {
            throw new ConflictoException("El email '" + dto.getEmail() + "' ya está registrado.");
        }

        // 2. CREACIÓN DE LA ENTIDAD
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(dto.getUsername());
        nuevoUsuario.setEmail(dto.getEmail());
        nuevoUsuario.setNombre(dto.getNombre());
        nuevoUsuario.setApellidos(dto.getApellidos());

        // Encriptamos la contraseña antes de guardar
        nuevoUsuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Asignamos valores por defecto
        nuevoUsuario.setRol(Rol.CLIENTE);
        nuevoUsuario.setActivo(true);

        usuarioRepo.save(nuevoUsuario);

    }

    @Transactional
    public void actualizarPassword(String username, CambioPasswordDTO dto) {
        Usuario usuario = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 1. Validar que la contraseña actual es correcta
        // passwordEncoder.matches(texto_plano, hash_guardado)
        if (!passwordEncoder.matches(dto.getPasswordActual(), usuario.getPassword())) {
            throw new AccesoDenegadoException("La contraseña actual no es correcta.");
        }

        // 2. Encriptar la nueva contraseña
        String nuevaPasswordEncriptada = passwordEncoder.encode(dto.getPasswordNueva());

        // 3. Guardar cambios
        usuario.setPassword(nuevaPasswordEncriptada);
        usuarioRepo.save(usuario);
    }

    private UsuarioUpdateDTO entityToUpdateDto(Usuario u) {
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
        dto.setNombre(u.getNombre());
        dto.setApellidos(u.getApellidos());
        dto.setEmail(u.getEmail());
        dto.setFechaNacimiento(u.getFechaNacimiento());
        dto.setImagenPerfil(u.getImagenUrl());
        dto.setMetodoPagoPreferido(u.getMetodoPagoPref());

        // Leer de la entidad Direccion
        if (u.getDireccion() != null) {
            dto.setDireccion(u.getDireccion().getCalle());
            dto.setCiudad(u.getDireccion().getCiudad());
            dto.setCodigoPostal(u.getDireccion().getCodigoPostal());
        }

        // Leer de la lista de Teléfonos
        if (u.getTelefonos() != null) {
            dto.setTelefonos(u.getTelefonos().stream()
                    .map(Telefono::getNumero)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}