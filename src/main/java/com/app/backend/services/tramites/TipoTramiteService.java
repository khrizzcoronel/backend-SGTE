package com.app.backend.services.tramites;

import com.app.backend.dtos.tramites.request.PlantillaRequestDTO;
import com.app.backend.dtos.tramites.request.PlantillaEditarRequestDTO;
import com.app.backend.dtos.tramites.request.ActualizarRequisitosPlantillaRequestDTO;
import com.app.backend.dtos.tramites.response.PlantillaResponseDTO;
import com.app.backend.dtos.tramites.response.TipoPlantillaResponseDTO;

import lombok.NonNull;
import java.util.List;

// Contrato de administracion de plantillas de tramite.
public interface TipoTramiteService {
    List<TipoPlantillaResponseDTO> listarTodos();
    List<TipoPlantillaResponseDTO> listarActivos();
    List<TipoPlantillaResponseDTO> listarPorCategoria(@NonNull Integer idCategoria);
    TipoPlantillaResponseDTO obtenerPorId(@NonNull Integer id);
    TipoPlantillaResponseDTO crear(PlantillaRequestDTO dto);
    TipoPlantillaResponseDTO actualizar(@NonNull Integer id, PlantillaEditarRequestDTO dto);
    TipoPlantillaResponseDTO actualizarRequisitos(@NonNull Integer id, ActualizarRequisitosPlantillaRequestDTO dto);
    void eliminarCompleto(@NonNull Integer id);
    void eliminar(@NonNull Integer id);
    List<PlantillaResponseDTO> listarPlantillas(String categoria, Boolean activo, String busqueda);
}

