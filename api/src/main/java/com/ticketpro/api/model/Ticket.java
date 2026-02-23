package com.ticketpro.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigoQr; // El hash único para el QR

    @Enumerated(EnumType.STRING)
    private EstadoTicket estadoTicket = EstadoTicket.VALIDO;

    private LocalDateTime fechaValidacion; // Se llena cuando entran al evento

    // RELACIÓN: Muchos tickets pertenecen a una sola compra
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compra_id", nullable = false)
    private Compra compra;
}

enum EstadoTicket {
    VALIDO, USADO, ANULADO
}
