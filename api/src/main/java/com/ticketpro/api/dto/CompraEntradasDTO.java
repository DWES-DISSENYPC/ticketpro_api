package com.ticketpro.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ###### DTO DE COMPRA DE ENTRADAS ###### */
// ------ Representa La Peticion De Compra Con Los Datos Necessarios ------
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompraEntradasDTO {

    /* ###### ATRIBUTOS ###### */

    // ------ Identificador De La Sesion Seleccionada ------
    private Long sesionId;

    // ------ Cantidad De Entradas A Comprar ------
    private Integer cantidad;
}
