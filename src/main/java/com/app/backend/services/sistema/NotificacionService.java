package com.app.backend.services.sistema;

import com.app.backend.dtos.sistema.request.NotificacionRequestDTO;
import com.app.backend.dtos.sistema.response.NotificacionResponseDTO;
import lombok.NonNull;
import java.util.List;

public interface NotificacionService {
    List<NotificacionResponseDTO> listarPorUsuario(@NonNull Integer idUsuario);
    List<NotificacionResponseDTO> listarNoLeidasPorUsuario(@NonNull Integer idUsuario);
    NotificacionResponseDTO obtenerPorId(@NonNull Integer id);
    NotificacionResponseDTO crear(NotificacionRequestDTO dto);
    NotificacionResponseDTO marcarComoLeida(@NonNull Integer id);
}
