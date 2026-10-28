package com.app.backend.services.tramites;

import com.app.backend.dtos.tramites.request.AccionPasoRequestDTO;
import com.app.backend.dtos.tramites.request.CrearSolicitudRequestDTO;
import com.app.backend.dtos.tramites.response.SolicitudDetalleResponseDTO;
import com.app.backend.dtos.tramites.response.SolicitudResponseDTO;
import com.app.backend.dtos.tramites.response.SolicitudesPlantillasVigentesRespuestaDTO;

import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// Contrato principal de gestion de solicitudes en coordinacion.
public interface SolicitudService {
    List<SolicitudResponseDTO> listarTodas();

    List<SolicitudResponseDTO> listarPorUsuario(@NonNull Integer idUsuario);

    List<SolicitudResponseDTO> listarPorEstado(String estado);

    SolicitudResponseDTO obtenerPorId(@NonNull Integer id);

    SolicitudResponseDTO obtenerPorCodigo(String codigo);

    SolicitudResponseDTO crear(SolicitudResponseDTO dto);

    SolicitudResponseDTO actualizar(@NonNull Integer id, SolicitudResponseDTO dto);

    void eliminar(@NonNull Integer id);

    List<SolicitudesPlantillasVigentesRespuestaDTO> listarPlantillasVigente();

    void crearSolicitudConDocumentos(CrearSolicitudRequestDTO dto, List<MultipartFile> archivos);

    List<SolicitudDetalleResponseDTO> listarSolicitudesUsuarioAutenticado();

    SolicitudDetalleResponseDTO obtenerDetalleSolicitud(@NonNull Integer idSolicitud);

    List<SolicitudDetalleResponseDTO> listarSolicitudesPorRol(String nombreRol);

    void aprobarPasoActual(AccionPasoRequestDTO dto);

    void rechazarSolicitud(AccionPasoRequestDTO dto);
}


