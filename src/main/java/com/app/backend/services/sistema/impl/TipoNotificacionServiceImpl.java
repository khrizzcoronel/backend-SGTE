package com.app.backend.services.sistema.impl;

import com.app.backend.dtos.sistema.request.TipoNotificacionRequestDTO;
import com.app.backend.dtos.sistema.response.TipoNotificacionResponseDTO;
import com.app.backend.entities.sistema.TipoNotificacion;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.sistema.TipoNotificacionRepository;
import com.app.backend.services.sistema.TipoNotificacionService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
public class TipoNotificacionServiceImpl implements TipoNotificacionService {

    private final TipoNotificacionRepository tipoNotificacionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TipoNotificacionResponseDTO> listarTodos() {
        return tipoNotificacionRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TipoNotificacionResponseDTO obtenerPorId(@NonNull Integer id) {
        return toDTO(tipoNotificacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tipo notificación no encontrado con id: " + id)));
    }

    @Override
    public TipoNotificacionResponseDTO crear(TipoNotificacionRequestDTO dto) {
        TipoNotificacion tipo = TipoNotificacion.builder()
                .codigoTipo(dto.getCodigoTipo())
                .nombreTipo(dto.getNombreTipo())
                .plantillaDefecto(dto.getPlantillaDefecto())
                .estaActivo(dto.getEstaActivo() != null ? dto.getEstaActivo() : true)
                .build();
        return toDTO(tipoNotificacionRepository.save(tipo));
    }

    @Override
    public TipoNotificacionResponseDTO actualizar(@NonNull Integer id, TipoNotificacionRequestDTO dto) {
        TipoNotificacion tipo = tipoNotificacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tipo notificación no encontrado con id: " + id));
        tipo.setCodigoTipo(dto.getCodigoTipo());
        tipo.setNombreTipo(dto.getNombreTipo());
        tipo.setPlantillaDefecto(dto.getPlantillaDefecto());
        tipo.setEstaActivo(dto.getEstaActivo());
        return toDTO(tipoNotificacionRepository.save(tipo));
    }

    @Override
    public void eliminar(@NonNull Integer id) {
        if (!tipoNotificacionRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Tipo notificación no encontrado con id: " + id);
        }
        tipoNotificacionRepository.deleteById(id);
    }

    private TipoNotificacionResponseDTO toDTO(TipoNotificacion t) {
        return TipoNotificacionResponseDTO.builder()
                .idTipo(t.getIdTipo())
                .codigoTipo(t.getCodigoTipo())
                .nombreTipo(t.getNombreTipo())
                .plantillaDefecto(t.getPlantillaDefecto())
                .estaActivo(t.getEstaActivo())
                .build();
    }
}
