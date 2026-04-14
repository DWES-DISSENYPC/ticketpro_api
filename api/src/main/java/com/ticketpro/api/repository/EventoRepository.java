package com.ticketpro.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketpro.api.model.Evento;

/* ###### REPOSITORIO DE EVENTOS ###### */
// ------ Gestiona El Acceso A Los Eventos Almacenados En Base De Datos ------
public interface EventoRepository extends JpaRepository<Evento, Long> {

    /* ###### BUSQUEDAS Y VERIFICACIONES ###### */

    // ------ Obtiene Una Lista De Eventos Filtrados Por Categoria ------
    List<Evento> findByCategoria(String categoria);

    // ------ Verifica Si Ya Existe Un Evento Con Ese Identificador De Ticketmaster ------
    boolean existsByTicketmasterId(String tmId);
}
