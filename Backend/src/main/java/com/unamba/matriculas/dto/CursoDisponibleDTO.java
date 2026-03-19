package com.unamba.matriculas.dto;

public class CursoDisponibleDTO {
    private Long idCurso;
    private String codigoCurso;
    private String nombre;
    private Integer creditos;
    private Integer semestre;
    private Integer vecesDesaprobado; // cuántas veces lo jaló
    private boolean esRepeticion;

    public CursoDisponibleDTO() {}

    public CursoDisponibleDTO(Long idCurso, String codigoCurso, String nombre,
                               Integer creditos, Integer semestre,
                               Integer vecesDesaprobado) {
        this.idCurso = idCurso;
        this.codigoCurso = codigoCurso;
        this.nombre = nombre;
        this.creditos = creditos;
        this.semestre = semestre;
        this.vecesDesaprobado = vecesDesaprobado;
        this.esRepeticion = vecesDesaprobado > 0;
    }

    public Long getIdCurso() { return idCurso; }
    public String getCodigoCurso() { return codigoCurso; }
    public String getNombre() { return nombre; }
    public Integer getCreditos() { return creditos; }
    public Integer getSemestre() { return semestre; }
    public Integer getVecesDesaprobado() { return vecesDesaprobado; }
    public boolean isEsRepeticion() { return esRepeticion; }
}
