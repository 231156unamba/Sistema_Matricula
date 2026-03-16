package com.unamba.matriculas.repository;

import com.unamba.matriculas.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByDni(String dni);
    Optional<Estudiante> findByCodigoEstudiante(String codigoEstudiante);
    List<Estudiante> findByTipo(Estudiante.TipoEstudiante tipo);
    List<Estudiante> findByEstado(Estudiante.EstadoEstudiante estado);
    long countByTipo(Estudiante.TipoEstudiante tipo);
    
    /**
     * Cuenta estudiantes cuyo código comienza con el prefijo dado (ej: "231" para año 23, semestre 1)
     */
    long countByCodigoEstudianteStartingWith(String prefijo);
}
