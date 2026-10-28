package com.app.backend.services.documentos.impl;

import com.app.backend.dtos.documentos.response.DocumentoGeneradoDTO;
import com.app.backend.entities.documentos.DocumentoGenerado;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.documentos.DocumentoGeneradoRepository;
import com.app.backend.repositories.sistema.UsuarioRepository;
import com.app.backend.repositories.tramites.SolicitudRepository;
import com.app.backend.services.documentos.DocumentoGeneradoService;
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
// Implementa operaciones CRUD de documentos finales generados por el flujo.
public class DocumentoGeneradoServiceImpl implements DocumentoGeneradoService {

    private final DocumentoGeneradoRepository documentoGeneradoRepository;
    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    @Override @Transactional(readOnly = true)
    public List<DocumentoGeneradoDTO> listarPorSolicitud(@NonNull Integer idSolicitud) { return documentoGeneradoRepository.findBySolicitudIdSolicitud(idSolicitud).stream().map(this::toDTO).toList(); }

    @Override @Transactional(readOnly = true)
    public DocumentoGeneradoDTO obtenerPorId(@NonNull Integer id) { return toDTO(documentoGeneradoRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Documento generado no encontrado con id: " + id))); }

    @Override
    public DocumentoGeneradoDTO crear(DocumentoGeneradoDTO dto) {
        DocumentoGenerado d = DocumentoGenerado.builder()
                .solicitud(solicitudRepository.findById(dto.getIdSolicitud()).orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada: " + dto.getIdSolicitud())))
                .tipoDocumento(dto.getTipoDocumento()).codigoDocumento(dto.getCodigoDocumento()).rutaArchivo(dto.getRutaArchivo())
                .plantillaUsada(dto.getPlantillaUsada()).firmaDigital(dto.getFirmaDigital())
                .esOficial(dto.getEsOficial() != null ? dto.getEsOficial() : false)
                .fechaGeneracion(dto.getFechaGeneracion() != null ? dto.getFechaGeneracion() : LocalDateTime.now())
                .validoHasta(dto.getValidoHasta())
                .build();
        if (dto.getGeneradoPorId() != null) d.setGeneradoPor(usuarioRepository.findById(dto.getGeneradoPorId()).orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + dto.getGeneradoPorId())));
        if (dto.getFirmadoPorId() != null) d.setFirmadoPor(usuarioRepository.findById(dto.getFirmadoPorId()).orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + dto.getFirmadoPorId())));
        return toDTO(documentoGeneradoRepository.save(d));
    }

    @Override
    public void eliminar(@NonNull Integer id) { if (!documentoGeneradoRepository.existsById(id)) throw new RecursoNoEncontradoException("Documento generado no encontrado con id: " + id); documentoGeneradoRepository.deleteById(id); }

    private DocumentoGeneradoDTO toDTO(DocumentoGenerado d) {
        return DocumentoGeneradoDTO.builder().idGenerado(d.getIdGenerado()).idSolicitud(d.getSolicitud().getIdSolicitud()).tipoDocumento(d.getTipoDocumento()).codigoDocumento(d.getCodigoDocumento()).rutaArchivo(d.getRutaArchivo()).plantillaUsada(d.getPlantillaUsada()).generadoPorId(d.getGeneradoPor() != null ? d.getGeneradoPor().getIdUsuario() : null).firmadoPorId(d.getFirmadoPor() != null ? d.getFirmadoPor().getIdUsuario() : null).firmaDigital(d.getFirmaDigital()).esOficial(d.getEsOficial()).fechaGeneracion(d.getFechaGeneracion()).validoHasta(d.getValidoHasta()).build();
    }
}
