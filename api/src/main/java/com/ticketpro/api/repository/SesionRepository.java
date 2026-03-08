package com.ticketpro.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketpro.api.model.Sesion;

public interface SesionRepository extends JpaRepository<Sesion, Long> {

    List<Sesion> findByEventoId(Long eventoId);

}
