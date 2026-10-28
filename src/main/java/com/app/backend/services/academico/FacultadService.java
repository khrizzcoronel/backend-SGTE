package com.app.backend.services.academico;

import com.app.backend.dtos.academico.response.FacultadDTO;
import lombok.NonNull;
import java.util.List;

public interface FacultadService {
    List<FacultadDTO> listarTodas();
    List<FacultadDTO> listarPorUniversidad(@NonNull Integer idUniversidad);
    FacultadDTO obtenerPorId(@NonNull Integer id);
    FacultadDTO crear(FacultadDTO dto);
    FacultadDTO actualizar(@NonNull Integer id, FacultadDTO dto);
    void eliminar(@NonNull Integer id);
}
