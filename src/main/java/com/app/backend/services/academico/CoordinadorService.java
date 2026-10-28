package com.app.backend.services.academico;

import com.app.backend.dtos.academico.response.CoordinadorDTO;
import lombok.NonNull;
import java.util.List;

public interface CoordinadorService {
    List<CoordinadorDTO> listarTodos();
    List<CoordinadorDTO> listarPorCarrera(@NonNull Integer idCarrera);
    List<CoordinadorDTO> listarActivos();
    CoordinadorDTO obtenerPorId(@NonNull Integer id);
    CoordinadorDTO crear(CoordinadorDTO dto);
    CoordinadorDTO actualizar(@NonNull Integer id, CoordinadorDTO dto);
    void eliminar(@NonNull Integer id);
}
