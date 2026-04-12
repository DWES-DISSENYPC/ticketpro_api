package com.ticketpro.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketpro.api.model.Ubicacion;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
Optional<Ubicacion> findByNombreAndCiudad(String nombre, String ciudad);

}
