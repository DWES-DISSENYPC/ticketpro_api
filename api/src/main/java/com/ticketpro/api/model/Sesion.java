package com.ticketpro.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sesiones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaHora;
    private BigDecimal precioBase;
    private Integer entradasVendidas = 0;
    
    // 0:DISPONIBLE, 1:ULTIMAS, 2:AGOTADA, 3:CANCELADA
    private Integer estado = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id")
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id")
    private Sala sala;

    @OneToMany(mappedBy = "sesion")
    private List<Compra> compras;
}
