package com.unamba.matriculas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "administradores")
public class Administrador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin")
    private Long idAdmin;
    
    @NotBlank(message = "El usuario es obligatorio")
    @Size(min = 4, max = 50, message = "El usuario debe tener entre 4 y 50 caracteres")
    @Column(unique = true, nullable = false)
    private String usuario;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(nullable = false)
    private String password;
    
    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100)
    @Column(nullable = false)
    private String nombres;
    
    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100)
    @Column(nullable = false)
    private String apellidos;
    
    @Email(message = "Email inválido")
    @Size(max = 100)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolAdmin rol = RolAdmin.ADMIN;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    public enum RolAdmin {
        SUPER_ADMIN,
        ADMIN,
        REGISTRADOR
    }

    public Administrador() {
    }

    public Administrador(Long idAdmin, String usuario, String password, String nombres, String apellidos, String email, RolAdmin rol, Boolean activo, LocalDateTime fechaCreacion) {
        this.idAdmin = idAdmin;
        this.usuario = usuario;
        this.password = password;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.rol = rol;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Long idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RolAdmin getRol() {
        return rol;
    }

    public void setRol(RolAdmin rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
