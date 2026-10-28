package com.app.backend.services.tramites.impl;

import com.app.backend.dtos.tramites.response.EtapaProcesamientoResponseDTO;
import com.app.backend.entities.tramites.Etapa;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.tramites.EtapaRepository;
import com.app.backend.services.tramites.EtapaProcesamientoService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
// Catalogo de etapas de procesamiento reutilizadas por los flujos.
public class EtapaProcesamientoServiceImpl implements EtapaProcesamientoService {

    private final EtapaRepository etapaRepository;

    @Override @Transactional(readOnly = true)
    public List<EtapaProcesamientoResponseDTO> listarTodas() { return etapaRepository.findAll().stream().map(this::toDTO).toList(); }

    @Override @Transactional(readOnly = true)
    public EtapaProcesamientoResponseDTO obtenerPorId(@NonNull Integer id) { return toDTO(etapaRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Etapa no encontrada con id: " + id))); }

    @Override
    public EtapaProcesamientoResponseDTO crear(EtapaProcesamientoResponseDTO dto) {
        Etapa e = Etapa.builder().nombreEtapa(dto.getNombreEtapa()).descripcion(dto.getDescripcionEtapa()).codigo(dto.getCodigoEtapa()).build();
        return toDTO(etapaRepository.save(e));
    }

    @Override
    public EtapaProcesamientoResponseDTO actualizar(@NonNull Integer id, EtapaProcesamientoResponseDTO dto) {
        Etapa e = etapaRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Etapa no encontrada con id: " + id));
        e.setNombreEtapa(dto.getNombreEtapa()); e.setDescripcion(dto.getDescripcionEtapa()); e.setCodigo(dto.getCodigoEtapa());
        return toDTO(etapaRepository.save(e));
    }

    @Override
    public void eliminar(@NonNull Integer id) { if (!etapaRepository.existsById(id)) throw new RecursoNoEncontradoException("Etapa no encontrada con id: " + id); etapaRepository.deleteById(id); }

    private EtapaProcesamientoResponseDTO toDTO(Etapa e) { return EtapaProcesamientoResponseDTO.builder().idEtapa(e.getIdEtapa()).nombreEtapa(e.getNombreEtapa()).descripcionEtapa(e.getDescripcion()).codigoEtapa(e.getCodigo()).build(); }
}

