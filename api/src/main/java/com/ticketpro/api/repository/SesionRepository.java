package com.ticketpro.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketpro.api.model.Sesion;

/* ###### REPOSITORIO DE SESIONES ###### */
// ------ Interfaz Jpa Para La Persistencia De Entidades Sesion ------
public interface SesionRepository extends JpaRepository<Sesion, Long> {

    /* ###### BUSQUEDAS PERSONALIZADAS ###### */

    // ------ Devuelve Lista De Sesiones Por Identificador De Evento ------
    List<Sesion> findByEventoId(Long eventoId);

}
