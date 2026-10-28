package com.app.backend.repositories.tramites;

import com.app.backend.entities.tramites.FlujoTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Persistencia de definiciones de flujo de trabajo.
public interface FlujoTrabajoRepository extends JpaRepository<FlujoTrabajo, Integer> {
    List<FlujoTrabajo> findByEstaActivoTrue();
}
