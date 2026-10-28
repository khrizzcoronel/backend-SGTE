package com.app.backend.services.academico;

import com.app.backend.dtos.academico.response.SemestreDTO;
import lombok.NonNull;
import java.util.List;

public interface SemestreService {
    List<SemestreDTO> listarTodos();
    SemestreDTO obtenerPorId(@NonNull Integer id);
    SemestreDTO obtenerActivo();
    SemestreDTO crear(SemestreDTO dto);
    SemestreDTO actualizar(@NonNull Integer id, SemestreDTO dto);
    void eliminar(@NonNull Integer id);
}
