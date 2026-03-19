package com.unamba.matriculas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "cursos")
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

    @Column(name = "carrera", length = 100)
    private String carrera;

    public Curso() {}

    public Curso(Long idCurso, String codigoCurso, String nombre, Integer creditos, Integer semestre) {
        this.idCurso = idCurso;
        this.codigoCurso = codigoCurso;
        this.nombre = nombre;
        this.creditos = creditos;
        this.semestre = semestre;
    }

    public Long getIdCurso() { return idCurso; }
    public void setIdCurso(Long idCurso) { this.idCurso = idCurso; }
    public String getCodigoCurso() { return codigoCurso; }
    public void setCodigoCurso(String codigoCurso) { this.codigoCurso = codigoCurso; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getCreditos() { return creditos; }
    public void setCreditos(Integer creditos) { this.creditos = creditos; }
    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }
    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }
}
