package com.unamba.matriculas.repository;

import com.unamba.matriculas.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Optional<Administrador> findByUsuario(String usuario);
    Optional<Administrador> findByUsuarioAndActivoTrue(String usuario);
    boolean existsByUsuario(String usuario);
}
