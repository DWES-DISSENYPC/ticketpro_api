package com.ticketpro.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ticketpro.api.dto.CompraEntradasDTO;
import com.ticketpro.api.dto.DetalleCompraDTO;
import com.ticketpro.api.dto.HistorialCompraDTO;
import com.ticketpro.api.dto.MensajeResponseDTO;
import com.ticketpro.api.service.CompraService;

/* ###### CONTROLADOR DE COMPRAS ###### */
// ------ Punto De Entrada Para Todas Las Operaciones Relacionadas Con Las Compras ------
@RestController
@RequestMapping("/api/compras")
public class CompraController {

    /* ###### DEPENDENCIAS INYECTADAS ###### */

    // ------ Servicio Para Procesar La Logica De Compras ------
    @Autowired
    private CompraService compraService;

    /* ###### ENDPOINTS DE OPERACIONES DE COMPRA ###### */

    // ------ Endpoint Para Realizar Una Nueva Compra ------
    @PostMapping
    public ResponseEntity<String> realizarCompra(
            @RequestBody CompraEntradasDTO compraDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        // ------ El UserDetails Contiene El Username Jwt ------
        String username = userDetails.getUsername();

        // ------ Llamamos Al Servicio Pasando El Email En Lugar Del Id ------
        compraService.realizarCompra(username, compraDTO);

        return ResponseEntity.ok("Compra realizada con éxito. ¡Entradas reservadas!");
    }

    /* ###### ENDPOINTS DE CONSULTAS DE COMPRA ###### */

    // ------ Endpoint Para La Tabla General Del Historial De Compras ------
    @GetMapping("/mis-compras")
    public ResponseEntity<List<HistorialCompraDTO>> listarMisCompras(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(compraService.obtenerHistorial(userDetails.getUsername()));
    }

    // ------ Endpoint Para El Boton Ver Detalle De Una Compra Especifica ------
    @GetMapping("/{id}")
    public ResponseEntity<DetalleCompraDTO> verDetalle(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(compraService.obtenerDetalle(id, userDetails.getUsername()));
    }

    /* ###### ENDPOINTS DE CANCELACION ###### */

    // ------ Endpoint Para Cancelar Una Compra ------
    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<MensajeResponseDTO> cancelar(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        compraService.cancelarCompra(id, userDetails.getUsername());
        return ResponseEntity
                .ok(new MensajeResponseDTO("Compra cancelada correctamente. El importe será devuelto a su tarjeta."));
    }

    /* ###### ENDPOINTS PARA ADMINISTRACION ###### */

    // ------ Endpoint Para Obtener Las Compras Pendientes Del Usuario Autenticado ------
    @GetMapping("/pendientes")
    public ResponseEntity<List<DetalleCompraDTO>> listarPendientes(
            @RequestParam(required = false) Long usuarioId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Long idBusqueda = usuarioId;
        
        // Si no se pasa un ID, buscamos el del propio usuario autenticado
        if (idBusqueda == null && userDetails != null) {
            idBusqueda = compraService.obtenerIdUsuarioPorUsername(userDetails.getUsername());
        }

        if (idBusqueda == null) {
            return ResponseEntity.badRequest().build();
        }

        List<DetalleCompraDTO> pendientes = compraService.obtenerComprasPendientes(idBusqueda);
        
        if (pendientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(pendientes);
    }

    @GetMapping(value = "/ticket/{codigo}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] obtenerQR(@PathVariable String codigo) {
        return compraService.obtenerImagenQR(codigo);
    }

    @GetMapping(value = "/ticket/{id}/descargar", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] descargarTicket(@PathVariable Long id) {
        return compraService.obtenerTicketCompleto(id);
    }
}
