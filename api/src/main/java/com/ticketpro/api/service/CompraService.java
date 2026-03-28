package com.ticketpro.api.service;

import com.ticketpro.api.dto.CompraEntradasDTO;
import com.ticketpro.api.dto.DetalleCompraDTO;
import com.ticketpro.api.dto.HistorialCompraDTO;
import com.ticketpro.api.exception.AccesoDenegadoException;
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
import java.util.stream.Collectors;

@Service
public class CompraService {

    @Autowired
    private SesionRepository sesionRepository;
    @Autowired
    private CompraRepository compraRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Transactional
    public void realizarCompra(String username, CompraEntradasDTO dto) {
        // 1. Cargar datos
        Sesion sesion = sesionRepository.findById(dto.getSesionId())
                .orElseThrow(() -> new RecursoNoEncontrado("Sesión no encontrada"));

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado"));

        // 2. Validación de Stock
        int capacidadMax = sesion.getSala().getCapacidad();
        int vendidasYa = sesion.getEntradasVendidas();

        if (vendidasYa + dto.getCantidad() > capacidadMax) {
            throw new RuntimeException("Aforo completo. No quedan suficientes entradas.");
        }

        // 3. Actualizar stock en la Sesión
        sesion.setEntradasVendidas(vendidasYa + dto.getCantidad());
        if (sesion.getEntradasVendidas() >= capacidadMax) {
            sesion.setEstado(2); // 2: AGOTADA
        }
        sesionRepository.save(sesion);

        // 4. Crear la Entidad Compra
        Compra compra = new Compra();
        compra.setUsuario(usuario);
        compra.setSesion(sesion);
        compra.setNumEntradas(dto.getCantidad());
        compra.setPrecioUnitario(sesion.getPrecioBase());

        BigDecimal total = sesion.getPrecioBase().multiply(new BigDecimal(dto.getCantidad()));
        compra.setTotalPagado(total);

        // Generación de localizador y datos de pago
        compra.setLocalizador("TPRO-" + System.currentTimeMillis() % 100000);
        compra.setMetodoPago(usuario.getMetodoPagoPref());
        compra.setEstadoPago(EstadoPago.PAGADA);

        // El campo qrCodeData de Compra puede ser un resumen o el localizador
        compra.setQrCodeData("COMPRA-" + compra.getLocalizador());

        // Guardamos la compra para obtener el ID antes de los tickets
        compra = compraRepository.save(compra);

        // 5. EMISIÓN DE TICKETS INDIVIDUALES
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 1; i <= dto.getCantidad(); i++) {
            Ticket ticket = new Ticket();
            ticket.setCompra(compra);

            // IMPORTANTE: En tu entidad el campo se llama 'codigoQr'
            // Usamos UUID para que cada entrada sea única e imposible de falsificar
            ticket.setCodigoQr("TICKET-" + java.util.UUID.randomUUID().toString());

            // En tu entidad el campo es 'estadoTicket' de tipo 'EstadoTicket'
            ticket.setEstadoTicket(EstadoTicket.VALIDO);

            tickets.add(ticket);
        }

        ticketRepository.saveAll(tickets);
    }

    @Transactional(readOnly = true)
    public List<HistorialCompraDTO> obtenerHistorial(String username) {
        List<Compra> compras = compraRepository.findByUsuarioUsername(username);
        return compras.stream().map(c -> new HistorialCompraDTO(
                c.getId(),
                c.getLocalizador(),
                c.getSesion().getEvento().getTitulo(),
                c.getSesion().getFechaHora(),
                c.getNumEntradas(),
                c.getTotalPagado(),
                c.getEstadoPago().toString())).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DetalleCompraDTO obtenerDetalle(Long compraId, String username) {
        Compra c = compraRepository.findById(compraId)
                .orElseThrow(() -> new RecursoNoEncontrado("Compra no encontrada"));

        // Validación de seguridad: el usuario solo ve sus propias compras
        if (!c.getUsuario().getUsername().equals(username)) {
            throw new AccesoDenegadoException("No tienes permiso para ver esta compra");
        }

        // Extraemos solo los Strings de los QR de la lista de tickets
        List<String> qrs = c.getTickets().stream()
                .map(Ticket::getCodigoQr)
                .collect(Collectors.toList());

        return new DetalleCompraDTO(
                c.getLocalizador(),
                c.getSesion().getEvento().getTitulo(),
                c.getSesion().getEvento().getCategoria(),
                c.getSesion().getFechaHora(),
                c.getSesion().getSala().getNombre(),
                c.getSesion().getSala().getUbicacion().getCiudad(),
                c.getNumEntradas(),
                c.getPrecioUnitario(),
                c.getTotalPagado(),
                c.getEstadoPago().toString(),
                c.getFechaCompra(),
                qrs);
    }

    @Transactional
    public void cancelarCompra(Long compraId, String username) {
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new RecursoNoEncontrado("Compra no encontrada"));

        // 1. Seguridad: Verificar que la compra es del usuario
        if (!compra.getUsuario().getUsername().equals(username)) {
            throw new AccesoDenegadoException("No puedes cancelar una compra que no te pertenece");
        }

        // 2. Validación de tiempo (Mínimo 48h antes del evento)
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime fechaLimite = compra.getSesion().getFechaHora().minusHours(7200);

        if (ahora.isAfter(fechaLimite)) {
            throw new ConflictoException("Solo se pueden cancelar compras con más de 5 dias de antelación");
        }

        // 3. Liberar Stock en la Sesión
        Sesion sesion = compra.getSesion();
        sesion.setEntradasVendidas(sesion.getEntradasVendidas() - compra.getNumEntradas());

        // Si la sesión estaba agotada (estado 2), vuelve a estar disponible (estado 0)
        if (sesion.getEstado() == 2) {
            sesion.setEstado(0);
        }
        sesionRepository.save(sesion);

        // 4. Anular la Compra
        compra.setEstadoPago(EstadoPago.CANCELADA); // Asegúrate de tener este valor en tu Enum
        compraRepository.save(compra);

        // 5. Anular todos los Tickets individuales
        for (Ticket t : compra.getTickets()) {
            t.setEstadoTicket(EstadoTicket.ANULADO);
        }
        // No hace falta save explícito de tickets si usas CascadeType.ALL en Compra
    }

    public List<DetalleCompraDTO> obtenerComprasPendientes(Long usuarioId) {
        List<Compra> compras = compraRepository.findByUsuarioIdAndEstadoPago(usuarioId, "PENDIENTE");
        List<DetalleCompraDTO> pendientes = new ArrayList<>();
        for (Compra c : compras) {

            pendientes.add(CompraToDetalleCompraDTO(c));

        }

        return pendientes;
    }

    private DetalleCompraDTO CompraToDetalleCompraDTO(Compra compra) {
    DetalleCompraDTO dto = new DetalleCompraDTO();

    // 1. Datos básicos de la compra (usando tus nombres exactos de campos)
    dto.setLocalizador(compra.getLocalizador());
    dto.setEstado(compra.getEstadoPago().toString());
    dto.setFechaCompra(compra.getFechaCompra());
    dto.setTotalPagado(compra.getTotalPagado());
    dto.setPrecioUnitario(compra.getPrecioUnitario()); // <-- Usamos el de Compra
    dto.setNumEntradas(compra.getNumEntradas());      // <-- Usamos el de Compra

    // 2. Datos del Evento y Ubicación
    if (compra.getSesion() != null) {
        dto.setTituloEvento(compra.getSesion().getEvento().getTitulo());
        dto.setCategoriaEvento(compra.getSesion().getEvento().getCategoria());
        dto.setFechaSesion(compra.getSesion().getFechaHora());
        
        if (compra.getSesion().getSala() != null) {
            dto.setNombreSala(compra.getSesion().getSala().getNombre());
            
            if (compra.getSesion().getSala().getUbicacion() != null) {
                dto.setCiudad(compra.getSesion().getSala().getUbicacion().getCiudad());
            }
        }
    }

    // 3. Códigos QR de la lista de TICKETS
    if (compra.getTickets() != null && !compra.getTickets().isEmpty()) {
        List<String> qrs = compra.getTickets().stream()
                .map(ticket -> ticket.getCodigoQr())
                .collect(Collectors.toList());
        dto.setCodigosQr(qrs);
    }

    return dto;
}
}
