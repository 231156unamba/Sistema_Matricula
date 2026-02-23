package com.unamba.matriculas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "prerrequisitos")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
