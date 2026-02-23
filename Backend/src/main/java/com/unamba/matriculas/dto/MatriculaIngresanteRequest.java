package com.unamba.matriculas.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaIngresanteRequest {
    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 dígitos")
    private String dni;
    
    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;
    
    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;
    
    @NotBlank(message = "El voucher es obligatorio")
    private String voucher;
    
    @NotBlank(message = "La declaración jurada es obligatoria")
    private String declaracionJurada;
    
    @NotBlank(message = "El certificado de estudios es obligatorio")
    private String certificadoEstudios;
    
    @NotBlank(message = "La boleta de matrícula es obligatoria")
    private String boletaMatricula;
    
    @NotBlank(message = "La boleta de examen es obligatoria")
    private String boletaExamen;
    
    @NotBlank(message = "El pago de centro médico es obligatorio")
    private String pagoCentroMedico;
    
    @NotBlank(message = "La hoja de matrícula es obligatoria")
    private String hojaMatricula;
}
