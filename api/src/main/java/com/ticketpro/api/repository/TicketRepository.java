package com.ticketpro.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketpro.api.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
