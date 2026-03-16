package com.unamba.matriculas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "estudiantes")
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiante")
    private Long idEstudiante;
    
    @Column(name = "codigo_estudiante", unique = true, length = 20)
    private String codigoEstudiante;
    
    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 dígitos")
    @Column(name = "dni", nullable = false, length = 8)
    private String dni;
    
    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100)
    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;
    
    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100)
    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;
    
    @NotNull(message = "El tipo de estudiante es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoEstudiante tipo;
    
    @Column(name = "carrera")
    private String carrera;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoEstudiante estado = EstadoEstudiante.ACTIVO;
    
    @Min(value = 0, message = "Los créditos no pueden ser negativos")
    @Max(value = 30, message = "Los créditos no pueden exceder 30")
    @Column(name = "creditos_maximos")
    private Integer creditosMaximos = 23;
    
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    
    public enum TipoEstudiante {
        INGRESANTE, REGULAR
    }
    
    public enum EstadoEstudiante {
        ACTIVO, INHABILITADO, RETIRADO
    }

    public Estudiante() {}

    public Estudiante(Long idEstudiante, String codigoEstudiante, String dni, String nombres, String apellidos, TipoEstudiante tipo, EstadoEstudiante estado, Integer creditosMaximos, LocalDateTime fechaRegistro, String carrera) {
        this.idEstudiante = idEstudiante;
        this.codigoEstudiante = codigoEstudiante;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.tipo = tipo;
        this.estado = estado;
        this.creditosMaximos = creditosMaximos;
        this.fechaRegistro = fechaRegistro;
        this.carrera = carrera;
    }

    public Long getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(Long idEstudiante) { this.idEstudiante = idEstudiante; }
    public String getCodigoEstudiante() { return codigoEstudiante; }
    public void setCodigoEstudiante(String codigoEstudiante) { this.codigoEstudiante = codigoEstudiante; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public TipoEstudiante getTipo() { return tipo; }
    public void setTipo(TipoEstudiante tipo) { this.tipo = tipo; }
    public EstadoEstudiante getEstado() { return estado; }
    public void setEstado(EstadoEstudiante estado) { this.estado = estado; }
    public Integer getCreditosMaximos() { return creditosMaximos; }
    public void setCreditosMaximos(Integer creditosMaximos) { this.creditosMaximos = creditosMaximos; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }
}
