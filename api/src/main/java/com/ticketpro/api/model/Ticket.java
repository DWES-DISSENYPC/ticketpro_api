package com.ticketpro.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/* ###### DEFINICION DE CLASE ###### */
// ------ Entidad Que Representa Un Ticket De Entrada Al Evento ------
@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    /* ###### ATRIBUTOS ###### */

    // ------ Identificador Unico Del Ticket ------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ------ El Hash Unico Para El Codigo Qr ------
    @Column(unique = true, nullable = false)
    private String codigoQr; 

    // ------ Estado Actual Del Ticket ------
    @Enumerated(EnumType.STRING)
    private EstadoTicket estadoTicket = EstadoTicket.VALIDO;

    // ------ Se Llena Cuando Entran Al Evento ------
    private LocalDateTime fechaValidacion;

    /* ###### RELACIONES ###### */

    // ------ Muchos Tickets Pertenecen A Una Sola Compra ------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compra_id", nullable = false)
    private Compra compra;
}
