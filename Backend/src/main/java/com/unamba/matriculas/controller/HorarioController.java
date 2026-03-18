package com.unamba.matriculas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.text.Normalizer;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

    private final String UPLOAD_DIR = "src/main/resources/static/horarios/";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadHorario(@RequestParam("carrera") String carrera, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo está vacío.");
        }

        try {
            // Normalizar el nombre de la carrera para que coincida con el frontend
            String normalized = Normalizer.normalize(carrera, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            String carreraSinTildes = pattern.matcher(normalized).replaceAll("");
            String carreraNormalizada = carreraSinTildes.toLowerCase().trim().replaceAll("\\s+", "_");
            
            String filename = carreraNormalizada + ".pdf";
            Path filePath = Paths.get(UPLOAD_DIR, filename);

            // Renombrar el archivo antiguo si existe
            if (Files.exists(filePath)) {
                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String oldFilename = carreraNormalizada + "_" + timeStamp + ".pdf";
                Path oldFilePath = Paths.get(UPLOAD_DIR, oldFilename);
                Files.move(filePath, oldFilePath);
            }

            // Guardar el nuevo archivo
            Files.copy(file.getInputStream(), filePath);

            return ResponseEntity.ok().body("Horario subido y actualizado correctamente.");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al subir el archivo: " + e.getMessage());
        }
    }
}
