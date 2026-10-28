package com.app.backend.services.tramites;

import com.app.backend.dtos.tramites.response.DetallesPlantillaResponseDTO;
import lombok.NonNull;

import java.util.List;

// Expone vistas de detalle de plantillas para paneles y consultas.
public interface DetallesTramiteService {
    List<DetallesPlantillaResponseDTO> listarTodos();
    List<DetallesPlantillaResponseDTO> listarPorCarrera(@NonNull Integer idCarrera);
    List<DetallesPlantillaResponseDTO> listarPorFacultad(@NonNull Integer idFacultad);
    DetallesPlantillaResponseDTO obtenerPorPlantilla(@NonNull Integer idPlantilla);
}
