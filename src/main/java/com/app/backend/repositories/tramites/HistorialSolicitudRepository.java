package com.app.backend.repositories.tramites;

import com.app.backend.entities.tramites.HistorialSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Persistencia del historial de cambios/acciones de cada solicitud.
public interface HistorialSolicitudRepository extends JpaRepository<HistorialSolicitud, Integer> {
    // Se usa para construir linea de tiempo en detalle de solicitud.
    List<HistorialSolicitud> findBySolicitudIdSolicitudOrderByFechaEntradaAsc(Integer idSolicitud);
}
