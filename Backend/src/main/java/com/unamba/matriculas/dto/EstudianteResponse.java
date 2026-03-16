package com.unamba.matriculas.dto;

import java.time.LocalDateTime;

public class EstudianteResponse {
    private Long idEstudiante;
    private String codigoEstudiante;
    private String dni;
    private String nombres;
    private String apellidos;
    private String tipo;
    private String estado;
    private Integer creditosMaximos;
    private LocalDateTime fechaRegistro;

    public EstudianteResponse() {}

    public EstudianteResponse(Long idEstudiante, String codigoEstudiante, String dni, String nombres, String apellidos, String tipo, String estado, Integer creditosMaximos, LocalDateTime fechaRegistro) {
        this.idEstudiante = idEstudiante;
        this.codigoEstudiante = codigoEstudiante;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.tipo = tipo;
        this.estado = estado;
        this.creditosMaximos = creditosMaximos;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(Long idEstudiante) { this.idEstudiante = idEstudiante; }
    public String getCodigoEstudiante() { return codigoEstudiante; }
    public void setCodigoEstudiante(String codigoEstudiante) { this.codigoEstudiante = codigoEstudiante; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Integer getCreditosMaximos() { return creditosMaximos; }
    public void setCreditosMaximos(Integer creditosMaximos) { this.creditosMaximos = creditosMaximos; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    public static EstudianteResponse fromEntity(com.unamba.matriculas.model.Estudiante estudiante) {
        EstudianteResponse response = new EstudianteResponse();
        response.setIdEstudiante(estudiante.getIdEstudiante());
        response.setCodigoEstudiante(estudiante.getCodigoEstudiante());
        response.setDni(estudiante.getDni());
        response.setNombres(estudiante.getNombres());
        response.setApellidos(estudiante.getApellidos());
        response.setTipo(estudiante.getTipo().name());
        response.setEstado(estudiante.getEstado().name());
        response.setCreditosMaximos(estudiante.getCreditosMaximos());
        response.setFechaRegistro(estudiante.getFechaRegistro());
        return response;
    }
}
