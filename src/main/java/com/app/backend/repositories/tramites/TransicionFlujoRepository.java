package com.app.backend.repositories.tramites;

import com.app.backend.entities.tramites.TransicionFlujo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Consultas de transiciones entre pasos de un flujo especifico.
public interface TransicionFlujoRepository extends JpaRepository<TransicionFlujo, Integer> {
    List<TransicionFlujo> findByFlujoTrabajoIdFlujo(Integer idFlujo);
}
