package com.app.backend.repositories.tramites;

import com.app.backend.entities.tramites.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Categorias de tramite disponibles para clasificacion de plantillas.
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    List<Categoria> findByEstaActivoTrue();
}
