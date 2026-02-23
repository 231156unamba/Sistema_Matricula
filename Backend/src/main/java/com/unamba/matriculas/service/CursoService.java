package com.unamba.matriculas.service;

import com.unamba.matriculas.model.Curso;
import com.unamba.matriculas.model.DetalleMatricula;
import com.unamba.matriculas.model.Prerrequisito;
import com.unamba.matriculas.repository.CursoRepository;
import com.unamba.matriculas.repository.DetalleMatriculaRepository;
import com.unamba.matriculas.repository.PrerrequisitoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CursoService {
    
    private final CursoRepository cursoRepository;
    private final PrerrequisitoRepository prerrequisitoRepository;
    private final DetalleMatriculaRepository detalleMatriculaRepository;
    
    public List<Curso> obtenerTodos() {
        return cursoRepository.findAll();
    }
    
    public List<Curso> obtenerPorSemestre(Integer semestre) {
        return cursoRepository.findBySemestre(semestre);
    }
    
    public List<Curso> obtenerCursosDisponibles(Long idEstudiante, Integer semestreActual) {
        List<DetalleMatricula> cursosAprobados = detalleMatriculaRepository
            .findCursosAprobadosByEstudiante(idEstudiante);
        
        List<Long> idsAprobados = cursosAprobados.stream()
            .map(d -> d.getCurso().getIdCurso())
            .collect(Collectors.toList());
        
        return cursoRepository.findAll().stream()
            .filter(curso -> curso.getSemestre() >= semestreActual)
            .filter(curso -> !idsAprobados.contains(curso.getIdCurso()))
            .filter(curso -> cumplePrerrequisitos(curso.getIdCurso(), idsAprobados))
            .collect(Collectors.toList());
    }
    
    private boolean cumplePrerrequisitos(Long idCurso, List<Long> idsAprobados) {
        List<Prerrequisito> prerrequisitos = prerrequisitoRepository.findByCurso_IdCurso(idCurso);
        
        if (prerrequisitos.isEmpty()) {
            return true;
        }
        
        return prerrequisitos.stream()
            .allMatch(prereq -> idsAprobados.contains(prereq.getCursoPrerrequisito().getIdCurso()));
    }
    
    public Curso crear(Curso curso) {
        return cursoRepository.save(curso);
    }
}
