package com.app.backend.repositories.academico;

import com.app.backend.entities.academico.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Integer> {
    Optional<Carrera> findByCodigoCarrera(String codigoCarrera);
    List<Carrera> findByFacultadIdFacultad(Integer idFacultad);
}
