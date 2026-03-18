package com.unamba.matriculas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/recursos")
public class RecursoController {

    private final String BASE_STATIC_DIR = "src/main/resources/static/";

    @PostMapping("/malla/upload")
    public ResponseEntity<String> uploadMalla(@RequestParam("carrera") String carrera, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) return ResponseEntity.badRequest().body("Archivo vacío");
        
        try {
            String carreraNormalizada = normalizar(carrera);
            String filename = carreraNormalizada + ".pdf";
            Path uploadPath = Paths.get(BASE_STATIC_DIR, "mallas");
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok("Malla curricular subida correctamente");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al guardar el archivo");
        }
    }

    @PostMapping("/reglamento/upload")
    public ResponseEntity<String> uploadReglamento(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) return ResponseEntity.badRequest().body("Archivo vacío");

        try {
            String filename = "reglamento_general.pdf";
            Path uploadPath = Paths.get(BASE_STATIC_DIR, "reglamento");
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok("Reglamento subido correctamente");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al guardar el archivo");
        }
    }

    private String normalizar(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("")
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", "_");
    }
}
