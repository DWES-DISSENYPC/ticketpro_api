package com.ticketpro.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ticketpro.api.model.Rol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ###### DTO DE PERFIL COMPLETO DE USUARIO ###### */
// ------ Contiene Absolutamente Todos Los Datos Necesarios Para Rellenar Perfil Angular ------
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPerfilDTO {

    /* ###### DATOS PERSONALES Y CUENTA ###### */

    // ------ Nombre De Usuario Exclusivo ------
    private String username;

    // ------ Nombre De Pila ------
    private String nombre;

    // ------ Apellidos Del Usuario ------
    private String apellidos;

    // ------ Correo Electronico ------
    private String email;

    // ------ Documento De Identidad Dni ------
    private String dni;

    // ------ Privilegios Dentro Del Sistema ------
    private Rol rol;

    // ------ Fecha Del Primer Resgistro ------
    private LocalDateTime createdAt;

    // ------ Fecha De Nacimiento ------
    private LocalDate fechaNacimiento;

    // ------ Imagen Del Perfil Si Existe ------
    private String imagenUrl;

    // ------ Preferencia De Pago Del Usuario ------
    private String metodoPagoPref;
    
    /* ###### DIRECCION APLANADA ###### */
    // ------ Datos De Direccion Aplanados O Como Objeto ------

    // ------ Nombre Principal De La Calle ------
    private String calle;

    // ------ Nombre De Ciudad ------
    private String ciudad;

    // ------ Numero De Codigo Postal ------
    private String codigoPostal;

    // ------ Numero Del Edificio O Casa ------
    private String numero;

    // ------ Piso Puerta O Indicacion Interna ------
    private String pisoPuerta;

    // ------ Nombre De Provincia Autonoma ------
    private String provincia;

    // ------ Nombre Del Pais ------
    private String pais;

    /* ###### TELEFONOS ASOCIADOS ###### */

    // ------ Lista Integrada De Todos Sus Telefonos ------
    private List<TelefonoDTO> telefonos;

}
