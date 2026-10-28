package com.app.backend.services.tramites.impl;

import com.app.backend.dtos.tramites.response.SeguimientoSolicitudResponseDTO;
import com.app.backend.entities.tramites.HistorialSolicitud;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.sistema.UsuarioRepository;
import com.app.backend.repositories.tramites.EtapaRepository;
import com.app.backend.repositories.tramites.HistorialSolicitudRepository;
import com.app.backend.repositories.tramites.PasoFlujoRepository;
import com.app.backend.repositories.tramites.SolicitudRepository;
import com.app.backend.services.tramites.SeguimientoSolicitudService;
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
// Gestiona el historial cronologico de acciones por solicitud.
public class SeguimientoSolicitudServiceImpl implements SeguimientoSolicitudService {

    private final HistorialSolicitudRepository historialSolicitudRepository;
    private final SolicitudRepository solicitudRepository;
    private final PasoFlujoRepository pasoFlujoRepository;
    private final EtapaRepository etapaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override @Transactional(readOnly = true)
    public List<SeguimientoSolicitudResponseDTO> listarPorSolicitud(@NonNull Integer idSolicitud) {
        return historialSolicitudRepository.findBySolicitudIdSolicitudOrderByFechaEntradaAsc(idSolicitud).stream().map(this::toDTO).toList();
    }

    @Override
    public SeguimientoSolicitudResponseDTO crear(SeguimientoSolicitudResponseDTO dto) {
        // Inserta un evento de seguimiento enlazado a solicitud/paso/etapa/usuario.
        HistorialSolicitud s = HistorialSolicitud.builder()
                .solicitud(solicitudRepository.findById(dto.getIdSolicitud()).orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada: " + dto.getIdSolicitud())))
                .estado(dto.getEstado())
                .comentarios(dto.getComentarios())
                .fechaEntrada(dto.getFechaEntrada() != null ? dto.getFechaEntrada() : LocalDateTime.now())
                .fechaCompletado(dto.getFechaCompletado())
                .slaExcedido(dto.getSlaExcedido() != null ? dto.getSlaExcedido() : false)
                .build();
        if (dto.getIdPaso() != null) s.setPasoFlujo(pasoFlujoRepository.findById(dto.getIdPaso()).orElseThrow(() -> new RecursoNoEncontradoException("Paso no encontrado: " + dto.getIdPaso())));
        if (dto.getIdEtapa() != null) s.setEtapa(etapaRepository.findById(dto.getIdEtapa()).orElseThrow(() -> new RecursoNoEncontradoException("Etapa no encontrada: " + dto.getIdEtapa())));
        if (dto.getProcesadoPorId() != null) s.setProcesadoPor(usuarioRepository.findById(dto.getProcesadoPorId()).orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + dto.getProcesadoPorId())));
        return toDTO(historialSolicitudRepository.save(s));
    }

    private SeguimientoSolicitudResponseDTO toDTO(HistorialSolicitud s) {
        return SeguimientoSolicitudResponseDTO.builder().idSeguimiento(s.getIdHistorial()).idSolicitud(s.getSolicitud().getIdSolicitud()).idPaso(s.getPasoFlujo() != null ? s.getPasoFlujo().getIdPaso() : null).idEtapa(s.getEtapa() != null ? s.getEtapa().getIdEtapa() : null).estado(s.getEstado()).procesadoPorId(s.getProcesadoPor() != null ? s.getProcesadoPor().getIdUsuario() : null).comentarios(s.getComentarios()).fechaEntrada(s.getFechaEntrada()).fechaCompletado(s.getFechaCompletado()).slaExcedido(s.getSlaExcedido()).build();
    }
}

