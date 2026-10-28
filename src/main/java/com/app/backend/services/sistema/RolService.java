package com.app.backend.services.sistema;

import com.app.backend.dtos.sistema.response.RolDTO;
import lombok.NonNull;
import java.util.List;

public interface RolService {
    List<RolDTO> listarTodos();
    RolDTO obtenerPorId(@NonNull Integer id);
    RolDTO crear(RolDTO dto);
    RolDTO actualizar(@NonNull Integer id, RolDTO dto);
    void eliminar(@NonNull Integer id);
}
