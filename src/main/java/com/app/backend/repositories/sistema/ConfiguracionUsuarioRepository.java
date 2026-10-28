package com.app.backend.repositories.sistema;

import com.app.backend.entities.sistema.ConfiguracionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracionUsuarioRepository extends JpaRepository<ConfiguracionUsuario, Integer> {
    Optional<ConfiguracionUsuario> findByUsuarioIdUsuario(Integer idUsuario);
}
