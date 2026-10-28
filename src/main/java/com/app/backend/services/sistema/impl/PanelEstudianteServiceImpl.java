package com.app.backend.services.sistema.impl;

import com.app.backend.dtos.sistema.response.NotificacionResponseDTO;
import com.app.backend.dtos.sistema.response.PanelEstudianteDTO;
import com.app.backend.dtos.tramites.response.SolicitudResponseDTO;
import com.app.backend.entities.sistema.Notificacion;
import com.app.backend.entities.sistema.Usuario;
import com.app.backend.entities.tramites.Solicitud;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.sistema.NotificacionRepository;
import com.app.backend.repositories.sistema.UsuarioRepository;
import com.app.backend.repositories.tramites.SolicitudRepository;
import com.app.backend.services.sistema.PanelEstudianteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import lombok.NonNull;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PanelEstudianteServiceImpl implements PanelEstudianteService {

    private final UsuarioRepository usuarioRepository;
    private final SolicitudRepository solicitudRepository;
    private final NotificacionRepository notificacionRepository;

    @Override
    @SuppressWarnings("")
    public PanelEstudianteDTO obtenerPanel(@NonNull Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + idUsuario));

        List<Solicitud> todasLasSolicitudes = solicitudRepository.findByUsuarioIdUsuario(idUsuario);

        long solicitudesActivas = todasLasSolicitudes.stream()
                .filter(s -> !s.getEstadoActual().equalsIgnoreCase("finalizado") &&
                        !s.getEstadoActual().equalsIgnoreCase("aprobado") &&
                        !s.getEstadoActual().equalsIgnoreCase("rechazado"))
                .count();

        List<SolicitudResponseDTO> solicitudesRecientes = todasLasSolicitudes.stream()
                .sorted((s1, s2) -> s2.getIdSolicitud().compareTo(s1.getIdSolicitud())) // Asumiendo que ID mayor es mÃ¡s
                                                                                        // reciente
                .limit(5)
                .map(this::mapSolicitudToDTO)
                .collect(Collectors.toList());

        List<NotificacionResponseDTO> notificacionesRecientes = notificacionRepository.findByUsuarioIdUsuarioOrderByFechaCreacionDesc(idUsuario)
                .stream()
                .limit(5)
                .map(this::mapNotificacionToDTO)
                .collect(Collectors.toList());

        return PanelEstudianteDTO.builder()
                .nombreEstudiante(usuario.getNombres() + " " + usuario.getApellidos())
                .solicitudesActivas((int) solicitudesActivas)
                .horasAcumuladas(15) // Placeholder como se discutiÃ³
                .solicitudesRecientes(solicitudesRecientes)
                .notificaciones(notificacionesRecientes)
                .build();
    }

    private SolicitudResponseDTO mapSolicitudToDTO(Solicitud s) {
        return SolicitudResponseDTO.builder()
                .idSolicitud(s.getIdSolicitud())
                .codigoSolicitud(s.getCodigoSolicitud())
                .idPlantilla(s.getPlantilla().getIdPlantilla())
                .idUsuario(s.getUsuario().getIdUsuario())
                .idCarrera(s.getCarrera() != null ? s.getCarrera().getIdCarrera() : null)
                .prioridad(s.getPrioridad())
                .estadoActual(s.getEstadoActual())
                .fechaEstimadaFin(s.getFechaEstimadaFin())
                .build();
    }

        private NotificacionResponseDTO mapNotificacionToDTO(Notificacion n) {
                return NotificacionResponseDTO.builder()
                .idNotificacion(n.getIdNotificacion())
                .idTipo(n.getTipoNotificacion().getIdTipo())
                .idUsuario(n.getUsuario().getIdUsuario())
                .idSolicitud(n.getSolicitud() != null ? n.getSolicitud().getIdSolicitud() : null)
                .titulo(n.getTitulo())
                .mensaje(n.getMensaje())
                .estaLeida(n.getEstaLeida())
                .build();
    }
}

