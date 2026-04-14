package com.ticketpro.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/* ###### DEFINICION DE CLASE ###### */
// ------ Entidad Que Representa A Un Usuario Del Sistema ------
@Entity
@Table(name = "usuarios")
@Data // ------ Genera Getters Setters Y ToString Automaticamente ------
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    /* ###### ATRIBUTOS ###### */

    // ------ Identificador Unico Del Usuario ------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ------ Nombre De Usuario Para El Login ------
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    // ------ Contraseña Encriptada Del Usuario ------
    @Column(nullable = false)
    private String password;

    // ------ Correo Electronico Del Usuario ------
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    // ------ Nombre De Pila Del Usuario ------
    @Column(nullable = false, length = 50)
    private String nombre;

    // ------ Apellidos Del Usuario ------
    @Column(nullable = false, length = 100)
    private String apellidos;

    // ------ Documento De Identidad Del Usuario ------
    @Column(unique = true, nullable = false, length = 20)
    private String dni;

    // ------ Fecha De Nacimiento Del Usuario ------
    private LocalDate fechaNacimiento;

    // ------ Metodo De Pago Preferido Por Defecto ------
    private String metodoPagoPref;

    // ------ Url De La Imagen De Perfil ------
    private String imagenUrl;

    // ------ Rol Que Tiene El Usuario En La Plataforma ------
    @Enumerated(EnumType.STRING)
    private Rol rol = Rol.CLIENTE;

    // ------ Indica Si La Cuenta Esta Activa ------
    private boolean activo = true;

    // ------ Fecha Del Ultimo Acceso Exitoso ------
    private LocalDateTime lastLogin;

    // ------ Token Para Restablecer La Contraseña ------
    private String passwordResetToken;

    // ------ Fecha En Que Se Creo La Cuenta ------
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // ------ Fecha En Que Se Actualizo El Perfil Por Ultima Vez ------
    private LocalDateTime updatedAt;

    /* ###### RELACIONES ###### */
    
    // ------ Relacion Uno A Uno Con Direccion ------
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Direccion direccion;

    // ------ Relacion Uno A Muchos Con Telefonos ------
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Telefono> telefonos;

    // ------ Relacion Uno A Muchos Con Compras ------
    @OneToMany(mappedBy = "usuario")
    private List<Compra> compras;

    /* ###### METODOS DE CICLO DE VIDA ###### */

    // ------ Metodo Para Auditoria Automatica Al Crear ------
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    // ------ Metodo Para Auditoria Automatica Al Actualizar ------
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
