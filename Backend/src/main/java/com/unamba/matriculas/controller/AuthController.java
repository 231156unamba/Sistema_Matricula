package com.unamba.matriculas.controller;

import com.unamba.matriculas.dto.LoginRequest;
import com.unamba.matriculas.dto.AuthResult;
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
                return ResponseEntity.ok(new LoginResponse(false, "Debe completar el registro", null, null));
            }
            
            return ResponseEntity.ok(new LoginResponse(true, "Login exitoso", estudiante, authResult.getToken()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new LoginResponse(false, e.getMessage(), null, null));
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
            
            return ResponseEntity.ok(new LoginResponse(true, "Login exitoso", estudiante, authResult.getToken()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new LoginResponse(false, e.getMessage(), null, null));
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
            
            return ResponseEntity.ok(new LoginResponse(true, "Validación de matrícula exitosa", estudiante, authResult.getToken()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new LoginResponse(false, e.getMessage(), null, null));
        }
    }
    
    static class LoginResponse {
        private boolean success;
        private String message;
        private Estudiante estudiante;
        private String token;

        public LoginResponse() {}

        public LoginResponse(boolean success, String message, Estudiante estudiante, String token) {
            this.success = success;
            this.message = message;
            this.estudiante = estudiante;
            this.token = token;
        }

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Estudiante getEstudiante() { return estudiante; }
        public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}
