package com.app.backend.services.tramites;

import com.app.backend.dtos.tramites.response.MotivoRechazoResponseDTO;
import lombok.NonNull;
import java.util.List;

// Gestion de motivos de rechazo disponibles en coordinacion.
public interface MotivoRechazoService {
    List<MotivoRechazoResponseDTO> listarTodos();
    List<MotivoRechazoResponseDTO> listarActivos();
    MotivoRechazoResponseDTO obtenerPorId(@NonNull Integer id);
    MotivoRechazoResponseDTO crear(MotivoRechazoResponseDTO dto);
    MotivoRechazoResponseDTO actualizar(@NonNull Integer id, MotivoRechazoResponseDTO dto);
    void eliminar(@NonNull Integer id);
}

