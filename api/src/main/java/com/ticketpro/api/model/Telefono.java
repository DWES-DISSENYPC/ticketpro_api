package com.ticketpro.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "telefonos")
@Data
public class Telefono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String numero;

    @Enumerated(EnumType.STRING)
    private TipoTelefono tipo = TipoTelefono.MOVIL;

    // Relación Muchos a Uno con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}


