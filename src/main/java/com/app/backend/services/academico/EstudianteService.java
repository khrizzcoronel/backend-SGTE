package com.app.backend.services.academico;

import com.app.backend.dtos.academico.response.EstudianteDTO;
import lombok.NonNull;
import java.util.List;

public interface EstudianteService {
    List<EstudianteDTO> listarTodos();
    List<EstudianteDTO> listarPorCarrera(@NonNull Integer idCarrera);
    EstudianteDTO obtenerPorId(@NonNull Integer id);
    EstudianteDTO obtenerPorUsuario(@NonNull Integer idUsuario);
    EstudianteDTO crear(EstudianteDTO dto);
    EstudianteDTO actualizar(@NonNull Integer id, EstudianteDTO dto);
    void eliminar(@NonNull Integer id);
}
