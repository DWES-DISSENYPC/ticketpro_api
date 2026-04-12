package com.ticketpro.api.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "eventos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoria;
    private String titulo;

    @Column(length = 1000)
    private String descripcion;
    private String imagenUrl;
    private Integer duracionMinutos;
    private String estado;
    private LocalDate fecha;

    @Column(unique = true) // Importante: para que no haya dos iguales en la BD
    private String ticketmasterId;
}
