package com.unamba.matriculas.repository;

import com.unamba.matriculas.model.Prerrequisito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrerrequisitoRepository extends JpaRepository<Prerrequisito, Long> {
    List<Prerrequisito> findByCurso_IdCurso(Long idCurso);
}
