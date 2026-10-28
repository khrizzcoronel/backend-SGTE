package com.app.backend.services.academico;

import com.app.backend.dtos.academico.response.DecanoDTO;
import lombok.NonNull;
import java.util.List;

public interface DecanoService {
    List<DecanoDTO> listarTodos();
    List<DecanoDTO> listarActivos();
    DecanoDTO obtenerPorId(@NonNull Integer id);
    DecanoDTO crear(DecanoDTO dto);
    DecanoDTO actualizar(@NonNull Integer id, DecanoDTO dto);
    void eliminar(@NonNull Integer id);
}
