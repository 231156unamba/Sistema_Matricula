package com.unamba.matriculas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "periodos_academicos")
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

    public Long getIdPeriodo() { return idPeriodo; }
    public void setIdPeriodo(Long idPeriodo) { this.idPeriodo = idPeriodo; }
    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }
    public Semestre getSemestre() { return semestre; }
    public void setSemestre(Semestre semestre) { this.semestre = semestre; }
    public EstadoPeriodo getEstado() { return estado; }
    public void setEstado(EstadoPeriodo estado) { this.estado = estado; }
}
