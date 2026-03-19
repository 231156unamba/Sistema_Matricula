package com.unamba.matriculas.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unamba.matriculas.dto.LoginRequest;
import com.unamba.matriculas.dto.AuthResult;
import com.unamba.matriculas.model.Administrador;
import com.unamba.matriculas.model.Estudiante;
import com.unamba.matriculas.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/login/ingresante")
    public ResponseEntity<LoginResponse> loginIngresante(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResult authResult = authService.loginIngresante(
                request.getIdentificador(), 
                request.getVoucher()
            );
            Estudiante estudiante = (Estudiante) authResult.getUser();
            
            if (estudiante == null) {
                return ResponseEntity.ok(new LoginResponse(false, "Debe completar el registro", null, null, null));
            }
            
            return ResponseEntity.ok(new LoginResponse(true, "Login exitoso", estudiante, null, authResult.getToken()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new LoginResponse(false, e.getMessage(), null, null, null));
        }
    }
    
    @PostMapping("/login/regular")
    public ResponseEntity<LoginResponse> loginRegular(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResult authResult = authService.loginRegular(
                request.getIdentificador(), 
                request.getVoucher()
            );
            Estudiante estudiante = (Estudiante) authResult.getUser();
            
            return ResponseEntity.ok(new LoginResponse(true, "Login exitoso", estudiante, null, authResult.getToken()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new LoginResponse(false, e.getMessage(), null, null, null));
        }
    }

    @PostMapping("/login/matricula-regular")
    public ResponseEntity<LoginResponse> loginMatriculaRegular(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResult authResult = authService.loginMatriculaRegular(
                request.getIdentificador(), 
                request.getVoucher()
            );
            Estudiante estudiante = (Estudiante) authResult.getUser();
            
            return ResponseEntity.ok(new LoginResponse(true, "Validación de matrícula exitosa", estudiante, null, authResult.getToken()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new LoginResponse(false, e.getMessage(), null, null, null));
        }
    }

    @PostMapping("/login/admin")
    public ResponseEntity<LoginResponse> loginAdmin(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResult authResult = authService.loginAdmin(
                request.getIdentificador(), 
                request.getVoucher()
            );
            Administrador admin = (Administrador) authResult.getUser();
            
            return ResponseEntity.ok(new LoginResponse(true, "Login admin exitoso", null, admin, authResult.getToken()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new LoginResponse(false, e.getMessage(), null, null, null));
        }
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginResponse {
        @JsonProperty("success")
        private boolean success;
        
        @JsonProperty("message")
        private String message;
        
        @JsonProperty("estudiante")
        private Estudiante estudiante;
        
        @JsonProperty("administrador")
        private Administrador administrador;
        
        @JsonProperty("token")
        private String token;
    }
}
