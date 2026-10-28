package com.app.backend.services.sistema;

import com.app.backend.dtos.sistema.request.CredencialRequestDTO;
import com.app.backend.dtos.sistema.response.CredencialResponseDTO;
import lombok.NonNull;

public interface CredencialService {
    CredencialResponseDTO obtenerPorUsuario(@NonNull Integer idUsuario);
    CredencialResponseDTO guardar(CredencialRequestDTO dto);
}
