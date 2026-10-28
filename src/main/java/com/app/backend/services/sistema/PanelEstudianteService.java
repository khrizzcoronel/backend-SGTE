package com.app.backend.services.sistema;

import com.app.backend.dtos.sistema.response.PanelEstudianteDTO;

public interface PanelEstudianteService {
    PanelEstudianteDTO obtenerPanel(Integer idUsuario);
}
