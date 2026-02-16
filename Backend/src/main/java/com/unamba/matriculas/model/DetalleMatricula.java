package com.unamba.matriculas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_matriculas")
public class DetalleMatricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // TODO: Agregar campos de detalle de matrícula
}
