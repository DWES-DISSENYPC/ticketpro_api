package com.ticketpro.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "compras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String localizador; // Ej: TPRO-123456

    @Column(nullable = false)
    private Integer numEntradas;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPagado;

    private String metodoPago;

    @Enumerated(EnumType.STRING)
    private EstadoPago estadoPago = EstadoPago.PAGADA;

    private String qrCodeData;

    @Column(updatable = false)
    private LocalDateTime fechaCompra;

    private LocalDateTime updatedAt;

    // --- RELACIONES ---

    // Muchas compras pertenecen a un Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Muchas compras pueden ser para una misma Sesión
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesion_id", nullable = false)
    private Sesion sesion;

    // Una compra genera varios Tickets individuales
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    @PrePersist
    protected void onCreate() {
        fechaCompra = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

