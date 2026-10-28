package com.app.backend.repositories.academico;

import com.app.backend.entities.academico.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {
    Optional<Estudiante> findByUsuarioIdUsuario(Integer idUsuario);
    List<Estudiante> findByCarreraIdCarrera(Integer idCarrera);
    Optional<Estudiante> findByIdEstudianteSga(Integer idEstudianteSga);
}
