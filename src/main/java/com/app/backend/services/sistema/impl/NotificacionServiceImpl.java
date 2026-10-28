package com.app.backend.services.sistema.impl;

import com.app.backend.dtos.sistema.request.NotificacionRequestDTO;
import com.app.backend.dtos.sistema.response.NotificacionResponseDTO;
import com.app.backend.entities.sistema.Notificacion;
import com.app.backend.entities.sistema.TipoNotificacion;
import com.app.backend.entities.sistema.Usuario;
import com.app.backend.entities.tramites.Solicitud;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.sistema.NotificacionRepository;
import com.app.backend.repositories.sistema.TipoNotificacionRepository;
import com.app.backend.repositories.sistema.UsuarioRepository;
import com.app.backend.repositories.tramites.SolicitudRepository;
import com.app.backend.services.sistema.NotificacionService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final TipoNotificacionRepository tipoNotificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final SolicitudRepository solicitudRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> listarPorUsuario(@NonNull Integer idUsuario) {
        return notificacionRepository.findByUsuarioIdUsuarioOrderByFechaCreacionDesc(idUsuario).stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> listarNoLeidasPorUsuario(@NonNull Integer idUsuario) {
        return notificacionRepository.findByUsuarioIdUsuarioAndEstaLeidaFalseOrderByFechaCreacionDesc(idUsuario).stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public NotificacionResponseDTO obtenerPorId(@NonNull Integer id) {
        return toDTO(notificacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Notificación no encontrada con id: " + id)));
    }

    @Override
    public NotificacionResponseDTO crear(NotificacionRequestDTO dto) {
        TipoNotificacion tipo = tipoNotificacionRepository.findById(dto.getIdTipo())
                .orElseThrow(() -> new RecursoNoEncontradoException("Tipo notificación no encontrado: " + dto.getIdTipo()));
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + dto.getIdUsuario()));

        Notificacion notificacion = Notificacion.builder()
                .tipoNotificacion(tipo)
                .usuario(usuario)
                .titulo(dto.getTitulo())
                .mensaje(dto.getMensaje())
                .canal(dto.getCanal())
                .estaLeida(false)
                .estaEnviada(false)
                .programadaPara(dto.getProgramadaPara())
                .build();

        if (dto.getIdSolicitud() != null) {
            Solicitud solicitud = solicitudRepository.findById(dto.getIdSolicitud())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada: " + dto.getIdSolicitud()));
            notificacion.setSolicitud(solicitud);
        }

        return toDTO(notificacionRepository.save(notificacion));
    }

    @Override
    public NotificacionResponseDTO marcarComoLeida(@NonNull Integer id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Notificación no encontrada con id: " + id));
        notificacion.setEstaLeida(true);
        notificacion.setFechaLectura(LocalDateTime.now());
        return toDTO(notificacionRepository.save(notificacion));
    }

    private NotificacionResponseDTO toDTO(Notificacion n) {
        return NotificacionResponseDTO.builder()
                .idNotificacion(n.getIdNotificacion())
                .idTipo(n.getTipoNotificacion().getIdTipo())
                .idUsuario(n.getUsuario().getIdUsuario())
                .idSolicitud(n.getSolicitud() != null ? n.getSolicitud().getIdSolicitud() : null)
                .titulo(n.getTitulo())
                .mensaje(n.getMensaje())
                .canal(n.getCanal())
                .estaLeida(n.getEstaLeida())
                .fechaLectura(n.getFechaLectura())
                .estaEnviada(n.getEstaEnviada())
                .fechaEnvio(n.getFechaEnvio())
                .errorEnvio(n.getErrorEnvio())
                .programadaPara(n.getProgramadaPara())
                .fechaCreacion(n.getFechaCreacion())
                .build();
    }
}
