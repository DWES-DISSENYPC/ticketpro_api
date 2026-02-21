package com.ticketpro.api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data // Genera getters, setters, toString, etc. automáticamente
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(unique = true, nullable = false, length = 20)
    private String dni;

    private LocalDate fechaNacimiento;

    private String metodoPagoPref;

    private String imagenUrl;

    @Enumerated(EnumType.STRING)
    private Rol rol = Rol.CLIENTE;

    private boolean activo = true;

    private LocalDateTime lastLogin;

    private String passwordResetToken;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // RELACIONES
    
    // 1 a 1 con Dirección (El usuario es el dueño de la relación)
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Direccion direccion;

    // 1 a muchos con Teléfonos
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Telefono> telefonos;

    // 1 a muchos con Compras
    @OneToMany(mappedBy = "usuario")
    private List<Compra> compras;

    // Métodos para auditoría automática
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

