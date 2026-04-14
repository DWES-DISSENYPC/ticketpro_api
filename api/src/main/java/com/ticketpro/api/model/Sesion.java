package com.ticketpro.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/* ###### DEFINICION DE CLASE ###### */
// ------ Representa Una Sesion Especifica De Un Evento ------
@Entity
@Table(name = "sesiones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sesion {

    /* ###### ATRIBUTOS ###### */

    // ------ Identificador Unico De La Sesion ------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ------ Fecha Y Hora De La Sesion ------
    private LocalDateTime fechaHora;

    // ------ Precio Base De Las Entradas Para La Sesion ------
    private BigDecimal precioBase;

    // ------ Cantidad De Entradas Ya Vendidas ------
    private Integer entradasVendidas = 0;
    
    // ------ Estado De La Sesion (0:Disponible, 1:Ultimas, 2:Agotada, 3:Cancelada) ------
    private Integer estado = 0;

    /* ###### RELACIONES ###### */

    // ------ Evento Al Que Pertenece Esta Sesion ------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id")
    private Evento evento;

    // ------ Sala Donde Se Celebra La Sesion ------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id")
    private Sala sala;

    // ------ Compras Realizadas En Esta Sesion ------
    @OneToMany(mappedBy = "sesion")
    private List<Compra> compras;
}
