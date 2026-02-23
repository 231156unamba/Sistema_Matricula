package com.unamba.matriculas.controller;

import com.unamba.matriculas.model.Curso;
import com.unamba.matriculas.service.CursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CursoController {
    
    private final CursoService cursoService;
    
    @GetMapping
    public ResponseEntity<List<Curso>> obtenerTodos() {
        return ResponseEntity.ok(cursoService.obtenerTodos());
    }
    
    @GetMapping("/semestre/{semestre}")
    public ResponseEntity<List<Curso>> obtenerPorSemestre(@PathVariable Integer semestre) {
        return ResponseEntity.ok(cursoService.obtenerPorSemestre(semestre));
    }
    
    @GetMapping("/disponibles/{idEstudiante}/{semestre}")
    public ResponseEntity<List<Curso>> obtenerDisponibles(
        @PathVariable Long idEstudiante,
        @PathVariable Integer semestre
    ) {
        return ResponseEntity.ok(cursoService.obtenerCursosDisponibles(idEstudiante, semestre));
    }
    
    @PostMapping
    public ResponseEntity<Curso> crear(@Valid @RequestBody Curso curso) {
        return ResponseEntity.ok(cursoService.crear(curso));
    }
}
