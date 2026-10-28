package com.app.backend.repositories.tramites;

import com.app.backend.entities.tramites.MotivoRechazo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
// Catalogo de motivos de rechazo (activos/inactivos).
public interface MotivoRechazoRepository extends JpaRepository<MotivoRechazo, Integer> {
    Optional<MotivoRechazo> findByCodigoMotivo(String codigoMotivo);
    List<MotivoRechazo> findByEstaActivoTrue();
}
