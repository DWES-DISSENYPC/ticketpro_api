package com.ticketpro.api.service;

import com.ticketpro.api.dto.CompraDTO;
import com.ticketpro.api.dto.HistorialCompraDTO;
import com.ticketpro.api.exception.ConflictoException;
import com.ticketpro.api.exception.RecursoNoEncontrado;
import com.ticketpro.api.model.*;
import com.ticketpro.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompraService {

    @Autowired private SesionRepository sesionRepo;
    @Autowired private CompraRepository compraRepo;
    @Autowired private UsuarioRepository usuarioRepo;

    @Transactional
   public void procesarCompra(CompraDTO datos, String username) {
    // 1. Validar Sesión
        Sesion sesion = sesionRepo.findById(datos.getSesionId())
        .orElseThrow(() -> new RecursoNoEncontrado("La sesión no existe"));

    // 2. Buscar al Usuario por email
        Optional<Usuario> opt = usuarioRepo.findByUsername(username);
        if (opt.isEmpty()) throw new RecursoNoEncontrado("Usuario no identificado");
        Usuario usuario = opt.get();
        // 2. Validar Capacidad (Lógica de negocio)
        int disponibles = sesion.getSala().getCapacidad() - sesion.getEntradasVendidas();
        if (datos.getCantidad() > disponibles) {
            throw new ConflictoException("Lo sentimos, solo quedan " + disponibles + " entradas disponibles.");
        }

        // 3. Crear la Compra
        Compra compra = new Compra();
        compra.setSesion(sesion);
        
        compra.setUsuario(usuario);

        compra.setNumEntradas(datos.getCantidad());
        compra.setPrecioUnitario(sesion.getPrecioBase());
        compra.setTotalPagado(sesion.getPrecioBase().multiply(new BigDecimal(datos.getCantidad())));
        compra.setFechaCompra(LocalDateTime.now());
        compra.setLocalizador(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        compra.setEstadoPago(EstadoPago.PAGADA); // Asumimos pago directo por ahora

        // 4. Actualizar inventario de la sesión
        sesion.setEntradasVendidas(sesion.getEntradasVendidas() + datos.getCantidad());
        
        // 5. Guardar todo
        compraRepo.save(compra);
        sesionRepo.save(sesion);
    }

    public List<HistorialCompraDTO> obtenerHistorial(String username) {
    
        List<Compra> compras = compraRepo.findByUsuarioUsername(username);
        List<HistorialCompraDTO> historial = new ArrayList<>();
        for (Compra c : compras) {
            historial.add(historiaToHistorialDTO(c));

        }
        return historial;
}

    public HistorialCompraDTO historiaToHistorialDTO(Compra c) {

        return new HistorialCompraDTO(
             c.getLocalizador(),
            c.getSesion().getEvento().getTitulo(),
            c.getSesion().getEvento().getCategoria(),
            c.getSesion().getFechaHora(),
            c.getSesion().getSala().getNombre(),
            c.getSesion().getSala().getUbicacion().getCiudad(),
            c.getNumEntradas(),
            c.getPrecioUnitario(),
            c.getTotalPagado(),
            c.getEstadoPago().toString()

        );
    }
}
