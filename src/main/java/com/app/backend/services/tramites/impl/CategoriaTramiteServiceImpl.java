package com.app.backend.services.tramites.impl;

import com.app.backend.dtos.tramites.response.CategoriaPlantillaResponseDTO;
import com.app.backend.entities.tramites.Categoria;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.tramites.CategoriaRepository;
import com.app.backend.services.tramites.CategoriaTramiteService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
// CRUD de categorias para clasificar plantillas de tramite.
public class CategoriaTramiteServiceImpl implements CategoriaTramiteService {

    private final CategoriaRepository categoriaRepository;

    @Override @Transactional(readOnly = true)
    public List<CategoriaPlantillaResponseDTO> listarTodas() { return categoriaRepository.findAll().stream().map(this::toDTO).toList(); }

    @Override @Transactional(readOnly = true)
    public List<CategoriaPlantillaResponseDTO> listarActivas() { return categoriaRepository.findByEstaActivoTrue().stream().map(this::toDTO).toList(); }

    @Override @Transactional(readOnly = true)
    public CategoriaPlantillaResponseDTO obtenerPorId(@NonNull Integer id) { return toDTO(categoriaRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("CategorÃ­a no encontrada con id: " + id))); }

    @Override
    public CategoriaPlantillaResponseDTO crear(CategoriaPlantillaResponseDTO dto) {
        Categoria c = Categoria.builder().nombreCategoria(dto.getNombreCategoria()).descripcionCategoria(dto.getDescripcionCategoria()).estaActivo(dto.getEstaActivo() != null ? dto.getEstaActivo() : true).build();
        return toDTO(categoriaRepository.save(c));
    }

    @Override
    public CategoriaPlantillaResponseDTO actualizar(@NonNull Integer id, CategoriaPlantillaResponseDTO dto) {
        Categoria c = categoriaRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("CategorÃ­a no encontrada con id: " + id));
        c.setNombreCategoria(dto.getNombreCategoria()); c.setDescripcionCategoria(dto.getDescripcionCategoria()); c.setEstaActivo(dto.getEstaActivo());
        return toDTO(categoriaRepository.save(c));
    }

    @Override
    public void eliminar(@NonNull Integer id) { if (!categoriaRepository.existsById(id)) throw new RecursoNoEncontradoException("CategorÃ­a no encontrada con id: " + id); categoriaRepository.deleteById(id); }

    private CategoriaPlantillaResponseDTO toDTO(Categoria c) { return CategoriaPlantillaResponseDTO.builder().idCategoria(c.getIdCategoria()).nombreCategoria(c.getNombreCategoria()).descripcionCategoria(c.getDescripcionCategoria()).estaActivo(c.getEstaActivo()).build(); }
}

