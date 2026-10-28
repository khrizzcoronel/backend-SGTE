package com.app.backend.repositories.academico;

import com.app.backend.entities.academico.Periodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeriodoRepository extends JpaRepository<Periodo, Integer> {
    Optional<Periodo> findByCodigoPeriodo(String codigoPeriodo);
    Optional<Periodo> findByEstaActivoTrue();
    Optional<Periodo> findByEsPeriodoActualTrue();
}
