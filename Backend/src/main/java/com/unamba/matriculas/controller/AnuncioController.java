package com.unamba.matriculas.controller;

import com.unamba.matriculas.model.Anuncio;
import com.unamba.matriculas.service.AnuncioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anuncios")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AnuncioController {
    
    private final AnuncioService anuncioService;
    
    @GetMapping
    public ResponseEntity<List<Anuncio>> obtenerTodos() {
        return ResponseEntity.ok(anuncioService.obtenerTodos());
    }
    
    @GetMapping("/activos")
    public ResponseEntity<List<Anuncio>> obtenerActivos() {
        return ResponseEntity.ok(anuncioService.obtenerActivos());
    }
    
    @PostMapping
    public ResponseEntity<Anuncio> crear(@Valid @RequestBody Anuncio anuncio) {
        return ResponseEntity.ok(anuncioService.crear(anuncio));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Anuncio anuncio) {
        try {
            return ResponseEntity.ok(anuncioService.actualizar(id, anuncio));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        anuncioService.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
