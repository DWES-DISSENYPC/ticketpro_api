package com.ticketpro.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketpro.api.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByCategoria(String categoria);
}
