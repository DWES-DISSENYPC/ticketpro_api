package com.ticketpro.api.dto;

import lombok.Data;

@Data
public class CompraDTO {
    private Long sesionId;
    private Integer cantidad; // Número de entradas que quiere comprar
}