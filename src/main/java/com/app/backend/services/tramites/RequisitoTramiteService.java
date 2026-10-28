package com.app.backend.services.tramites;

import com.app.backend.dtos.tramites.response.RequisitoPlantillaResponseDTO;
import lombok.NonNull;
import java.util.List;

// Operaciones sobre requisitos documentales de plantillas.
public interface RequisitoTramiteService {
    List<RequisitoPlantillaResponseDTO> listarPorPlantilla(@NonNull Integer idPlantilla);
    RequisitoPlantillaResponseDTO obtenerPorId(@NonNull Integer id);
    RequisitoPlantillaResponseDTO crear(RequisitoPlantillaResponseDTO dto);
    RequisitoPlantillaResponseDTO actualizar(@NonNull Integer id, RequisitoPlantillaResponseDTO dto);
    void eliminar(@NonNull Integer id);
}

