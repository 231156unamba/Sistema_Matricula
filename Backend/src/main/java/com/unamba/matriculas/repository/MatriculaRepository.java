package com.unamba.matriculas.repository;

import com.unamba.matriculas.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    List<Matricula> findByEstudiante_IdEstudiante(Long idEstudiante);
    List<Matricula> findByPeriodo_IdPeriodo(Long idPeriodo);
    Optional<Matricula> findByEstudiante_IdEstudianteAndPeriodo_IdPeriodo(Long idEstudiante, Long idPeriodo);
}
