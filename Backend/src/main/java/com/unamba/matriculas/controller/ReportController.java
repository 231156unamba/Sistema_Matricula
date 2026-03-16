package com.unamba.matriculas.controller;

import com.unamba.matriculas.model.*;
import com.unamba.matriculas.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReportController {
    
    private final ReportService reportService;
    
    @GetMapping("/estudiantes/estadisticas")
    public ResponseEntity<Map<String, Long>> obtenerEstadisticasEstudiantes() {
        return ResponseEntity.ok(reportService.obtenerEstadisticasEstudiantes());
    }
    
    @GetMapping("/matriculas/estadisticas")
    public ResponseEntity<Map<String, Long>> obtenerEstadisticasMatriculas() {
        return ResponseEntity.ok(reportService.obtenerEstadisticasMatriculas());
    }
    
    @GetMapping("/cursos/estadisticas")
    public ResponseEntity<Map<String, Long>> obtenerEstadisticasCursos() {
        return ResponseEntity.ok(reportService.obtenerEstadisticasCursos());
    }
    
    @GetMapping("/pagos/estadisticas")
    public ResponseEntity<Map<String, Long>> obtenerEstadisticasPagos() {
        return ResponseEntity.ok(reportService.obtenerEstadisticasPagos());
    }
    
    @GetMapping("/academicas/estadisticas")
    public ResponseEntity<Map<String, Long>> obtenerEstadisticasAcademicas() {
        return ResponseEntity.ok(reportService.obtenerEstadisticasAcademicas());
    }
    
    @GetMapping("/estudiantes/mas-desaprobados")
    public ResponseEntity<List<Estudiante>> obtenerEstudiantesConMasDesaprobados(
            @RequestParam(defaultValue = "10") int limite) {
        return ResponseEntity.ok(reportService.obtenerEstudiantesConMasDesaprobados(limite));
    }
    
    @GetMapping("/cursos/mas-demandados")
    public ResponseEntity<List<Curso>> obtenerCursosMasDemandados(
            @RequestParam(defaultValue = "10") int limite) {
        return ResponseEntity.ok(reportService.obtenerCursosMasDemandados(limite));
    }
    
    @GetMapping("/cursos/mas-desaprobados")
    public ResponseEntity<List<Curso>> obtenerCursosConMasDesaprobados(
            @RequestParam(defaultValue = "10") int limite) {
        return ResponseEntity.ok(reportService.obtenerCursosConMasDesaprobados(limite));
    }
    
    @GetMapping("/periodo-actual")
    public ResponseEntity<Map<String, Object>> obtenerReportePeriodoActual() {
        return ResponseEntity.ok(reportService.obtenerReportePeriodoActual());
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> obtenerDashboard() {
        Map<String, Object> dashboard = Map.of(
            "estudiantes", reportService.obtenerEstadisticasEstudiantes(),
            "matriculas", reportService.obtenerEstadisticasMatriculas(),
            "cursos", reportService.obtenerEstadisticasCursos(),
            "pagos", reportService.obtenerEstadisticasPagos(),
            "academicas", reportService.obtenerEstadisticasAcademicas(),
            "periodoActual", reportService.obtenerReportePeriodoActual()
        );
        return ResponseEntity.ok(dashboard);
    }
}
