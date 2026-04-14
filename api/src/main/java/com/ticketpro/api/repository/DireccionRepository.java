package com.ticketpro.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketpro.api.model.Direccion;

/* ###### REPOSITORIO DE DIRECCION ###### */
// ------ Interfaz Jpa Para La Persistencia De Entidades Direccion ------
public interface DireccionRepository extends JpaRepository<Direccion, Long> {

}
