package com.app.backend.repositories.academico;

import com.app.backend.entities.academico.Decano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DecanoRepository extends JpaRepository<Decano, Integer> {
    Optional<Decano> findByUsuarioIdUsuario(Integer idUsuario);
    List<Decano> findByFacultadIdFacultad(Integer idFacultad);
    List<Decano> findByEstaActivoTrue();
}
