package com.ticketpro.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ticketpro.api.model.Evento;

/* ###### REPOSITORIO DE EVENTOS ###### */
// ------ Gestiona El Acceso A Los Eventos Almacenados En Base De Datos ------
public interface EventoRepository extends JpaRepository<Evento, Long> {

    /* ###### BUSQUEDAS Y VERIFICACIONES ###### */

    // ------ Obtiene Una Lista De Eventos Filtrados Por Categoria ------
    List<Evento> findByCategoria(String categoria);

    // ------ Verifica Si Ya Existe Un Evento Con Ese Identificador De Ticketmaster ------
    boolean existsByTicketmasterId(String tmId);

    // ------ Busqueda Avanzada Por Titulo O Ciudad ------
    @Query("SELECT DISTINCT e FROM Evento e " +
           "LEFT JOIN Sesion s ON s.evento = e " +
           "LEFT JOIN s.sala sa " +
           "LEFT JOIN sa.ubicacion u " +
           "WHERE LOWER(e.titulo) LIKE LOWER(CONCAT('%', :termino, '%')) " +
           "OR LOWER(u.ciudad) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Evento> buscarPorTituloOCiudad(@Param("termino") String termino);
}
