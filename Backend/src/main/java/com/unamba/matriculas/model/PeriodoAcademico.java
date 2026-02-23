package com.unamba.matriculas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "periodos_academicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeriodoAcademico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_periodo")
    private Long idPeriodo;
    
    @NotNull(message = "El año es obligatorio")
    @Min(value = 2020, message = "El año debe ser mayor o igual a 2020")
    @Column(name = "anio", nullable = false)
    private Integer anio;
    
    @NotNull(message = "El semestre es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "semestre", nullable = false)
    private Semestre semestre;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoPeriodo estado = EstadoPeriodo.ABIERTO;
    
    public enum Semestre {
        I, II
    }
    
    public enum EstadoPeriodo {
        ABIERTO, CERRADO
    }
}
