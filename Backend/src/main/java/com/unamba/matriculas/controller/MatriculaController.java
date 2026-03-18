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

import com.unamba.matriculas.model.Curso;
import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
@RequiredArgsConstructor
public class MatriculaController {
    
    private final MatriculaService matriculaService;
    
    @GetMapping("/cursos-disponibles/{idEstudiante}")
    public ResponseEntity<List<Curso>> obtenerCursosDisponibles(@PathVariable Long idEstudiante) {
        try {
            return ResponseEntity.ok(matriculaService.obtenerCursosDisponibles(idEstudiante));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
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
    
    static class MatriculaResponse {
        private boolean success;
        private String message;
        private Matricula matricula;

        public MatriculaResponse() {}

        public MatriculaResponse(boolean success, String message, Matricula matricula) {
            this.success = success;
            this.message = message;
            this.matricula = matricula;
        }

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Matricula getMatricula() { return matricula; }
        public void setMatricula(Matricula matricula) { this.matricula = matricula; }
    }
}
