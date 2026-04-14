package com.ticketpro.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ###### DEFINICION DE CLASE ###### */
// ------ Entidad Que Representa Una Sala Donde Ocurren Los Eventos ------
@Entity
@Table(name = "salas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sala {

    /* ###### ATRIBUTOS ###### */

    // ------ Identificador Unico De La Sala ------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ------ Nombre De La Sala ------
    private String nombre;

    // ------ Capacidad Maxima De La Sala ------
    private Integer capacidad;

    /* ###### RELACIONES ###### */

    // ------ Ubicacion Fisica De La Sala ------
    @ManyToOne
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;
}
