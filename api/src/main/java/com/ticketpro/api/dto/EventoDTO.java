package com.ticketpro.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ###### DTO DE EVENTO ###### */
// ------ Objeto Basico Para Listar Eventos Sin Sobrecargar La Red ------
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoDTO {

    /* ###### ATRIBUTOS ###### */

    // ------ Identificador Unico Del Evento ------
    private Long id;

    // ------ Titulo Principal Del Evento ------
    private String titulo;

    // ------ Breve Resumen De Lo Que Trata El Evento ------
    private String descripcion;

    // ------ Categoria A La Que Pertenece El Evento ------
    private String categoria;

    // ------ Url Con La Imagen Del Cartel Del Evento ------
    private String imagenUrl;

    // ------ Duracion Aproximada En Minutos ------
    private Integer duracionMinutos;

    // ------ Estado Actual Activo O Cancelado ------
    private String estado;

    // ------ Omitimos La Descripcion Larga Para La Lista General Si Queremos Ahorrar Ancho De Banda ------
}
