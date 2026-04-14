package com.ticketpro.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ###### DEFINICION DE CLASE ###### */
// ------ Clase Que Representa Una Direccion En El Sistema ------
@Entity
@Table(name = "direcciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Direccion {

    /* ###### ATRIBUTOS ###### */
    // ------ Identificador Unico De La Direccion ------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ###### RELACIONES ###### */
    // ------ Usuario Al Que Pertenece La Direccion ------
    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true, nullable = false)
    private Usuario usuario;

    /* ###### ATRIBUTOS DE UBICACION ###### */
    // ------ Nombre De La Calle ------
    @Column(nullable = false)
    private String calle;

    // ------ Numero Del Edificio ------
    private String numero;
    
    // ------ Piso O Puerta ------
    private String pisoPuerta;

    // ------ Codigo Postal ------
    private String codigoPostal;

    // ------ Ciudad De Residencia ------
    private String ciudad;

    // ------ Provincia De Residencia ------
    private String provincia;

    // ------ Pais De Residencia ------
    private String pais = "España";
}
