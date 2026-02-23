package com.unamba.matriculas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "estudiantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
