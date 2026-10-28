package com.app.backend.services.sistema.impl;

import com.app.backend.dtos.sistema.response.RolDTO;
import com.app.backend.entities.sistema.Rol;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.sistema.RolRepository;
import com.app.backend.services.sistema.RolService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RolDTO> listarTodos() {
        return rolRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RolDTO obtenerPorId(@NonNull Integer id) {
        return toDTO(rolRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado con id: " + id)));
    }

    @Override
    public RolDTO crear(RolDTO dto) {
        Rol rol = Rol.builder()
                .nombreRol(dto.getNombreRol())
                .descripcionRol(dto.getDescripcionRol())
                .nivelJerarquico(dto.getNivelJerarquico())
                .build();
        return toDTO(rolRepository.save(rol));
    }

    @Override
    public RolDTO actualizar(@NonNull Integer id, RolDTO dto) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado con id: " + id));
        rol.setNombreRol(dto.getNombreRol());
        rol.setDescripcionRol(dto.getDescripcionRol());
        rol.setNivelJerarquico(dto.getNivelJerarquico());
        return toDTO(rolRepository.save(rol));
    }

    @Override
    public void eliminar(@NonNull Integer id) {
        if (!rolRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Rol no encontrado con id: " + id);
        }
        rolRepository.deleteById(id);
    }

    private RolDTO toDTO(Rol r) {
        return RolDTO.builder()
                .idRol(r.getIdRol())
                .nombreRol(r.getNombreRol())
                .descripcionRol(r.getDescripcionRol())
                .nivelJerarquico(r.getNivelJerarquico())
                .build();
    }
}
