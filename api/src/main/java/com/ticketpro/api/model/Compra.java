package com.ticketpro.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/* ###### DEFINICION DE CLASE ###### */
// ------ Clase Que Representa Una Compra En El Sistema ------
@Entity
@Table(name = "compras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compra {

    /* ###### ATRIBUTOS ###### */

    // ------ Identificador Unico De La Compra ------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ------ Localizador De La Compra ------
    @Column(unique = true, nullable = false, length = 20)
    private String localizador; // Ej: TPRO-123456

    // ------ Numero De Entradas Compradas ------
    @Column(nullable = false)
    private Integer numEntradas;

    // ------ Precio Unitario De Cada Entrada ------
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    // ------ Total Pagado Por La Compra ------
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPagado;

    // ------ Metodo De Pago Utilizado ------
    private String metodoPago;

    // ------ Estado Del Pago ------
    @Enumerated(EnumType.STRING)
    private EstadoPago estadoPago = EstadoPago.PAGADA;

    // ------ Datos Del Codigo Qr ------
    private String qrCodeData;

    // ------ Fecha De La Compra ------
    @Column(updatable = false)
    private LocalDateTime fechaCompra;

    // ------ Fecha De Ultima Actualizacion ------
    private LocalDateTime updatedAt;

    /* ###### RELACIONES ###### */

    // ------ Usuario Que Realizo La Compra ------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // ------ Sesion A La Que Pertenece La Compra ------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesion_id", nullable = false)
    private Sesion sesion;

    // ------ Lista De Tickets Generados Por Esta Compra ------
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    /* ###### METODOS DE CICLO DE VIDA ###### */

    // ------ Se Ejecuta Antes De Persistir La Entidad Por Primera Vez ------
    @PrePersist
    protected void onCreate() {
        fechaCompra = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    // ------ Se Ejecuta Antes De Actualizar La Entidad ------
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
