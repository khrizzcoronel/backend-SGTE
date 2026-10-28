package com.app.backend.services.tramites;

import com.app.backend.dtos.tramites.response.EtapaProcesamientoResponseDTO;
import lombok.NonNull;
import java.util.List;

// CRUD del catalogo de etapas de procesamiento.
public interface EtapaProcesamientoService {
    List<EtapaProcesamientoResponseDTO> listarTodas();
    EtapaProcesamientoResponseDTO obtenerPorId(@NonNull Integer id);
    EtapaProcesamientoResponseDTO crear(EtapaProcesamientoResponseDTO dto);
    EtapaProcesamientoResponseDTO actualizar(@NonNull Integer id, EtapaProcesamientoResponseDTO dto);
    void eliminar(@NonNull Integer id);
}

