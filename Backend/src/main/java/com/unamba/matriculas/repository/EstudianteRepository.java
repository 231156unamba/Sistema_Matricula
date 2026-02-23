package com.unamba.matriculas.repository;

import com.unamba.matriculas.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByDni(String dni);
    Optional<Estudiante> findByCodigoEstudiante(String codigoEstudiante);
    
    /**
     * Cuenta estudiantes cuyo código comienza con el prefijo dado (ej: "231" para año 23, semestre 1)
     */
    long countByCodigoEstudianteStartingWith(String prefijo);
}
