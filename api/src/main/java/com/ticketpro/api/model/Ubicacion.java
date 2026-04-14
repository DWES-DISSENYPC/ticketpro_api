package com.ticketpro.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ###### DEFINICION DE CLASE ###### */
// ------ Representa La Ubicacion Y Direccion Fisica De Una Sala O Evento ------
@Entity
@Table(name = "ubicaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ubicacion {

    /* ###### ATRIBUTOS ###### */

    // ------ Identificador Unico De La Ubicacion ------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ------ Nombre Del Recinto O Ubicacion ------
    private String nombre;

    // ------ Nombre De La Calle ------
    private String calle;

    // ------ Numero Del Inmueble ------
    private String numero;

    // ------ Ciudad De La Ubicacion ------
    private String ciudad;

    // ------ Provincia De La Ubicacion ------
    private String provincia;

    // ------ Codigo Postal ------
    private String cp;
}
