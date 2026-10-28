package com.app.backend.repositories.tramites;

import com.app.backend.entities.tramites.RequisitoPlantilla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Requisitos documentales por plantilla, ordenados para render en formularios.
public interface RequisitoPlantillaRepository extends JpaRepository<RequisitoPlantilla, Integer> {
    List<RequisitoPlantilla> findByPlantillaIdPlantillaOrderByNumeroOrdenAsc(Integer idPlantilla);
    void deleteByPlantillaIdPlantilla(Integer idPlantilla);
}
