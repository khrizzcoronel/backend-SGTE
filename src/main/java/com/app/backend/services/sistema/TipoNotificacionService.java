package com.app.backend.services.sistema;

import com.app.backend.dtos.sistema.request.TipoNotificacionRequestDTO;
import com.app.backend.dtos.sistema.response.TipoNotificacionResponseDTO;
import lombok.NonNull;
import java.util.List;

public interface TipoNotificacionService {
    List<TipoNotificacionResponseDTO> listarTodos();
    TipoNotificacionResponseDTO obtenerPorId(@NonNull Integer id);
    TipoNotificacionResponseDTO crear(TipoNotificacionRequestDTO dto);
    TipoNotificacionResponseDTO actualizar(@NonNull Integer id, TipoNotificacionRequestDTO dto);
    void eliminar(@NonNull Integer id);
}
