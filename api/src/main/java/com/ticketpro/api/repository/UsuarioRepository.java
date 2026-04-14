package com.ticketpro.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketpro.api.model.Usuario;

/* ###### REPOSITORIO DE USUARIOS ###### */
// ------ Gestiona Las Operaciones En Base De Datos De Cuentas De Usuario ------
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /* ###### BUSQUEDAS Y VERIFICACIONES DE SEGURIDAD ###### */

    // ------ Busca Un Usuario Por Su Nombre De Sesion ------
    Optional<Usuario> findByUsername(String username);

    // ------ Devuelve Verdadero Si El Username Ya Se Encuentra Registrado ------
    Boolean existsByUsername(String username);

    // ------ Comprueba La Existencia Del Email Solicitado ------
    boolean existsByEmail(String email);

    // ------ Verifica Que El Dni No Este Siendo Usado Por Otra Persona ------
    boolean existsByDni(String dni);

    /* ###### BUSQUEDAS DE RECUPERACION DE DATOS ###### */

    // ------ Busca El Usuario Propietario Del Token De Reset ------
    Optional<Usuario> findByPasswordResetToken(String token);

    // ------ Encuentra Al Usuario Atado A Dicho Correo ------
    Optional<Usuario> findByEmail(String email);
}
