package com.unamba.matriculas.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "El identificador es obligatorio")
    private String identificador; // DNI o código de estudiante
    
    @NotBlank(message = "El voucher es obligatorio")
    private String voucher;

    public LoginRequest() {}

    public LoginRequest(String identificador, String voucher) {
        this.identificador = identificador;
        this.voucher = voucher;
    }

    public String getIdentificador() { return identificador; }
    public void setIdentificador(String identificador) { this.identificador = identificador; }
    public String getVoucher() { return voucher; }
    public void setVoucher(String voucher) { this.voucher = voucher; }
}
