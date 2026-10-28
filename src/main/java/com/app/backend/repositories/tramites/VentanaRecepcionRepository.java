package com.app.backend.repositories.tramites;

import com.app.backend.entities.tramites.VentanaRecepcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Ventanas temporales de recepcion de solicitudes por plantilla.
public interface VentanaRecepcionRepository extends JpaRepository<VentanaRecepcion, Integer> {
    List<VentanaRecepcion> findByPlantillaIdPlantilla(Integer idPlantilla);
    void deleteByPlantillaIdPlantilla(Integer idPlantilla);
}
