package com.ticketpro.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketpro.api.model.Ticket;

/* ###### REPOSITORIO DE TICKETS ###### */
// ------ Interfaz Jpa Para La Persistencia De Entidades Ticket ------
public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
