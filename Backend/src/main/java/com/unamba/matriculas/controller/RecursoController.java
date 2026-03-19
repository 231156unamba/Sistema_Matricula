package com.unamba.matriculas.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;

@RestController
@RequestMapping("/api/recursos")
public class RecursoController {

    @Value("${recursos.static.dir}")
    private String staticDir;

    // ── UPLOAD ────────────────────────────────────────────────────────────────

    @PostMapping("/horario/upload")
    public ResponseEntity<String> uploadHorario(@RequestParam("carrera") String carrera,
                                                 @RequestParam("file") MultipartFile file) throws IOException {
        return guardar(file, "horarios", normalizar(carrera));
    }

    @PostMapping("/malla/upload")
    public ResponseEntity<String> uploadMalla(@RequestParam("carrera") String carrera,
                                               @RequestParam("file") MultipartFile file) throws IOException {
        return guardar(file, "mallas", normalizar(carrera));
    }

    @PostMapping("/reglamento/upload")
    public ResponseEntity<String> uploadReglamento(@RequestParam("file") MultipartFile file) throws IOException {
        return guardar(file, "reglamento", "reglamento_general");
    }

    @GetMapping("/carreras")
    public ResponseEntity<byte[]> getCarreras() throws IOException {
        Path file = Paths.get(staticDir, "carreras.json");
        if (!Files.exists(file)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Files.readAllBytes(file));
    }

    // ── DOWNLOAD ──────────────────────────────────────────────────────────────

    @GetMapping("/horario/{carrera:.+}")
    public ResponseEntity<byte[]> getHorario(@PathVariable String carrera) throws IOException {
        return leer("horarios", carrera);
    }

    @GetMapping("/malla/{carrera:.+}")
    public ResponseEntity<byte[]> getMalla(@PathVariable String carrera) throws IOException {
        return leer("mallas", carrera);
    }

    @GetMapping("/reglamento")
    public ResponseEntity<byte[]> getReglamento() throws IOException {
        return leer("reglamento", "reglamento_general");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private ResponseEntity<String> guardar(MultipartFile file, String carpeta, String nombre) throws IOException {
        if (file.isEmpty()) return ResponseEntity.badRequest().body("Archivo vacío");
        Path dir = Paths.get(staticDir, carpeta);
        Files.createDirectories(dir);
        Path dest = dir.resolve(nombre + ".pdf");
        if (Files.exists(dest)) Files.delete(dest);
        Files.copy(file.getInputStream(), dest);
        return ResponseEntity.ok("Archivo guardado: " + dest.toAbsolutePath());
    }

    private ResponseEntity<byte[]> leer(String carpeta, String nombre) throws IOException {
        Path dir = Paths.get(staticDir, carpeta);
        Path file = dir.resolve(nombre + ".pdf");
        System.out.println("[RecursoController] staticDir=" + staticDir);
        System.out.println("[RecursoController] Buscando: " + file.toAbsolutePath());
        System.out.println("[RecursoController] Existe: " + Files.exists(file));
        if (!Files.exists(file)) {
            // Listar archivos disponibles para debug
            if (Files.exists(dir)) {
                Files.list(dir).forEach(p -> System.out.println("[RecursoController] Disponible: " + p.getFileName()));
            } else {
                System.out.println("[RecursoController] Carpeta no existe: " + dir.toAbsolutePath());
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nombre + ".pdf\"")
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .contentType(MediaType.APPLICATION_PDF)
                .body(Files.readAllBytes(file));
    }

    private String normalizar(String text) {
        if (text == null) return "";
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase().trim()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "_");
    }
}
