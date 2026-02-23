package com.ticketpro.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketpro.api.model.Sesion;

public interface SesionRepository extends JpaRepository<Sesion, Long> {

}
