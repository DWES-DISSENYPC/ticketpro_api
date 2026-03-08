package com.ticketpro.api.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateDTO {
   private String nombre;
    private String apellidos;
    private String email;
    private String direccion;
    private String ciudad;
    private String codigoPostal;
    private LocalDate fechaNacimiento;
    private String imagenPerfil; // URL o Base64
    private String metodoPagoPreferido;
    private List<String> telefonos; // Lista para soportar varios
}
