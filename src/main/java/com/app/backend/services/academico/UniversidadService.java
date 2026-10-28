package com.app.backend.services.academico;

import com.app.backend.dtos.academico.response.UniversidadDTO;
import lombok.NonNull;
import java.util.List;

public interface UniversidadService {
    List<UniversidadDTO> listarTodas();
    UniversidadDTO obtenerPorId(@NonNull Integer id);
    UniversidadDTO crear(UniversidadDTO dto);
    UniversidadDTO actualizar(@NonNull Integer id, UniversidadDTO dto);
    void eliminar(@NonNull Integer id);
}
