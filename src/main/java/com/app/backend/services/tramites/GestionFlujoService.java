package com.app.backend.services.tramites;

import com.app.backend.dtos.tramites.request.AsignarPasoUsuarioRequestDTO;
import com.app.backend.dtos.tramites.request.FlujoTrabajoCompletoRequestDTO;
import com.app.backend.dtos.tramites.response.FlujoTrabajoDetalleResponseDTO;
import com.app.backend.dtos.tramites.response.PasoFlujoDetalleResponseDTO;

import java.util.List;

// Operaciones de alto nivel para crear y consultar flujos completos.
public interface GestionFlujoService {
    List<FlujoTrabajoDetalleResponseDTO> listarFlujosCompletos();
    FlujoTrabajoDetalleResponseDTO obtenerFlujoCompleto(Integer idFlujo);
    FlujoTrabajoDetalleResponseDTO crearFlujoCompleto(FlujoTrabajoCompletoRequestDTO request);
    PasoFlujoDetalleResponseDTO asignarUsuarioPaso(Integer idPaso, AsignarPasoUsuarioRequestDTO request);
}