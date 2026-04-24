package com.ticketpro.api.service;

import com.ticketpro.api.dto.CarritoAddRequestDTO;
import com.ticketpro.api.dto.CarritoItemDTO;
import com.ticketpro.api.dto.CompraEntradasDTO;
import com.ticketpro.api.exception.RecursoNoEncontrado;
import com.ticketpro.api.model.CarritoItem;
import com.ticketpro.api.model.Sesion;
import com.ticketpro.api.model.Usuario;
import com.ticketpro.api.repository.CarritoItemRepository;
import com.ticketpro.api.repository.SesionRepository;
import com.ticketpro.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarritoService {

    @Autowired
    private CarritoItemRepository carritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SesionRepository sesionRepository;

    @Autowired
    private CompraService compraService;

    @Transactional(readOnly = true)
    public List<CarritoItemDTO> obtenerCarrito(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado"));

        return carritoRepository.findByUsuario(usuario).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void añadirAlCarrito(String username, CarritoAddRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado"));

        Sesion sesion = sesionRepository.findById(dto.getSesionId())
                .orElseThrow(() -> new RecursoNoEncontrado("Sesión no encontrada"));

        Optional<CarritoItem> itemExistente = carritoRepository.findByUsuarioAndSesion(usuario, sesion);

        if (itemExistente.isPresent()) {
            CarritoItem item = itemExistente.get();
            item.setCantidad(item.getCantidad() + dto.getCantidad());
            carritoRepository.save(item);
        } else {
            CarritoItem nuevoItem = new CarritoItem();
            nuevoItem.setUsuario(usuario);
            nuevoItem.setSesion(sesion);
            nuevoItem.setCantidad(dto.getCantidad());
            carritoRepository.save(nuevoItem);
        }
    }

    @Transactional
    public void eliminarDelCarrito(Long itemId) {
        carritoRepository.deleteById(itemId);
    }

    @Transactional
    public void vaciarCarrito(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado"));
        carritoRepository.deleteByUsuario(usuario);
    }

    @Transactional
    public void finalizarCompra(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado"));

        List<CarritoItem> items = carritoRepository.findByUsuario(usuario);

        if (items.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // Procesamos cada item como una compra independiente
        for (CarritoItem item : items) {
            CompraEntradasDTO compraDTO = new CompraEntradasDTO();
            compraDTO.setSesionId(item.getSesion().getId());
            compraDTO.setCantidad(item.getCantidad());
            
            compraService.realizarCompra(username, compraDTO);
        }

        // Una vez comprados todos, vaciamos el carrito
        carritoRepository.deleteByUsuario(usuario);
    }

    private CarritoItemDTO convertToDTO(CarritoItem item) {
        BigDecimal precioBase = item.getSesion().getPrecioBase();
        BigDecimal subtotal = precioBase.multiply(new BigDecimal(item.getCantidad()));

        return CarritoItemDTO.builder()
                .id(item.getId())
                .sesionId(item.getSesion().getId())
                .eventoTitulo(item.getSesion().getEvento().getTitulo())
                .imagenUrl(item.getSesion().getEvento().getImagenUrl())
                .fechaHora(item.getSesion().getFechaHora())
                .nombreSala(item.getSesion().getSala().getNombre())
                .nombreUbicacion(item.getSesion().getSala().getUbicacion().getCiudad())
                .precioBase(precioBase)
                .cantidad(item.getCantidad())
                .subtotal(subtotal)
                .build();
    }
}
