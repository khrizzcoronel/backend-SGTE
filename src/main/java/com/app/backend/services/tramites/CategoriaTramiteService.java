package com.app.backend.services.tramites;

import com.app.backend.dtos.tramites.response.CategoriaPlantillaResponseDTO;
import lombok.NonNull;
import java.util.List;

// CRUD de categorias utilizadas en plantillas de tramite.
public interface CategoriaTramiteService {
    List<CategoriaPlantillaResponseDTO> listarTodas();
    List<CategoriaPlantillaResponseDTO> listarActivas();
    CategoriaPlantillaResponseDTO obtenerPorId(@NonNull Integer id);
    CategoriaPlantillaResponseDTO crear(CategoriaPlantillaResponseDTO dto);
    CategoriaPlantillaResponseDTO actualizar(@NonNull Integer id, CategoriaPlantillaResponseDTO dto);
    void eliminar(@NonNull Integer id);
}

