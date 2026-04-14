package com.ticketpro.api.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/* ###### DTO DE SESION DE EVENTO ###### */
// ------ Objeto Que Transfiere Los Datos Exclusivos De Una Sesion ------
@Data
public class SesionDTO {

    /* ###### ATRIBUTOS ###### */

    // ------ Identificador De La Sesion ------
    private Long id;

    // ------ Fecha Y Hora Programada ------
    private LocalDateTime fechaHora;

    // ------ Precio Original De Entrada ------
    private BigDecimal precioBase;

    // ------ Numero De Entradas Ya Vendidas ------
    private Integer entradasVendidas;

    // ------ Estado Operativo De La Sesion ------
    private Integer estado;
    
    /* ###### DATOS DE SALA APLANADOS ###### */
    // ------ Datos Aplanados Para Que Angular No Tenga Que Navegar Por Objetos Anidados ------

    // ------ Nombre Comercial De La Sala ------
    private String nombreSala;

    // ------ Capacidad Maxima Permitida ------
    private Integer capacidadSala;

    // ------ Nombre De La Ubicacion Fisica ------
    private String nombreUbicacion;

    // ------ Ciudad A La Que Pertenece ------
    private String ciudadUbicacion;
}
