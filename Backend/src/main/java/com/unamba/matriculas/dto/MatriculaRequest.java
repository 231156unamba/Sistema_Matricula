package com.unamba.matriculas.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class MatriculaRequest {
    @NotNull(message = "El ID del período es obligatorio")
    private Long idPeriodo;
    
    @NotEmpty(message = "Debe seleccionar al menos un curso")
    private List<Long> idCursos;
    
    @jakarta.validation.constraints.NotBlank(message = "El voucher es obligatorio")
    private String voucher;
    
    @NotNull(message = "El ID del estudiante es obligatorio")
    private Long idEstudiante;

    public MatriculaRequest() {}

    public MatriculaRequest(Long idPeriodo, List<Long> idCursos, String voucher, Long idEstudiante) {
        this.idPeriodo = idPeriodo;
        this.idCursos = idCursos;
        this.voucher = voucher;
        this.idEstudiante = idEstudiante;
    }

    public Long getIdPeriodo() { return idPeriodo; }
    public void setIdPeriodo(Long idPeriodo) { this.idPeriodo = idPeriodo; }
    public List<Long> getIdCursos() { return idCursos; }
    public void setIdCursos(List<Long> idCursos) { this.idCursos = idCursos; }
    public String getVoucher() { return voucher; }
    public void setVoucher(String voucher) { this.voucher = voucher; }
    public Long getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(Long idEstudiante) { this.idEstudiante = idEstudiante; }
}
