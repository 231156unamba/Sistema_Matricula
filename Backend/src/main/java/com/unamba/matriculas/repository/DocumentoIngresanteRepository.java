package com.unamba.matriculas.repository;

import com.unamba.matriculas.model.DocumentoIngresante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DocumentoIngresanteRepository extends JpaRepository<DocumentoIngresante, Long> {
    Optional<DocumentoIngresante> findByEstudiante_IdEstudiante(Long idEstudiante);
}
