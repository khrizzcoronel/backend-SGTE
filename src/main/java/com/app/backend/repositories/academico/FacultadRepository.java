package com.app.backend.repositories.academico;

import com.app.backend.entities.academico.Facultad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultadRepository extends JpaRepository<Facultad, Integer> {
    Optional<Facultad> findByNombreFacultad(String nombreFacultad);
    Optional<Facultad> findByNombreFacultadIgnoreCase(String nombreFacultad);
    List<Facultad> findByUniversidadIdUniversidad(Integer idUniversidad);
}
