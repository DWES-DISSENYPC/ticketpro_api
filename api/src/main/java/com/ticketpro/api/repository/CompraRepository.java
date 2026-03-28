package com.ticketpro.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketpro.api.model.Compra;
import com.ticketpro.api.model.EstadoPago;

public interface CompraRepository extends JpaRepository<Compra, Long> {

    Optional<Compra> findByLocalizador(String localizador);

    List<Compra> findByUsuarioUsername(String username);

    List<Compra> findByUsuarioIdAndEstadoPago(Long usuarioId, String estadoPago);
}
