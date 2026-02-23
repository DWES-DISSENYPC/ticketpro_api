package com.ticketpro.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketpro.api.model.Compra;

public interface CompraRepository extends JpaRepository<Compra, Long>{

    Optional<Compra> findByLocalizador(String localizador);
}
