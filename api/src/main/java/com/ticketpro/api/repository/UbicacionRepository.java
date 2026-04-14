package com.ticketpro.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketpro.api.model.Ubicacion;

/* ###### REPOSITORIO DE UBICACIONES ###### */
// ------ Interfaz Jpa Para La Persistencia De Entidades Ubicacion ------
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {

    /* ###### BUSQUEDAS PERSONALIZADAS ###### */

    // ------ Encuentra Una Ubicacion Especifica Segun Su Nombre Y Ciudad ------
    Optional<Ubicacion> findByNombreAndCiudad(String nombre, String ciudad);

}
