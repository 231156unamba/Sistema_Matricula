package com.unamba.matriculas.service;

import com.unamba.matriculas.model.*;
import com.unamba.matriculas.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    
    private final EstudianteRepository estudianteRepository;
    private final MatriculaRepository matriculaRepository;
    private final CursoRepository cursoRepository;
    private final DetalleMatriculaRepository detalleMatriculaRepository;
    private final PagoRepository pagoRepository;
    private final PeriodoAcademicoRepository periodoRepository;
    
    public Map<String, Long> obtenerEstadisticasEstudiantes() {
        long totalEstudiantes = estudianteRepository.count();
        long ingresantes = estudianteRepository.countByTipo(Estudiante.TipoEstudiante.INGRESANTE);
        long regulares = estudianteRepository.countByTipo(Estudiante.TipoEstudiante.REGULAR);
        long activos = estudianteRepository.findByEstado(Estudiante.EstadoEstudiante.ACTIVO).size();
        long inhabilitados = estudianteRepository.findByEstado(Estudiante.EstadoEstudiante.INHABILITADO).size();
        long retirados = estudianteRepository.findByEstado(Estudiante.EstadoEstudiante.RETIRADO).size();
        
        return Map.of(
            "total", totalEstudiantes,
            "ingresantes", ingresantes,
            "regulares", regulares,
            "activos", activos,
            "inhabilitados", inhabilitados,
            "retirados", retirados
        );
    }
    
    public Map<String, Long> obtenerEstadisticasMatriculas() {
        List<Matricula> matriculas = matriculaRepository.findAll();
        
        long matriculasIngresantes = matriculas.stream()
            .filter(m -> m.getTipo() == Matricula.TipoMatricula.INGRESANTE)
            .count();
        
        long matriculasRegulares = matriculas.stream()
            .filter(m -> m.getTipo() == Matricula.TipoMatricula.REGULAR)
            .count();
        
        return Map.of(
            "total", (long) matriculas.size(),
            "ingresantes", matriculasIngresantes,
            "regulares", matriculasRegulares
        );
    }
    
    public Map<String, Long> obtenerEstadisticasCursos() {
        List<Curso> cursos = cursoRepository.findAll();
        
        Map<Integer, Long> cursosPorSemestre = cursos.stream()
            .collect(Collectors.groupingBy(Curso::getSemestre, Collectors.counting()));
        
        long totalCursos = cursos.size();
        int totalCreditos = cursos.stream().mapToInt(Curso::getCreditos).sum();
        
        Map<String, Long> estadisticas = Map.of(
            "total", totalCursos,
            "totalCreditos", (long) totalCreditos
        );
        
        // Agregar cursos por semestre
        cursosPorSemestre.forEach((semestre, count) -> 
            estadisticas.put("semestre" + semestre, count));
        
        return estadisticas;
    }
    
    public Map<String, Long> obtenerEstadisticasPagos() {
        List<Pago> pagos = pagoRepository.findAll();
        
        long pagosValidados = pagos.stream()
            .filter(Pago::getValidado)
            .count();
        
        long pagosPendientes = pagos.size() - pagosValidados;
        
        Map<Pago.TipoPago, Long> pagosPorTipo = pagos.stream()
            .collect(Collectors.groupingBy(Pago::getTipoPago, Collectors.counting()));
        
        Map<String, Long> estadisticas = Map.of(
            "total", (long) pagos.size(),
            "validados", pagosValidados,
            "pendientes", pagosPendientes
        );
        
        // Agregar pagos por tipo
        pagosPorTipo.forEach((tipo, count) -> 
            estadisticas.put(tipo.name().toLowerCase(), count));
        
        return estadisticas;
    }
    
    public Map<String, Long> obtenerEstadisticasAcademicas() {
        List<DetalleMatricula> detalles = detalleMatriculaRepository.findAll();
        
        long cursosEnCurso = detalles.stream()
            .filter(d -> d.getEstado() == DetalleMatricula.EstadoCurso.EN_CURSO)
            .count();
        
        long cursosAprobados = detalles.stream()
            .filter(d -> d.getEstado() == DetalleMatricula.EstadoCurso.APROBADO)
            .count();
        
        long cursosDesaprobados = detalles.stream()
            .filter(d -> d.getEstado() == DetalleMatricula.EstadoCurso.DESAPROBADO)
            .count();
        
        return Map.of(
            "total", (long) detalles.size(),
            "enCurso", cursosEnCurso,
            "aprobados", cursosAprobados,
            "desaprobados", cursosDesaprobados
        );
    }
    
    public List<Estudiante> obtenerEstudiantesConMasDesaprobados(int limite) {
        return detalleMatriculaRepository.findAll().stream()
            .filter(d -> d.getEstado() == DetalleMatricula.EstadoCurso.DESAPROBADO)
            .collect(Collectors.groupingBy(d -> d.getMatricula().getEstudiante(), Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<Estudiante, Long>comparingByValue().reversed())
            .limit(limite)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }
    
    public List<Curso> obtenerCursosMasDemandados(int limite) {
        return detalleMatriculaRepository.findAll().stream()
            .collect(Collectors.groupingBy(DetalleMatricula::getCurso, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<Curso, Long>comparingByValue().reversed())
            .limit(limite)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }
    
    public List<Curso> obtenerCursosConMasDesaprobados(int limite) {
        return detalleMatriculaRepository.findAll().stream()
            .filter(d -> d.getEstado() == DetalleMatricula.EstadoCurso.DESAPROBADO)
            .collect(Collectors.groupingBy(DetalleMatricula::getCurso, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<Curso, Long>comparingByValue().reversed())
            .limit(limite)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }
    
    public Map<String, Object> obtenerReportePeriodoActual() {
        PeriodoAcademico periodoActual = periodoRepository.findByEstado(PeriodoAcademico.EstadoPeriodo.ABIERTO)
            .orElseThrow(() -> new RuntimeException("No hay período académico abierto"));
        
        List<Matricula> matriculasPeriodo = matriculaRepository.findByPeriodo_IdPeriodo(periodoActual.getIdPeriodo());
        List<DetalleMatricula> detallesPeriodo = matriculasPeriodo.stream()
            .flatMap(m -> detalleMatriculaRepository.findAll().stream()
                .filter(d -> d.getMatricula().getIdMatricula().equals(m.getIdMatricula())))
            .collect(Collectors.toList());
        
        long totalMatriculas = matriculasPeriodo.size();
        long matriculasIngresantes = matriculasPeriodo.stream()
            .filter(m -> m.getTipo() == Matricula.TipoMatricula.INGRESANTE)
            .count();
        
        long totalCursosMatriculados = detallesPeriodo.size();
        int totalCreditosPeriodo = detallesPeriodo.stream()
            .mapToInt(d -> d.getCurso().getCreditos())
            .sum();
        
        return Map.of(
            "periodo", Map.of(
                "id", periodoActual.getIdPeriodo(),
                "anio", periodoActual.getAnio(),
                "semestre", periodoActual.getSemestre().name()
            ),
            "matriculas", Map.of(
                "total", totalMatriculas,
                "ingresantes", matriculasIngresantes,
                "regulares", totalMatriculas - matriculasIngresantes
            ),
            "cursos", Map.of(
                "totalMatriculados", totalCursosMatriculados,
                "totalCreditos", totalCreditosPeriodo
            )
        );
    }
}
