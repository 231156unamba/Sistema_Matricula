package com.unamba.matriculas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "prerrequisitos")
public class Prerrequisito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Curso curso;
    
    @ManyToOne
    @JoinColumn(name = "id_curso_prerrequisito")
    private Curso cursoPrerrequisito;

    public Prerrequisito() {}

    public Prerrequisito(Long id, Curso curso, Curso cursoPrerrequisito) {
        this.id = id;
        this.curso = curso;
        this.cursoPrerrequisito = cursoPrerrequisito;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }
    public Curso getCursoPrerrequisito() { return cursoPrerrequisito; }
    public void setCursoPrerrequisito(Curso cursoPrerrequisito) { this.cursoPrerrequisito = cursoPrerrequisito; }
}
