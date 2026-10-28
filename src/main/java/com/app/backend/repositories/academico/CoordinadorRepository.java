package com.app.backend.repositories.academico;

import com.app.backend.entities.academico.Coordinador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoordinadorRepository extends JpaRepository<Coordinador, Integer> {
    Optional<Coordinador> findByUsuarioIdUsuario(Integer idUsuario);
    List<Coordinador> findByCarreraIdCarrera(Integer idCarrera);
    List<Coordinador> findByEstaActivoTrue();
}
