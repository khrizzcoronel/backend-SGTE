package com.app.backend.services.sistema;

import com.app.backend.dtos.sistema.request.AutenticacionRequestDTO;
import com.app.backend.dtos.sistema.response.AutenticacionRespuestaDTO;

public interface AutenticacionService {
    AutenticacionRespuestaDTO iniciarSesion(AutenticacionRequestDTO peticion);
}
