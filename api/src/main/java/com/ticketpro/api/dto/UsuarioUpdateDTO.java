package com.ticketpro.api.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ###### DTO DE ACTUALIZACION DE PERFIL ###### */
// ------ Usado Especificamente Para Recibir Un Update O Patch Desde Angular ------
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateDTO {

    /* ###### DATOS MODIFICABLES ###### */

    // ------ Nombre Personalizado Actualizado ------
    private String nombre;

    // ------ Apellido Modificado ------
    private String apellidos;

    // ------ Nuevo Correo Para Contacto ------
    private String email;

    // ------ Correcion De Dni ------
    private String dni;

    // ------ Fecha De Nacimiento ------
    private LocalDate fechaNacimiento;

    /* ###### DIRECCION COMPLETA ###### */

    // ------ Nombre Especifico De La Calle ------
    private String calle;

    // ------ Cifra O Numero Del Edificio ------
    private String numero;

    // ------ Detalles Internos Nomenclatura ------
    private String pisoPuerta;

    // ------ Pueblo O Ciudad Donde Reside ------
    private String ciudad;

    // ------ Provincia Adscrita ------
    private String provincia;

    // ------ Codigo De Correos Postal ------
    private String codigoPostal;

    // ------ Estado Nacion ------
    private String pais;
    
    /* ###### DATOS MULTIMEDIA Y PREFERENCIAS ###### */

    // ------ Ruta Actualizada De Su Foto De Perfil ------
    private String imagenUrl;

    // ------ Tarjeta O Medio Que Prefiere Por Defecto ------
    private String metodoPagoPreferido;

    /* ###### LISTA TELEFONOS ###### */

    // ------ Repositorio Reemplazado Para Lista Entera ------
    private List<TelefonoDTO> telefonos;
}
