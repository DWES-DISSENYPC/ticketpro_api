package com.ticketpro.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ###### DEFINICION DE CLASE ###### */
// ------ Clase Que Representa Un Telefono En El Sistema ------
@Entity
@Table(name = "telefonos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Telefono {

    /* ###### ATRIBUTOS ###### */

    // ------ Identificador Unico Del Telefono ------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ------ Numero De Telefono ------
    @Column(nullable = false, length = 20)
    private String numero;

    // ------ Tipo De Telefono ------
    @Enumerated(EnumType.STRING)
    private TipoTelefono tipo = TipoTelefono.MOVIL;

    /* ###### RELACIONES ###### */

    // ------ Relacion Muchos A Uno Con Usuario ------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
