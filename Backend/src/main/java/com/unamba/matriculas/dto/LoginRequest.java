package com.unamba.matriculas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "El identificador es obligatorio")
    private String identificador; // DNI o código de estudiante
    
    @NotBlank(message = "El voucher es obligatorio")
    private String voucher;
}
