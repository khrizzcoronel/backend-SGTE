package com.app.backend.repositories.tramites;

import com.app.backend.entities.tramites.PasoFlujo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Acceso a pasos ordenados por flujo para construir y ejecutar el circuito de tramites.
public interface PasoFlujoRepository extends JpaRepository<PasoFlujo, Integer> {
    List<PasoFlujo> findByFlujoTrabajoIdFlujoOrderByOrdenPasoAsc(Integer idFlujo);
}
