package com.app.backend.services.sistema;

import com.app.backend.dtos.sistema.request.RegistroUsuarioDTO;
import com.app.backend.dtos.sistema.response.RegistroUsuarioRespuestaDTO;

public interface RegistroUsuarioService {
    RegistroUsuarioRespuestaDTO registrarUsuario(RegistroUsuarioDTO dto);
}
