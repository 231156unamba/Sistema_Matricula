package com.unamba.matriculas.controller;

import com.unamba.matriculas.dto.MatriculaIngresanteRequest;
import com.unamba.matriculas.dto.MatriculaRequest;
import com.unamba.matriculas.model.Matricula;
import com.unamba.matriculas.service.MatriculaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matriculas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MatriculaController {
    
    private final MatriculaService matriculaService;
    
    @PostMapping("/ingresante")
    public ResponseEntity<MatriculaResponse> matricularIngresante(@Valid @RequestBody MatriculaIngresanteRequest request) {
        try {
            Matricula matricula = matriculaService.matricularIngresante(request);
            return ResponseEntity.ok(new MatriculaResponse(true, "Matrícula exitosa", matricula));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MatriculaResponse(false, e.getMessage(), null));
        }
    }
    
    @PostMapping("/regular")
    public ResponseEntity<MatriculaResponse> matricularRegular(@Valid @RequestBody MatriculaRequest request) {
        try {
            Matricula matricula = matriculaService.matricularRegular(request);
            return ResponseEntity.ok(new MatriculaResponse(true, "Matrícula exitosa", matricula));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MatriculaResponse(false, e.getMessage(), null));
        }
    }
    
    @Data
    @AllArgsConstructor
    static class MatriculaResponse {
        private boolean success;
        private String message;
        private Matricula matricula;
    }
}
