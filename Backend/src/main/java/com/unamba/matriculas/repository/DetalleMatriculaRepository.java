package com.unamba.matriculas.repository;

import com.unamba.matriculas.model.DetalleMatricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetalleMatriculaRepository extends JpaRepository<DetalleMatricula, Long> {
    @Query("SELECT d FROM DetalleMatricula d WHERE d.matricula.estudiante.idEstudiante = :idEstudiante AND d.curso.idCurso = :idCurso")
    List<DetalleMatricula> findByEstudianteAndCurso(Long idEstudiante, Long idCurso);
    
    @Query("SELECT d FROM DetalleMatricula d WHERE d.matricula.estudiante.idEstudiante = :idEstudiante AND d.estado = 'APROBADO'")
    List<DetalleMatricula> findCursosAprobadosByEstudiante(Long idEstudiante);
}
