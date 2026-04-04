package com.ticketpro.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ticketpro.api.model.Rol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPerfilDTO {
    private String username;
    private String nombre;
    private String apellidos;
    private String email;
    private String dni;
    private Rol rol;
    private LocalDateTime createdAt;
    private LocalDate fechaNacimiento;
    private String imagenUrl;
    private String metodoPagoPref;
    
    // Datos de dirección aplanados o como objeto
    private String calle;
    private String ciudad;
    private String codigoPostal;
    private String numero;
    private String pisoPuerta;
    private String provincia;
    private String pais;

    
    private List<TelefonoDTO> telefonos;




}
