package com.ticketpro.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketpro.api.model.Compra;

/* ###### REPOSITORIO DE COMPRAS ###### */
// ------ Interfaz Para Operaciones De Base De Datos De La Entidad Compra ------
public interface CompraRepository extends JpaRepository<Compra, Long> {

    /* ###### BUSQUEDAS PERSONALIZADAS ###### */

    // ------ Encuentra Una Compra Por Su Localizador Unico ------
    Optional<Compra> findByLocalizador(String localizador);

    // ------ Recupera El Historial De Compras De Un Usuario Especifico ------
    List<Compra> findByUsuarioUsername(String username);

    // ------ Filtra Las Compras De Un Usuario Segun Su Estado De Pago ------
    List<Compra> findByUsuarioIdAndEstadoPago(Long usuarioId, String estadoPago);
}
