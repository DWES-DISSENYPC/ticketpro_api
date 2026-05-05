package com.ticketpro.api.service;

import com.ticketpro.api.dto.CompraEntradasDTO;
import com.ticketpro.api.dto.DetalleCompraDTO;
import com.ticketpro.api.dto.HistorialCompraDTO;
import com.ticketpro.api.dto.TicketDTO;
import com.ticketpro.api.exception.AccesoDenegadoException;
import com.ticketpro.api.exception.ConflictoException;
import com.ticketpro.api.exception.RecursoNoEncontrado;
import com.ticketpro.api.model.*;
import com.ticketpro.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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

        // Mapeo de tickets a TicketDTO con generación de URL de Google Charts QR
        List<TicketDTO> tickets = c.getTickets().stream()
                .map(t -> {
                    String qrUrl = "/api/compras/ticket/" + t.getCodigoQr() + "/qr";
                    return new TicketDTO(
                            t.getId(),
                            t.getCodigoQr(),
                            qrUrl,
                            c.getSesion().getEvento().getTitulo(),
                            c.getSesion().getFechaHora(),
                            c.getSesion().getSala().getNombre(),
                            c.getSesion().getSala().getUbicacion().getCiudad(),
                            t.getEstadoTicket() == EstadoTicket.USADO
                    );
                })
                .collect(Collectors.toList());

        return new DetalleCompraDTO(
                c.getId(),
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
                tickets);
    }

    @Transactional
    public void cancelarCompra(Long compraId, String username) {
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new RecursoNoEncontrado("Compra no encontrada"));

        // 1. Seguridad: Verificar que la compra es del usuario
        if (!compra.getUsuario().getUsername().equals(username)) {
            throw new AccesoDenegadoException("No puedes cancelar una compra que no te pertenece");
        }

        // 2. Validación de tiempo (Mínimo 5 días antes del evento)
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime fechaLimite = compra.getSesion().getFechaHora().minusDays(5);

        if (ahora.isAfter(fechaLimite)) {
            throw new ConflictoException("Solo se pueden cancelar compras con más de 5 días de antelación");
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
        List<Compra> compras = compraRepository.findByUsuarioIdAndEstadoPago(usuarioId, EstadoPago.PENDIENTE);
        List<DetalleCompraDTO> pendientes = new ArrayList<>();
        for (Compra c : compras) {

            pendientes.add(CompraToDetalleCompraDTO(c));

        }

        return pendientes;
    }

    private DetalleCompraDTO CompraToDetalleCompraDTO(Compra compra) {
    DetalleCompraDTO dto = new DetalleCompraDTO();

    // 1. Datos básicos de la compra (usando tus nombres exactos de campos)
    dto.setId(compra.getId());
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

    // 3. Listado de TICKETS con QR
    if (compra.getTickets() != null && !compra.getTickets().isEmpty()) {
        List<TicketDTO> tickets = compra.getTickets().stream()
                .map(t -> {
                    String qrUrl = "/api/compras/ticket/" + t.getCodigoQr() + "/qr";
                    return new TicketDTO(
                            t.getId(),
                            t.getCodigoQr(),
                            qrUrl,
                            compra.getSesion().getEvento().getTitulo(),
                            compra.getSesion().getFechaHora(),
                            compra.getSesion().getSala().getNombre(),
                            compra.getSesion().getSala().getUbicacion().getCiudad(),
                            t.getEstadoTicket() == EstadoTicket.USADO
                    );
                })
                .collect(Collectors.toList());
        dto.setTickets(tickets);
    }

    return dto;
}

    public Long obtenerIdUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .map(Usuario::getId)
                .orElse(null);
    }

    public byte[] obtenerImagenQR(String codigo) {
        String externalUrl = "https://api.qrserver.com/v1/create-qr-code/?size=160x160&data=" + codigo;
        WebClient webClient = WebClient.create();
        return webClient.get()
                .uri(externalUrl)
                .retrieve()
                .bodyToMono(byte[].class)
                .block(); 
    }

    public byte[] obtenerTicketCompleto(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RecursoNoEncontrado("Ticket no encontrado"));
        
        Compra compra = ticket.getCompra();
        Sesion sesion = compra.getSesion();
        Evento evento = sesion.getEvento();
        
        // 1. Obtener la imagen del QR
        byte[] qrBytes = obtenerImagenQR(ticket.getCodigoQr());
        
        try {
            BufferedImage qrImage = ImageIO.read(new ByteArrayInputStream(qrBytes));
            
            // 2. Crear una imagen nueva para el ticket (ej: 400x550)
            int width = 400;
            int height = 550;
            BufferedImage ticketImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = ticketImage.createGraphics();
            
            // Fondos y bordes
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, width, height);
            
            // Color primario (Azul oscuro aproximado)
            Color colorPrimario = new Color(25, 25, 112);
            
            g2d.setColor(colorPrimario);
            g2d.setStroke(new BasicStroke(10));
            g2d.drawRect(5, 5, width - 10, height - 10);
            
            // Antialiasing para texto suave
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            // 3. Dibujar textos
            g2d.setColor(colorPrimario);
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            g2d.drawString("TICKETPRO", 120, 50);
            
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            // Dividir titulo si es muy largo
            String titulo = evento.getTitulo();
            if (titulo.length() > 30) {
                g2d.drawString(titulo.substring(0, 30) + "...", 30, 100);
            } else {
                g2d.drawString(titulo, 30, 100);
            }
            
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            g2d.drawString("Fecha: " + sesion.getFechaHora().format(formatter), 30, 140);
            g2d.drawString("Lugar: " + sesion.getSala().getNombre(), 30, 170);
            g2d.drawString("Ciudad: " + sesion.getSala().getUbicacion().getCiudad(), 30, 200);
            
            g2d.setFont(new Font("Arial", Font.ITALIC, 12));
            g2d.drawString("Localizador: " + compra.getLocalizador(), 30, 240);
            g2d.drawString("Ticket ID: #" + ticket.getId(), 30, 260);
            
            // 4. Dibujar el QR centrado
            int qrX = (width - qrImage.getWidth()) / 2;
            g2d.drawImage(qrImage, qrX, 300, null);
            
            g2d.setFont(new Font("Monospaced", Font.BOLD, 12));
            String cod = ticket.getCodigoQr();
            if (cod.length() > 35) cod = cod.substring(0, 35) + "...";
            g2d.drawString(cod, 50, 480);
            
            g2d.dispose();
            
            // 5. Convertir a bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(ticketImage, "png", baos);
            return baos.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Error al generar la imagen del ticket", e);
        }
    }
}
