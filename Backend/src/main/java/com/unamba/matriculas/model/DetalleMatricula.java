package com.unamba.matriculas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_matricula")
public class DetalleMatricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;
    
    @ManyToOne
    @JoinColumn(name = "id_matricula")
    private Matricula matricula;
    
    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Curso curso;
    
    @Min(value = 1, message = "Veces llevado debe ser al menos 1")
    @Max(value = 4, message = "No puede llevar un curso más de 4 veces")
    @Column(name = "veces_llevado")
    private Integer vecesLlevado = 1;
    
    @DecimalMin(value = "0.0", message = "La nota no puede ser negativa")
    @DecimalMax(value = "20.0", message = "La nota no puede exceder 20")
    @Column(name = "nota_final", precision = 4, scale = 2)
    private BigDecimal notaFinal;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoCurso estado;
    
    public enum EstadoCurso {
        EN_CURSO, APROBADO, DESAPROBADO
    }

    public DetalleMatricula() {}

    public DetalleMatricula(Long idDetalle, Matricula matricula, Curso curso, Integer vecesLlevado, BigDecimal notaFinal, EstadoCurso estado) {
        this.idDetalle = idDetalle;
        this.matricula = matricula;
        this.curso = curso;
        this.vecesLlevado = vecesLlevado;
        this.notaFinal = notaFinal;
        this.estado = estado;
    }

    public Long getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Long idDetalle) { this.idDetalle = idDetalle; }
    public Matricula getMatricula() { return matricula; }
    public void setMatricula(Matricula matricula) { this.matricula = matricula; }
    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }
    public Integer getVecesLlevado() { return vecesLlevado; }
    public void setVecesLlevado(Integer vecesLlevado) { this.vecesLlevado = vecesLlevado; }
    public BigDecimal getNotaFinal() { return notaFinal; }
    public void setNotaFinal(BigDecimal notaFinal) { this.notaFinal = notaFinal; }
    public EstadoCurso getEstado() { return estado; }
    public void setEstado(EstadoCurso estado) { this.estado = estado; }
}
