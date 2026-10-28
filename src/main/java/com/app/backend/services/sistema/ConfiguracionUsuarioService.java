package com.app.backend.services.sistema;

import com.app.backend.dtos.sistema.request.ConfiguracionUsuarioRequestDTO;
import com.app.backend.dtos.sistema.response.ConfiguracionUsuarioResponseDTO;
import lombok.NonNull;

public interface ConfiguracionUsuarioService {
    ConfiguracionUsuarioResponseDTO obtenerPorUsuario(@NonNull Integer idUsuario);
    ConfiguracionUsuarioResponseDTO guardar(ConfiguracionUsuarioRequestDTO dto);
}
