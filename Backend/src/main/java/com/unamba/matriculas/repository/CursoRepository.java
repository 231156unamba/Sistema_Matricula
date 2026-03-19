package com.unamba.matriculas.repository;

import com.unamba.matriculas.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findBySemestre(Integer semestre);
    Optional<Curso> findByCodigoCurso(String codigoCurso);
    List<Curso> findByCarreraOrCarrera(String carrera, String comun);

    @Query("SELECT c FROM Curso c WHERE c.carrera = 'COMUN' OR c.carrera = :carrera OR c.carrera IS NULL")
    List<Curso> findByCarreraOrComun(@Param("carrera") String carrera);
}
