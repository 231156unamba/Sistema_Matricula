package com.unamba.matriculas.controller;

import com.unamba.matriculas.model.Estudiante;
import com.unamba.matriculas.service.EstudianteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EstudianteController {
    
    private final EstudianteService estudianteService;
    
    @GetMapping
    public ResponseEntity<List<Estudiante>> obtenerTodos() {
        return ResponseEntity.ok(estudianteService.obtenerTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(estudianteService.obtenerPorId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<?> obtenerPorCodigo(@PathVariable String codigo) {
        try {
            return ResponseEntity.ok(estudianteService.obtenerPorCodigo(codigo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
        @PathVariable Long id, 
        @RequestParam Estudiante.EstadoEstudiante estado
    ) {
        try {
            return ResponseEntity.ok(estudianteService.actualizarEstado(id, estado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}/creditos")
    public ResponseEntity<?> actualizarCreditos(
        @PathVariable Long id, 
        @RequestParam Integer creditos
    ) {
        try {
            return ResponseEntity.ok(estudianteService.actualizarCreditos(id, creditos));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
