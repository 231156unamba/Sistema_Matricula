package com.unamba.matriculas.repository;

import com.unamba.matriculas.model.Anuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, Long> {
    List<Anuncio> findByActivoTrueOrderByFechaCreacionDesc();
}
