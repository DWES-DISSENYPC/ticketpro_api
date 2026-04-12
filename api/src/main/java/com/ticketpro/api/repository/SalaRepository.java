package com.ticketpro.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketpro.api.model.Sala;
import com.ticketpro.api.model.Ubicacion;

public interface SalaRepository extends JpaRepository<Sala, Long> {

    Optional<Sala> findByNombreAndUbicacion(String nombre, Ubicacion ubi);

}
