package com.unamba.matriculas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "matriculas")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
