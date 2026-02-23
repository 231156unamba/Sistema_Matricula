package com.unamba.matriculas.repository;

import com.unamba.matriculas.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findBySemestre(Integer semestre);
}
