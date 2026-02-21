package com.ticketpro.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "eventos")
@Data
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoria;
    private String titulo;
    private String descripcion;
    private String imagenUrl;
    private Integer duracionMinutos;
    private String estado;
}
