package com.ticketpro.api.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ###### DEFINICION DE CLASE ###### */
// ------ Clase Que Representa Un Evento En El Sistema ------
@Entity
@Table(name = "eventos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evento {

    /* ###### ATRIBUTOS ###### */

    // ------ Identificador Unico Del Evento ------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ------ Categoria Del Evento ------
    private String categoria;

    // ------ Titulo Del Evento ------
    private String titulo;

    // ------ Descripcion Detallada Del Evento ------
    @Column(length = 1000)
    private String descripcion;

    // ------ Url De La Imagen Del Evento ------
    private String imagenUrl;

    // ------ Duracion En Minutos Del Evento ------
    private Integer duracionMinutos;

    // ------ Estado Actual Del Evento ------
    private String estado;

    // ------ Fecha Del Evento ------
    private LocalDate fecha;

    // ------ Identificador Unico En Ticketmaster Para Evitar Duplicidad ------
    @Column(unique = true)
    private String ticketmasterId;
}
