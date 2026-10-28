package com.app.backend.services.academico.impl;

import com.app.backend.dtos.academico.response.SemestreDTO;
import com.app.backend.entities.academico.Periodo;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.academico.PeriodoRepository;
import com.app.backend.services.academico.SemestreService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
public class SemestreServiceImpl implements SemestreService {

    private final PeriodoRepository periodoRepository;

    @Override @Transactional(readOnly = true)
    public List<SemestreDTO> listarTodos() { return periodoRepository.findAll().stream().map(this::toDTO).toList(); }

    @Override @Transactional(readOnly = true)
    public SemestreDTO obtenerPorId(@NonNull Integer id) { return toDTO(periodoRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Periodo no encontrado con id: " + id))); }

    @Override @Transactional(readOnly = true)
    public SemestreDTO obtenerActivo() { return toDTO(periodoRepository.findByEstaActivoTrue().orElseThrow(() -> new RecursoNoEncontradoException("No hay periodo activo"))); }

    @Override
    public SemestreDTO crear(SemestreDTO dto) {
        Periodo s = Periodo.builder().codigoPeriodo(dto.getCodigoPeriodo()).nombrePeriodo(dto.getNombrePeriodo()).fechaInicio(dto.getFechaInicio()).fechaFin(dto.getFechaFin()).estaActivo(dto.getEstaActivo() != null ? dto.getEstaActivo() : true).build();
        return toDTO(periodoRepository.save(s));
    }

    @Override
    public SemestreDTO actualizar(@NonNull Integer id, SemestreDTO dto) {
        Periodo s = periodoRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Periodo no encontrado con id: " + id));
        s.setCodigoPeriodo(dto.getCodigoPeriodo()); s.setNombrePeriodo(dto.getNombrePeriodo()); s.setFechaInicio(dto.getFechaInicio()); s.setFechaFin(dto.getFechaFin()); s.setEstaActivo(dto.getEstaActivo());
        return toDTO(periodoRepository.save(s));
    }

    @Override
    public void eliminar(@NonNull Integer id) { if (!periodoRepository.existsById(id)) throw new RecursoNoEncontradoException("Periodo no encontrado con id: " + id); periodoRepository.deleteById(id); }

    private SemestreDTO toDTO(Periodo s) { return SemestreDTO.builder().idSemestre(s.getIdPeriodo()).codigoPeriodo(s.getCodigoPeriodo()).nombrePeriodo(s.getNombrePeriodo()).fechaInicio(s.getFechaInicio()).fechaFin(s.getFechaFin()).estaActivo(s.getEstaActivo()).build(); }
}
