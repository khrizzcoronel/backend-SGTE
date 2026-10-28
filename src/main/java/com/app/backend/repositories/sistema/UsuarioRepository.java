package com.app.backend.repositories.sistema;

import com.app.backend.entities.sistema.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCedula(String cedula);
    Optional<Usuario> findByCorreoInstitucional(String correoInstitucional);
    boolean existsByCedula(String cedula);
    boolean existsByCorreoInstitucional(String correoInstitucional);
    List<Usuario> findByRoles_IdRol(Integer idRol);
}
