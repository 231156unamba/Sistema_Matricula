package com.unamba.matriculas.repository;

import com.unamba.matriculas.model.PeriodoAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PeriodoAcademicoRepository extends JpaRepository<PeriodoAcademico, Long> {
    Optional<PeriodoAcademico> findByEstado(PeriodoAcademico.EstadoPeriodo estado);
}
