package com.app.backend.repositories.tramites;

import com.app.backend.entities.tramites.Rechazo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Acceso a registros de rechazo asociados a una solicitud.
public interface RechazoRepository extends JpaRepository<Rechazo, Integer> {
    List<Rechazo> findBySolicitudIdSolicitud(Integer idSolicitud);
}
