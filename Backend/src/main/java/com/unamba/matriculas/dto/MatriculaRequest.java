package com.unamba.matriculas.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaRequest {
    @NotNull(message = "El ID del estudiante es obligatorio")
    private Long idEstudiante;
    
    @NotBlank(message = "El voucher es obligatorio")
    private String voucher;
    
    @NotEmpty(message = "Debe seleccionar al menos un curso")
    private List<Long> idCursos;
}
