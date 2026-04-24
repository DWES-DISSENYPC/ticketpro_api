package com.ticketpro.api.repository;

import com.ticketpro.api.model.CarritoItem;
import com.ticketpro.api.model.Usuario;
import com.ticketpro.api.model.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
    List<CarritoItem> findByUsuario(Usuario usuario);
    Optional<CarritoItem> findByUsuarioAndSesion(Usuario usuario, Sesion sesion);
    void deleteByUsuario(Usuario usuario);
}
