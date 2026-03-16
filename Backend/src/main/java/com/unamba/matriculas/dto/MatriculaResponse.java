package com.unamba.matriculas.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MatriculaResponse {
    private Long idMatricula;
    private EstudianteResponse estudiante;
    private PeriodoAcademicoResponse periodo;
    private String tipo;
    private LocalDateTime fecha;
    private List<DetalleMatriculaResponse> detalles;

    public MatriculaResponse() {}

    public MatriculaResponse(Long idMatricula, EstudianteResponse estudiante, PeriodoAcademicoResponse periodo, String tipo, LocalDateTime fecha, List<DetalleMatriculaResponse> detalles) {
        this.idMatricula = idMatricula;
        this.estudiante = estudiante;
        this.periodo = periodo;
        this.tipo = tipo;
        this.fecha = fecha;
        this.detalles = detalles;
    }

    public Long getIdMatricula() { return idMatricula; }
    public void setIdMatricula(Long idMatricula) { this.idMatricula = idMatricula; }
    public EstudianteResponse getEstudiante() { return estudiante; }
    public void setEstudiante(EstudianteResponse estudiante) { this.estudiante = estudiante; }
    public PeriodoAcademicoResponse getPeriodo() { return periodo; }
    public void setPeriodo(PeriodoAcademicoResponse periodo) { this.periodo = periodo; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public List<DetalleMatriculaResponse> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleMatriculaResponse> detalles) { this.detalles = detalles; }
    
    public static MatriculaResponse fromEntity(com.unamba.matriculas.model.Matricula matricula, List<DetalleMatriculaResponse> detalles) {
        MatriculaResponse response = new MatriculaResponse();
        response.setIdMatricula(matricula.getIdMatricula());
        response.setEstudiante(EstudianteResponse.fromEntity(matricula.getEstudiante()));
        response.setPeriodo(PeriodoAcademicoResponse.fromEntity(matricula.getPeriodo()));
        response.setTipo(matricula.getTipo().name());
        response.setFecha(matricula.getFecha());
        response.setDetalles(detalles);
        return response;
    }
}

class PeriodoAcademicoResponse {
    private Long idPeriodo;
    private Integer anio;
    private String semestre;
    private String estado;

    public PeriodoAcademicoResponse() {}

    public PeriodoAcademicoResponse(Long idPeriodo, Integer anio, String semestre, String estado) {
        this.idPeriodo = idPeriodo;
        this.anio = anio;
        this.semestre = semestre;
        this.estado = estado;
    }

    public Long getIdPeriodo() { return idPeriodo; }
    public void setIdPeriodo(Long idPeriodo) { this.idPeriodo = idPeriodo; }
    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }
    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public static PeriodoAcademicoResponse fromEntity(com.unamba.matriculas.model.PeriodoAcademico periodo) {
        PeriodoAcademicoResponse response = new PeriodoAcademicoResponse();
        response.setIdPeriodo(periodo.getIdPeriodo());
        response.setAnio(periodo.getAnio());
        response.setSemestre(periodo.getSemestre().name());
        response.setEstado(periodo.getEstado().name());
        return response;
    }
}

class DetalleMatriculaResponse {
    private Long idDetalle;
    private CursoResponse curso;
    private Integer vecesLlevado;
    private Double notaFinal;
    private String estado;

    public DetalleMatriculaResponse() {}

    public DetalleMatriculaResponse(Long idDetalle, CursoResponse curso, Integer vecesLlevado, Double notaFinal, String estado) {
        this.idDetalle = idDetalle;
        this.curso = curso;
        this.vecesLlevado = vecesLlevado;
        this.notaFinal = notaFinal;
        this.estado = estado;
    }

    public Long getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Long idDetalle) { this.idDetalle = idDetalle; }
    public CursoResponse getCurso() { return curso; }
    public void setCurso(CursoResponse curso) { this.curso = curso; }
    public Integer getVecesLlevado() { return vecesLlevado; }
    public void setVecesLlevado(Integer vecesLlevado) { this.vecesLlevado = vecesLlevado; }
    public Double getNotaFinal() { return notaFinal; }
    public void setNotaFinal(Double notaFinal) { this.notaFinal = notaFinal; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public static DetalleMatriculaResponse fromEntity(com.unamba.matriculas.model.DetalleMatricula detalle) {
        DetalleMatriculaResponse response = new DetalleMatriculaResponse();
        response.setIdDetalle(detalle.getIdDetalle());
        response.setCurso(CursoResponse.fromEntity(detalle.getCurso()));
        response.setVecesLlevado(detalle.getVecesLlevado());
        response.setNotaFinal(detalle.getNotaFinal() != null ? detalle.getNotaFinal().doubleValue() : null);
        response.setEstado(detalle.getEstado().name());
        return response;
    }
}

class CursoResponse {
    private Long idCurso;
    private String codigoCurso;
    private String nombre;
    private Integer creditos;
    private Integer semestre;

    public CursoResponse() {}

    public CursoResponse(Long idCurso, String codigoCurso, String nombre, Integer creditos, Integer semestre) {
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
    
    public static CursoResponse fromEntity(com.unamba.matriculas.model.Curso curso) {
        CursoResponse response = new CursoResponse();
        response.setIdCurso(curso.getIdCurso());
        response.setCodigoCurso(curso.getCodigoCurso());
        response.setNombre(curso.getNombre());
        response.setCreditos(curso.getCreditos());
        response.setSemestre(curso.getSemestre());
        return response;
    }
}
