package com.ticketpro.api.service;

import com.ticketpro.api.dto.CambioPasswordDTO;
import com.ticketpro.api.dto.RegistroRequestDTO;
import com.ticketpro.api.dto.TelefonoDTO;
import com.ticketpro.api.dto.UsuarioPerfilDTO;
import com.ticketpro.api.dto.UsuarioUpdateDTO;
import com.ticketpro.api.exception.AccesoDenegadoException;
import com.ticketpro.api.exception.ConflictoException;
import com.ticketpro.api.exception.RecursoNoEncontrado;
import com.ticketpro.api.model.Direccion;
import com.ticketpro.api.model.Rol;
import com.ticketpro.api.model.Telefono;
import com.ticketpro.api.model.TipoTelefono;
import com.ticketpro.api.model.Usuario;
import com.ticketpro.api.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public UsuarioPerfilDTO obtenerPerfilCompleto(String username) {
        Usuario u = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado con username: " + username));
        u.getTelefonos().size();
        return entityToPerfilDto(u);
    }

    @Transactional
    public UsuarioUpdateDTO actualizarPerfil(String username, UsuarioUpdateDTO dto) {

        Usuario usuario = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado"));

        // --- CAMPOS BÁSICOS ---
        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            usuario.setNombre(dto.getNombre());
        }

        if (dto.getApellidos() != null && !dto.getApellidos().isBlank()) {
            usuario.setApellidos(dto.getApellidos());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            usuario.setEmail(dto.getEmail());
        }

        if (dto.getFechaNacimiento() != null) {
            usuario.setFechaNacimiento(dto.getFechaNacimiento());
        }

        if (dto.getMetodoPagoPreferido() != null) {
            usuario.setMetodoPagoPref(dto.getMetodoPagoPreferido());
        }

        // --- DIRECCIÓN ---
        Direccion dir = usuario.getDireccion();
        if (dir == null) {
            dir = new Direccion();
            dir.setUsuario(usuario);
        }

        if (dto.getCalle() != null && !dto.getCalle().isBlank()) {
            dir.setCalle(dto.getCalle());
        }

        if (dto.getNumero() != null && !dto.getNumero().isBlank()) {
            dir.setNumero(dto.getNumero());
        }

        if (dto.getPisoPuerta() != null && !dto.getPisoPuerta().isBlank()) {
            dir.setPisoPuerta(dto.getPisoPuerta());
        }

        if (dto.getCiudad() != null && !dto.getCiudad().isBlank()) {
            dir.setCiudad(dto.getCiudad());
        }

        if (dto.getProvincia() != null && !dto.getProvincia().isBlank()) {
            dir.setProvincia(dto.getProvincia());
        }

        if (dto.getCodigoPostal() != null && !dto.getCodigoPostal().isBlank()) {
            dir.setCodigoPostal(dto.getCodigoPostal());
        }

        if (dto.getPais() != null && !dto.getPais().isBlank()) {
            dir.setPais(dto.getPais());
        }

        usuario.setDireccion(dir);

        // --- TELÉFONOS ---
        usuario.getTelefonos().clear();

        if (dto.getTelefonos() != null) {
            for (TelefonoDTO tDto : dto.getTelefonos()) {
                Telefono t = new Telefono();
                t.setNumero(tDto.getNumero());
                t.setTipo(TipoTelefono.valueOf(tDto.getTipo()));
                t.setUsuario(usuario);
                usuario.getTelefonos().add(t);
            }
        }

        if (dto.getDni() != null && !dto.getDni().isBlank()) {
            usuario.setDni(dto.getDni());
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

        if (usuarioRepo.existsByDni(dto.getDni())) {
            throw new ConflictoException("El DNI '" + dto.getDni() + "' ya está registrado.");
        }

        // 2. CREACIÓN DE LA ENTIDAD
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(dto.getUsername());
        nuevoUsuario.setEmail(dto.getEmail());
        nuevoUsuario.setNombre(dto.getNombre());
        nuevoUsuario.setApellidos(dto.getApellidos());
        nuevoUsuario.setDni(dto.getDni());

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
        dto.setMetodoPagoPreferido(u.getMetodoPagoPref());
        dto.setImagenUrl(u.getImagenUrl());
        dto.setDni(u.getDni());

        // Dirección completa
        if (u.getDireccion() != null) {
            dto.setCalle(u.getDireccion().getCalle());
            dto.setNumero(u.getDireccion().getNumero());
            dto.setPisoPuerta(u.getDireccion().getPisoPuerta());
            dto.setCiudad(u.getDireccion().getCiudad());
            dto.setProvincia(u.getDireccion().getProvincia());
            dto.setCodigoPostal(u.getDireccion().getCodigoPostal());
            dto.setPais(u.getDireccion().getPais());
        }

        // Teléfonos
        if (u.getTelefonos() != null) {
            List<TelefonoDTO> lista = u.getTelefonos().stream()
                    .map(t -> {
                        TelefonoDTO dtoTel = new TelefonoDTO();
                        dtoTel.setNumero(t.getNumero());
                        dtoTel.setTipo(t.getTipo().name());
                        return dtoTel;
                    })
                    .collect(Collectors.toList());

            dto.setTelefonos(lista);
        }

        return dto;
    }

    public void generarTokenRecuperacion(String email) {
        long startTime = System.currentTimeMillis();

        // Usamos java.util.Optional
        Optional<Usuario> usuarioOpt = usuarioRepo.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String token = UUID.randomUUID().toString();

            usuario.setPasswordResetToken(token);
            // usuario.setTokenExpiration(LocalDateTime.now().plusHours(1)); // Opcional
            // pero recomendado
            usuarioRepo.save(usuario);

            // Envío del email (esto es lo que tarda más y justifica el delay)
            emailService.enviarCorreoHTML(usuario.getEmail(), token);
        } else {
            // Log discreto para desarrollo
            System.out.println("Aviso: Intento de recuperación para email no registrado: " + email);
        }

        // Mantenemos la simetría de tiempo para evitar ataques de temporización
        long duration = System.currentTimeMillis() - startTime;
        if (duration < 1500) {
            try {
                Thread.sleep(1500 - duration);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Transactional
    public void resetearPassword(String token, String nuevaPassword) {
        // Aquí sí lanzamos excepción porque el usuario ya está interactuando con un
        // token específico
        Usuario usuario = usuarioRepo.findByPasswordResetToken(token)
                .orElseThrow(() -> new AccesoDenegadoException("Token de recuperación inválido o expirado."));

        // Encriptamos y guardamos
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));

        // Limpieza de seguridad
        usuario.setPasswordResetToken(null);
        // usuario.setTokenExpiration(null);

        usuarioRepo.save(usuario);
    }

    @Transactional
    public Map<String, Object> obtenerDatosPerfilCompleto(String email) {
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado"));

        Map<String, Object> perfil = new HashMap<>();

        // Datos básicos
        perfil.put("username", usuario.getUsername());
        perfil.put("nombre", usuario.getNombre());
        perfil.put("apellidos", usuario.getApellidos());
        perfil.put("email", usuario.getEmail());
        perfil.put("dni", usuario.getDni());
        perfil.put("fechaNacimiento", usuario.getFechaNacimiento());
        perfil.put("metodoPagoPref", usuario.getMetodoPagoPref());
        perfil.put("imagenUrl", usuario.getImagenUrl());
        perfil.put("rol", usuario.getRol());
        perfil.put("createdAt", usuario.getCreatedAt());

        // Dirección (si existe)
        if (usuario.getDireccion() != null) {
            perfil.put("direccion", usuario.getDireccion());
        }

        // Teléfonos (solo los números)
        if (usuario.getTelefonos() != null) {
            perfil.put("telefonos", usuario.getTelefonos().stream()
                    .map(t -> t.getNumero())
                    .toList());
        }

        return perfil;
    }

    private UsuarioPerfilDTO entityToPerfilDto(Usuario u) {
        UsuarioPerfilDTO dto = new UsuarioPerfilDTO();
        dto.setUsername(u.getUsername());
        dto.setNombre(u.getNombre());
        dto.setApellidos(u.getApellidos());
        dto.setEmail(u.getEmail());
        dto.setDni(u.getDni());
        dto.setRol(u.getRol());
        dto.setCreatedAt(u.getCreatedAt()); // Ahora sí viaja
        dto.setFechaNacimiento(u.getFechaNacimiento());
        dto.setImagenUrl(u.getImagenUrl());
        dto.setMetodoPagoPref(u.getMetodoPagoPref());

        if (u.getDireccion() != null) {
            dto.setCalle(u.getDireccion().getCalle());
            dto.setCiudad(u.getDireccion().getCiudad());
            dto.setCodigoPostal(u.getDireccion().getCodigoPostal());

            dto.setNumero(u.getDireccion().getNumero());
            dto.setPisoPuerta(u.getDireccion().getPisoPuerta()); // Revisa si se llama así en tu entidad
            dto.setProvincia(u.getDireccion().getProvincia());
            dto.setPais(u.getDireccion().getPais());
        }

        // ... mapeo de teléfonos ...
        if (u.getTelefonos() != null) {
            List<TelefonoDTO> listaTelefonos = u.getTelefonos().stream()
                    // Usamos .name() para pasar de TipoTelefono (Enum) a String
                    .map(t -> new TelefonoDTO(t.getNumero(), t.getTipo().name()))
                    .collect(Collectors.toList());
            dto.setTelefonos(listaTelefonos);
        } else {
            dto.setTelefonos(new ArrayList<>());
        }
        return dto;
    }

}
