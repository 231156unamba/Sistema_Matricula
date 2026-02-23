package com.unamba.matriculas.service;

import com.unamba.matriculas.model.*;
import com.unamba.matriculas.repository.*;
import com.unamba.matriculas.dto.MatriculaIngresanteRequest;
import com.unamba.matriculas.dto.MatriculaRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    
    @Transactional
    public Matricula matricularIngresante(MatriculaIngresanteRequest request) throws Exception {
        pagoRepository.findByVoucherAndValidadoTrue(request.getVoucher())
            .orElseThrow(() -> new Exception("Voucher no válido"));
        
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
    
    private boolean validarPrerrequisitos(Long idEstudiante, Long idCurso) {
        List<Prerrequisito> prerrequisitos = prerrequisitoRepository.findByCurso_IdCurso(idCurso);
        
        if (prerrequisitos.isEmpty()) {
            return true;
        }
        
        List<DetalleMatricula> cursosAprobados = detalleMatriculaRepository
            .findCursosAprobadosByEstudiante(idEstudiante);
        
        return prerrequisitos.stream()
            .allMatch(prereq -> cursosAprobados.stream()
                .anyMatch(d -> d.getCurso().getIdCurso().equals(prereq.getCursoPrerrequisito().getIdCurso())));
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
