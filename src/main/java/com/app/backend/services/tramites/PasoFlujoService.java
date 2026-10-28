package com.app.backend.services.tramites;

import com.app.backend.dtos.tramites.response.PasoFlujoResponseDTO;
import lombok.NonNull;
import java.util.List;

// Contrato CRUD para pasos individuales de un flujo.
public interface PasoFlujoService {
    List<PasoFlujoResponseDTO> listarPorFlujo(@NonNull Integer idFlujo);
    PasoFlujoResponseDTO obtenerPorId(@NonNull Integer id);
    PasoFlujoResponseDTO crear(PasoFlujoResponseDTO dto);
    PasoFlujoResponseDTO actualizar(@NonNull Integer id, PasoFlujoResponseDTO dto);
    void eliminar(@NonNull Integer id);
}

