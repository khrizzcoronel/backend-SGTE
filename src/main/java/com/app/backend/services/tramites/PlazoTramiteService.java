package com.app.backend.services.tramites;

import com.app.backend.dtos.tramites.response.PlazoPlantillaResponseDTO;
import lombok.NonNull;
import java.util.List;

// Define ventanas y plazos de recepcion de solicitudes por plantilla.
public interface PlazoTramiteService {
    List<PlazoPlantillaResponseDTO> listarPorPlantilla(@NonNull Integer idPlantilla);
    PlazoPlantillaResponseDTO obtenerPorId(@NonNull Integer id);
    PlazoPlantillaResponseDTO crear(PlazoPlantillaResponseDTO dto);
    PlazoPlantillaResponseDTO actualizar(@NonNull Integer id, PlazoPlantillaResponseDTO dto);
    void eliminar(@NonNull Integer id);
}

