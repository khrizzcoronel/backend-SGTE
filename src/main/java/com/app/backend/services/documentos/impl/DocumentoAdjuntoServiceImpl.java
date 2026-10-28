package com.app.backend.services.documentos.impl;

import com.app.backend.dtos.documentos.response.DocumentoAdjuntoDTO;
import com.app.backend.entities.documentos.DocumentoAdjunto;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.documentos.DocumentoAdjuntoRepository;
import com.app.backend.repositories.tramites.RequisitoPlantillaRepository;
import com.app.backend.repositories.tramites.SolicitudRepository;
import com.app.backend.repositories.sistema.UsuarioRepository;
import com.app.backend.services.documentos.DocumentoAdjuntoService;
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
// Implementa operaciones CRUD de adjuntos vinculados al tramite.
public class DocumentoAdjuntoServiceImpl implements DocumentoAdjuntoService {

    private final DocumentoAdjuntoRepository documentoAdjuntoRepository;
    private final SolicitudRepository solicitudRepository;
    private final RequisitoPlantillaRepository requisitoPlantillaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override @Transactional(readOnly = true)
    public List<DocumentoAdjuntoDTO> listarPorSolicitud(@NonNull Integer idSolicitud) { return documentoAdjuntoRepository.findBySolicitudIdSolicitud(idSolicitud).stream().map(this::toDTO).toList(); }

    @Override @Transactional(readOnly = true)
    public DocumentoAdjuntoDTO obtenerPorId(@NonNull Integer id) { return toDTO(documentoAdjuntoRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Documento adjunto no encontrado con id: " + id))); }

    @Override
    public DocumentoAdjuntoDTO crear(DocumentoAdjuntoDTO dto) {
        DocumentoAdjunto d = DocumentoAdjunto.builder()
                .solicitud(solicitudRepository.findById(dto.getIdSolicitud()).orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada: " + dto.getIdSolicitud())))
                .nombreArchivo(dto.getNombreArchivo()).nombreOriginal(dto.getNombreOriginal()).rutaArchivo(dto.getRutaArchivo())
                .tamanoBytes(dto.getTamanoBytes()).tipoMime(dto.getTipoMime()).checksum(dto.getChecksum())
                .esValido(dto.getEsValido()).mensajeValidacion(dto.getMensajeValidacion())
                .fechaSubida(dto.getFechaSubida() != null ? dto.getFechaSubida() : LocalDateTime.now())
                .build();
        if (dto.getIdRequisito() != null) d.setRequisitoPlantilla(requisitoPlantillaRepository.findById(dto.getIdRequisito()).orElseThrow(() -> new RecursoNoEncontradoException("Requisito no encontrado: " + dto.getIdRequisito())));
        if (dto.getSubidoPorId() != null) d.setSubidoPor(usuarioRepository.findById(dto.getSubidoPorId()).orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + dto.getSubidoPorId())));
        return toDTO(documentoAdjuntoRepository.save(d));
    }

    @Override
    public void eliminar(@NonNull Integer id) { if (!documentoAdjuntoRepository.existsById(id)) throw new RecursoNoEncontradoException("Documento adjunto no encontrado con id: " + id); documentoAdjuntoRepository.deleteById(id); }

    private DocumentoAdjuntoDTO toDTO(DocumentoAdjunto d) {
        return DocumentoAdjuntoDTO.builder().idDocumento(d.getIdDocumento()).idSolicitud(d.getSolicitud().getIdSolicitud()).idRequisito(d.getRequisitoPlantilla() != null ? d.getRequisitoPlantilla().getIdRequisito() : null).nombreArchivo(d.getNombreArchivo()).nombreOriginal(d.getNombreOriginal()).rutaArchivo(d.getRutaArchivo()).tamanoBytes(d.getTamanoBytes()).tipoMime(d.getTipoMime()).checksum(d.getChecksum()).esValido(d.getEsValido()).mensajeValidacion(d.getMensajeValidacion()).subidoPorId(d.getSubidoPor() != null ? d.getSubidoPor().getIdUsuario() : null).fechaSubida(d.getFechaSubida()).build();
    }
}
