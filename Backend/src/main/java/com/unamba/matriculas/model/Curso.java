package com.unamba.matriculas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Long idCurso;
    
    @NotBlank(message = "El código del curso es obligatorio")
    @Size(max = 10)
    @Column(name = "codigo_curso", unique = true, nullable = false, length = 10)
    private String codigoCurso;
    
    @NotBlank(message = "El nombre del curso es obligatorio")
    @Size(max = 100)
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @NotNull(message = "Los créditos son obligatorios")
    @Min(value = 1, message = "Los créditos deben ser al menos 1")
    @Max(value = 6, message = "Los créditos no pueden exceder 6")
    @Column(name = "creditos", nullable = false)
    private Integer creditos;
    
    @NotNull(message = "El semestre es obligatorio")
    @Min(value = 1, message = "El semestre debe ser al menos 1")
    @Max(value = 10, message = "El semestre no puede exceder 10")
    @Column(name = "semestre", nullable = false)
    private Integer semestre;
}
