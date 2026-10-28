package com.app.backend.services.tramites.impl;

import com.app.backend.dtos.tramites.response.TransicionFlujoResponseDTO;
import com.app.backend.entities.tramites.TransicionFlujo;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.tramites.FlujoTrabajoRepository;
import com.app.backend.repositories.tramites.PasoFlujoRepository;
import com.app.backend.repositories.tramites.TransicionFlujoRepository;
import com.app.backend.services.tramites.TransicionFlujoService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
// Administra transiciones entre pasos de un flujo y sus banderas de negocio.
public class TransicionFlujoServiceImpl implements TransicionFlujoService {

    private final TransicionFlujoRepository transicionFlujoRepository;
    private final FlujoTrabajoRepository flujoTrabajoRepository;
    private final PasoFlujoRepository pasoFlujoRepository;

    @Override @Transactional(readOnly = true)
    public List<TransicionFlujoResponseDTO> listarPorFlujo(@NonNull Integer idFlujo) { return transicionFlujoRepository.findByFlujoTrabajoIdFlujo(idFlujo).stream().map(this::toDTO).toList(); }

    @Override
    public TransicionFlujoResponseDTO crear(TransicionFlujoResponseDTO dto) {
        TransicionFlujo t = TransicionFlujo.builder()
                .flujoTrabajo(flujoTrabajoRepository.findById(dto.getIdFlujo()).orElseThrow(() -> new RecursoNoEncontradoException("Flujo no encontrado: " + dto.getIdFlujo())))
                .pasoDestino(pasoFlujoRepository.findById(dto.getIdPasoDestino()).orElseThrow(() -> new RecursoNoEncontradoException("Paso destino no encontrado: " + dto.getIdPasoDestino())))
                .observacion(dto.getObservacion() != null ? dto.getObservacion() : false)
                .documentoGenerado(dto.getDocumentoGenerado() != null ? dto.getDocumentoGenerado() : false)
                .build();
        if (dto.getIdPasoOrigen() != null) t.setPasoOrigen(pasoFlujoRepository.findById(dto.getIdPasoOrigen()).orElseThrow(() -> new RecursoNoEncontradoException("Paso origen no encontrado: " + dto.getIdPasoOrigen())));
        return toDTO(transicionFlujoRepository.save(t));
    }

    @Override
    public void eliminar(@NonNull Integer id) { if (!transicionFlujoRepository.existsById(id)) throw new RecursoNoEncontradoException("Transicion no encontrada con id: " + id); transicionFlujoRepository.deleteById(id); }

    private TransicionFlujoResponseDTO toDTO(TransicionFlujo t) {
        return TransicionFlujoResponseDTO.builder().idTransicion(t.getIdTransicion()).idFlujo(t.getFlujoTrabajo().getIdFlujo()).idPasoOrigen(t.getPasoOrigen() != null ? t.getPasoOrigen().getIdPaso() : null).idPasoDestino(t.getPasoDestino().getIdPaso()).observacion(t.getObservacion()).documentoGenerado(t.getDocumentoGenerado()).build();
    }
}

