package com.ticketpro.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketpro.api.model.Direccion;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {

}
