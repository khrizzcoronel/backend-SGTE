package com.app.backend.services.tramites.impl;

import com.app.backend.dtos.tramites.request.RechazoSolicitudRequestDTO;
import com.app.backend.entities.tramites.Rechazo;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.sistema.UsuarioRepository;
import com.app.backend.repositories.tramites.MotivoRechazoRepository;
import com.app.backend.repositories.tramites.RechazoRepository;
import com.app.backend.repositories.tramites.SolicitudRepository;
import com.app.backend.services.tramites.RechazoSolicitudService;
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
// Maneja el registro formal de rechazos y su trazabilidad asociada.
public class RechazoSolicitudServiceImpl implements RechazoSolicitudService {

    private final RechazoRepository rechazoRepository;
    private final SolicitudRepository solicitudRepository;
    private final MotivoRechazoRepository motivoRechazoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override @Transactional(readOnly = true)
    public List<RechazoSolicitudRequestDTO> listarPorSolicitud(@NonNull Integer idSolicitud) {
        return rechazoRepository.findBySolicitudIdSolicitud(idSolicitud).stream().map(this::toDTO).toList();
    }

    @Override
    public RechazoSolicitudRequestDTO crear(RechazoSolicitudRequestDTO dto) {
        Rechazo r = Rechazo.builder()
                .solicitud(solicitudRepository.findById(dto.getIdSolicitud()).orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada: " + dto.getIdSolicitud())))
                .motivoRechazo(motivoRechazoRepository.findById(dto.getIdMotivo()).orElseThrow(() -> new RecursoNoEncontradoException("Motivo no encontrado: " + dto.getIdMotivo())))
                .rechazadoPor(usuarioRepository.findById(dto.getRechazadoPorId()).orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + dto.getRechazadoPorId())))
                .comentarios(dto.getComentarios())
                .fechaRechazo(dto.getFechaRechazo() != null ? dto.getFechaRechazo() : LocalDateTime.now())
                .notificacionEnviada(dto.getNotificacionEnviada() != null ? dto.getNotificacionEnviada() : false)
                .fechaNotificacion(dto.getFechaNotificacion())
                .build();
        return toDTO(rechazoRepository.save(r));
    }

    private RechazoSolicitudRequestDTO toDTO(Rechazo r) {
        return RechazoSolicitudRequestDTO.builder().idRechazo(r.getIdRechazo()).idSolicitud(r.getSolicitud().getIdSolicitud()).idMotivo(r.getMotivoRechazo().getIdMotivo()).rechazadoPorId(r.getRechazadoPor().getIdUsuario()).comentarios(r.getComentarios()).fechaRechazo(r.getFechaRechazo()).notificacionEnviada(r.getNotificacionEnviada()).fechaNotificacion(r.getFechaNotificacion()).build();
    }
}

