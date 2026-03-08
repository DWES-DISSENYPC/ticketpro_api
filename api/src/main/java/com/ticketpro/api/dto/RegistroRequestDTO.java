package com.ticketpro.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroRequestDTO {
    private String username;
    private String password;
    private String email;
    private String nombre;
    private String apellidos;
}
