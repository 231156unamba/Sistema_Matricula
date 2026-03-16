package com.unamba.matriculas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matriculas")
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_matricula")
    private Long idMatricula;
    
    @ManyToOne
    @JoinColumn(name = "id_estudiante")
    private Estudiante estudiante;
    
    @ManyToOne
    @JoinColumn(name = "id_periodo")
    private PeriodoAcademico periodo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoMatricula tipo;
    
    @Column(name = "fecha")
    private LocalDateTime fecha = LocalDateTime.now();
    
    public enum TipoMatricula {
        INGRESANTE, REGULAR
    }

    public Matricula() {}

    public Matricula(Long idMatricula, Estudiante estudiante, PeriodoAcademico periodo, TipoMatricula tipo, LocalDateTime fecha) {
        this.idMatricula = idMatricula;
        this.estudiante = estudiante;
        this.periodo = periodo;
        this.tipo = tipo;
        this.fecha = fecha;
    }

    public Long getIdMatricula() { return idMatricula; }
    public void setIdMatricula(Long idMatricula) { this.idMatricula = idMatricula; }
    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
    public PeriodoAcademico getPeriodo() { return periodo; }
    public void setPeriodo(PeriodoAcademico periodo) { this.periodo = periodo; }
    public TipoMatricula getTipo() { return tipo; }
    public void setTipo(TipoMatricula tipo) { this.tipo = tipo; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
