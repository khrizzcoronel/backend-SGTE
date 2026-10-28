package com.app.backend.repositories.tramites;

import com.app.backend.entities.tramites.Etapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// Etapas de flujo consultables por ID o por codigo funcional.
public interface EtapaRepository extends JpaRepository<Etapa, Integer> {
    Optional<Etapa> findByCodigo(String codigo);
}
