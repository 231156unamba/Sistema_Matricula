package com.unamba.matriculas.controller;

import com.unamba.matriculas.dto.LoginRequest;
import com.unamba.matriculas.model.Estudiante;
import com.unamba.matriculas.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/login/ingresante")
    public ResponseEntity<LoginResponse> loginIngresante(@Valid @RequestBody LoginRequest request) {
        try {
            Estudiante estudiante = authService.loginIngresante(
                request.getIdentificador(), 
                request.getVoucher()
            );
            
            if (estudiante == null) {
                return ResponseEntity.ok(new LoginResponse(false, "Debe completar el registro", null));
            }
            
            return ResponseEntity.ok(new LoginResponse(true, "Login exitoso", estudiante));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new LoginResponse(false, e.getMessage(), null));
        }
    }
    
    @PostMapping("/login/regular")
    public ResponseEntity<LoginResponse> loginRegular(@Valid @RequestBody LoginRequest request) {
        try {
            Estudiante estudiante = authService.loginRegular(
                request.getIdentificador(), 
                request.getVoucher()
            );
            
            return ResponseEntity.ok(new LoginResponse(true, "Login exitoso", estudiante));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new LoginResponse(false, e.getMessage(), null));
        }
    }
    
    @Data
    @AllArgsConstructor
    static class LoginResponse {
        private boolean success;
        private String message;
        private Estudiante estudiante;
    }
}
