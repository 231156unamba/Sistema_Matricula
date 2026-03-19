package com.unamba.matriculas.service;

import com.unamba.matriculas.dto.CursoDisponibleDTO;
import com.unamba.matriculas.dto.InfoMatriculaDTO;
import com.unamba.matriculas.model.*;
import com.unamba.matriculas.repository.*;
import com.unamba.matriculas.dto.MatriculaIngresanteRequest;
import com.unamba.matriculas.dto.MatriculaRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatriculaService {
    
    private final MatriculaRepository matriculaRepository;
    private final EstudianteRepository estudianteRepository;
    private final CursoRepository cursoRepository;
    private final DetalleMatriculaRepository detalleMatriculaRepository;
    private final PeriodoAcademicoRepository periodoRepository;
    private final PrerrequisitoRepository prerrequisitoRepository;
    private final PagoRepository pagoRepository;
    private final DocumentoIngresanteRepository documentoRepository;
    
    public List<Curso> obtenerCursosIngresante(Long idEstudiante) throws Exception {
        estudianteRepository.findById(idEstudiante)
            .orElseThrow(() -> new Exception("Estudiante no encontrado"));
        return cursoRepository.findBySemestre(1);
    }

    public InfoMatriculaDTO obtenerInfoMatricula(Long idEstudiante) throws Exception {
        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
            .orElseThrow(() -> new Exception("Estudiante no encontrado"));

        List<DetalleMatricula> historial = detalleMatriculaRepository.findByEstudiante(idEstudiante);

        // Aprobado = estado APROBADO o nota >= 10.5 → NO mostrar
        List<Long> aprobadosIds = historial.stream()
            .filter(d -> d.getEstado() == DetalleMatricula.EstadoCurso.APROBADO
                || (d.getNotaFinal() != null && d.getNotaFinal().compareTo(NOTA_APROBATORIA) >= 0))
            .map(d -> d.getCurso().getIdCurso())
            .distinct()
            .collect(Collectors.toList());

        // Desaprobados: estado DESAPROBADO o nota < 10.5 (con nota)
        java.util.Map<Long, Long> desaprobadosPorCurso = historial.stream()
            .filter(d -> d.getEstado() == DetalleMatricula.EstadoCurso.DESAPROBADO
                || (d.getNotaFinal() != null && d.getNotaFinal().compareTo(NOTA_APROBATORIA) < 0))
            .collect(Collectors.groupingBy(d -> d.getCurso().getIdCurso(), Collectors.counting()));

        // ¿Tiene algún curso jalado 2+ veces? → límite 14 créditos
        boolean tieneJaladoDosVeces = desaprobadosPorCurso.values().stream().anyMatch(v -> v >= 2);

        // Semestre actual = semestre más alto que el estudiante tiene en su historial
        // (ya sea EN_CURSO sin nota, APROBADO, o DESAPROBADO)
        int semestreActual = historial.stream()
            .mapToInt(d -> d.getCurso().getSemestre())
            .max()
            .orElse(1);

        // Límite superior: semestre actual + 2
        int semestreMaximo = semestreActual + 2;

        // Cursos de la carrera del estudiante (o comunes)
        String carreraEstudiante = (estudiante.getCarrera() != null && !estudiante.getCarrera().isBlank())
            ? estudiante.getCarrera() : null;

        List<Curso> cursosCandidatos = carreraEstudiante != null
            ? cursoRepository.findByCarreraOrComun(carreraEstudiante)
            : cursoRepository.findAll();

        List<CursoDisponibleDTO> disponibles = cursosCandidatos.stream()
            // No mostrar aprobados
            .filter(c -> !aprobadosIds.contains(c.getIdCurso()))
            // No mostrar cursos de semestre superior al límite
            .filter(c -> c.getSemestre() <= semestreMaximo)
            // Prerrequisitos cumplidos (solo APROBADO cuenta como prereq)
            .filter(c -> validarPrerrequisitos(idEstudiante, c.getIdCurso()))
            .map(c -> new CursoDisponibleDTO(
                c.getIdCurso(), c.getCodigoCurso(), c.getNombre(), c.getCreditos(), c.getSemestre(),
                desaprobadosPorCurso.getOrDefault(c.getIdCurso(), 0L).intValue()
            ))
            .collect(Collectors.toList());

        // Promedio ponderado: solo cursos con nota final
        List<DetalleMatricula> conNota = historial.stream()
            .filter(d -> d.getNotaFinal() != null)
            .collect(Collectors.toList());

        Double promedio = null;
        if (!conNota.isEmpty()) {
            double sumaProductos = conNota.stream()
                .mapToDouble(d -> d.getNotaFinal().doubleValue() * d.getCurso().getCreditos())
                .sum();
            int totalCreditos = conNota.stream().mapToInt(d -> d.getCurso().getCreditos()).sum();
            if (totalCreditos > 0) {
                promedio = BigDecimal.valueOf(sumaProductos / totalCreditos)
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();
            }
        }

        int creditosMax = tieneJaladoDosVeces ? 14 : estudiante.getCreditosMaximos();
        int creditosMin = tieneJaladoDosVeces ? 0 : 12;

        InfoMatriculaDTO info = new InfoMatriculaDTO();
        info.setCursosDisponibles(disponibles);
        info.setCreditosMinimos(creditosMin);
        info.setCreditosMaximos(creditosMax);
        info.setPromedioSemestreAnterior(promedio);
        info.setTieneJaladoDosVeces(tieneJaladoDosVeces);
        return info;
    }

    public List<Curso> obtenerCursosDisponibles(Long idEstudiante) throws Exception {
        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
            .orElseThrow(() -> new Exception("Estudiante no encontrado"));
        
        List<Curso> todosLosCursos = cursoRepository.findAll();
        List<DetalleMatricula> historial = detalleMatriculaRepository.findByEstudiante(idEstudiante);
        
        List<Long> cursosAprobadosIds = historial.stream()
            .filter(d -> d.getEstado() == DetalleMatricula.EstadoCurso.APROBADO)
            .map(d -> d.getCurso().getIdCurso())
            .collect(Collectors.toList());
            
        List<Long> cursosEnCursoIds = historial.stream()
            .filter(d -> d.getEstado() == DetalleMatricula.EstadoCurso.EN_CURSO)
            .map(d -> d.getCurso().getIdCurso())
            .collect(Collectors.toList());

        return todosLosCursos.stream()
            .filter(curso -> !cursosAprobadosIds.contains(curso.getIdCurso()))
            .filter(curso -> !cursosEnCursoIds.contains(curso.getIdCurso()))
            .filter(curso -> validarPrerrequisitos(idEstudiante, curso.getIdCurso()))
            .collect(Collectors.toList());
    }

    @Transactional
    public Matricula matricularIngresante(MatriculaIngresanteRequest request) throws Exception {
        pagoRepository.findByVoucherAndValidadoTrue(request.getVoucher())
            .orElseThrow(() -> new Exception("Voucher no válido"));
        
        // Verificar si ya existe un estudiante con ese DNI (ya se matriculó antes)
        if (estudianteRepository.findByDni(request.getDni()).isPresent()) {
            throw new Exception("Ya existe un ingresante registrado con ese DNI.");
        }

        Estudiante estudiante = new Estudiante();
        estudiante.setDni(request.getDni());
        estudiante.setNombres(request.getNombres());
        estudiante.setApellidos(request.getApellidos());
        estudiante.setTipo(Estudiante.TipoEstudiante.INGRESANTE);
        estudiante.setCodigoEstudiante(generarCodigoEstudiante());
        estudiante = estudianteRepository.save(estudiante);
        
        DocumentoIngresante doc = new DocumentoIngresante();
        doc.setEstudiante(estudiante);
        doc.setDeclaracionJurada(request.getDeclaracionJurada());
        doc.setCertificadoEstudios(request.getCertificadoEstudios());
        doc.setBoletaMatricula(request.getBoletaMatricula());
        doc.setBoletaExamen(request.getBoletaExamen());
        doc.setPagoCentroMedico(request.getPagoCentroMedico());
        doc.setHojaMatricula(request.getHojaMatricula());
        documentoRepository.save(doc);
        
        PeriodoAcademico periodo = periodoRepository.findByEstado(PeriodoAcademico.EstadoPeriodo.ABIERTO)
            .orElseThrow(() -> new Exception("No hay periodo académico abierto"));
        
        Matricula matricula = new Matricula();
        matricula.setEstudiante(estudiante);
        matricula.setPeriodo(periodo);
        matricula.setTipo(Matricula.TipoMatricula.INGRESANTE);
        matricula = matriculaRepository.save(matricula);
        
        List<Curso> cursosPrimerSemestre = cursoRepository.findBySemestre(1);
        for (Curso curso : cursosPrimerSemestre) {
            DetalleMatricula detalle = new DetalleMatricula();
            detalle.setMatricula(matricula);
            detalle.setCurso(curso);
            detalle.setEstado(DetalleMatricula.EstadoCurso.EN_CURSO);
            detalleMatriculaRepository.save(detalle);
        }
        
        return matricula;
    }
    
    @Transactional
    public Matricula matricularRegular(MatriculaRequest request) throws Exception {
        pagoRepository.findByVoucherAndValidadoTrue(request.getVoucher())
            .orElseThrow(() -> new Exception("Voucher no válido"));
        
        Estudiante estudiante = estudianteRepository.findById(request.getIdEstudiante())
            .orElseThrow(() -> new Exception("Estudiante no encontrado"));
        
        if (estudiante.getEstado() != Estudiante.EstadoEstudiante.ACTIVO) {
            throw new Exception("Estudiante no está activo");
        }
        
        PeriodoAcademico periodo = periodoRepository.findByEstado(PeriodoAcademico.EstadoPeriodo.ABIERTO)
            .orElseThrow(() -> new Exception("No hay periodo académico abierto"));

        // Verificar que no esté ya matriculado en este periodo
        if (matriculaRepository.findByEstudiante_IdEstudianteAndPeriodo_IdPeriodo(
                estudiante.getIdEstudiante(), periodo.getIdPeriodo()).isPresent()) {
            throw new Exception("Ya estás matriculado en el periodo académico actual.");
        }
        
        int creditosTotales = 0;
        for (Long idCurso : request.getIdCursos()) {
            Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new Exception("Curso no encontrado"));
            creditosTotales += curso.getCreditos();
            
            if (!validarPrerrequisitos(estudiante.getIdEstudiante(), idCurso)) {
                throw new Exception("No cumple con los prerrequisitos del curso: " + curso.getNombre());
            }
            
            List<DetalleMatricula> historial = detalleMatriculaRepository
                .findByEstudianteAndCurso(estudiante.getIdEstudiante(), idCurso);
            
            long vecesDesaprobado = historial.stream()
                .filter(d -> d.getEstado() == DetalleMatricula.EstadoCurso.DESAPROBADO)
                .count();
            
            if (vecesDesaprobado >= 4) {
                throw new Exception("Ha desaprobado el curso 4 veces. No puede continuar.");
            }
            
            if (vecesDesaprobado == 3 && request.getIdCursos().size() > 1) {
                throw new Exception("Solo puede llevar el curso que ha jalado 3 veces");
            }
        }
        
        if (creditosTotales > estudiante.getCreditosMaximos()) {
            throw new Exception("Excede el límite de créditos permitidos: " + estudiante.getCreditosMaximos());
        }
        
        Matricula matricula = new Matricula();
        matricula.setEstudiante(estudiante);
        matricula.setPeriodo(periodo);
        matricula.setTipo(Matricula.TipoMatricula.REGULAR);
        matricula = matriculaRepository.save(matricula);
        
        for (Long idCurso : request.getIdCursos()) {
            Curso curso = cursoRepository.findById(idCurso).get();
            List<DetalleMatricula> historial = detalleMatriculaRepository
                .findByEstudianteAndCurso(estudiante.getIdEstudiante(), idCurso);
            
            DetalleMatricula detalle = new DetalleMatricula();
            detalle.setMatricula(matricula);
            detalle.setCurso(curso);
            detalle.setVecesLlevado(historial.size() + 1);
            detalle.setEstado(DetalleMatricula.EstadoCurso.EN_CURSO);
            detalleMatriculaRepository.save(detalle);
        }
        
        return matricula;
    }
    
    private static final BigDecimal NOTA_APROBATORIA = new BigDecimal("10.5");

    private boolean validarPrerrequisitos(Long idEstudiante, Long idCurso) {
        List<Prerrequisito> prerrequisitos = prerrequisitoRepository.findByCurso_IdCurso(idCurso);
        if (prerrequisitos.isEmpty()) return true;

        List<DetalleMatricula> historial = detalleMatriculaRepository.findByEstudiante(idEstudiante);
        // Prerrequisito cumplido solo si fue APROBADO o tiene nota >= 10.5
        List<Long> aprobadosIds = historial.stream()
            .filter(d -> d.getEstado() == DetalleMatricula.EstadoCurso.APROBADO
                || (d.getNotaFinal() != null && d.getNotaFinal().compareTo(NOTA_APROBATORIA) >= 0))
            .map(d -> d.getCurso().getIdCurso())
            .distinct()
            .collect(Collectors.toList());

        return prerrequisitos.stream()
            .allMatch(prereq -> aprobadosIds.contains(prereq.getCursoPrerrequisito().getIdCurso()));
    }
    
    /**
     * Genera el código de estudiante siguiendo el formato AASNNNN:
     * - AA: Últimos 2 dígitos del año (ej: 23 para 2023)
     * - S: Semestre (1 = primera mitad del año, 2 = segunda mitad)
     * - NNNN: Número correlativo de 4 dígitos
     * 
     * Ejemplos: 231156, 222115
     */
    private String generarCodigoEstudiante() {
        int year = java.time.Year.now().getValue();
        int month = java.time.LocalDate.now().getMonthValue();
        
        // Determinar semestre: 1-6 = semestre 1, 7-12 = semestre 2
        int semestre = (month <= 6) ? 1 : 2;
        
        // Obtener últimos 2 dígitos del año
        String yearStr = String.valueOf(year).substring(2);
        
        // Contar estudiantes del mismo año y semestre para generar correlativo
        String prefijo = yearStr + semestre;
        long count = estudianteRepository.countByCodigoEstudianteStartingWith(prefijo);
        
        // Generar código: AASNNNN
        return String.format("%s%s%04d", yearStr, semestre, count + 1);
    }
}
