package com.ticketpro.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketpro.api.model.Sala;
import com.ticketpro.api.model.Ubicacion;

/* ###### REPOSITORIO DE SALAS ###### */
// ------ Maneja La Conexion Con La Tabla De Salas En Base De Datos ------
public interface SalaRepository extends JpaRepository<Sala, Long> {

    /* ###### BUSQUEDAS PERSONALIZADAS ###### */

    // ------ Busca Una Sala Por Su Nombre Y El Objeto De Su Ubicacion ------
    Optional<Sala> findByNombreAndUbicacion(String nombre, Ubicacion ubi);

}
