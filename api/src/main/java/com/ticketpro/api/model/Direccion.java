package com.ticketpro.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "direcciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true, nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String calle;

    private String numero;
    
    private String pisoPuerta;

    private String codigoPostal;

    private String ciudad;

    private String provincia;

    private String pais = "España";
}
