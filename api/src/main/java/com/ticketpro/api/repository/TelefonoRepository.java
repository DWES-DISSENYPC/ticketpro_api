package com.ticketpro.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketpro.api.model.Telefono;

/* ###### REPOSITORIO DE TELEFONOS ###### */
// ------ Interfaz Jpa Para La Persistencia De Entidades Telefono ------
public interface TelefonoRepository extends JpaRepository<Telefono, Long> {

}
