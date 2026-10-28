package com.app.backend.services.academico.impl;

import com.app.backend.dtos.academico.response.UniversidadDTO;
import com.app.backend.entities.academico.Universidad;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.academico.UniversidadRepository;
import com.app.backend.services.academico.UniversidadService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
public class UniversidadServiceImpl implements UniversidadService {

    private final UniversidadRepository universidadRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UniversidadDTO> listarTodas() {
        return universidadRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UniversidadDTO obtenerPorId(@NonNull Integer id) {
        return toDTO(universidadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Universidad no encontrada con id: " + id)));
    }

    @Override
    public UniversidadDTO crear(UniversidadDTO dto) {
        Universidad u = Universidad.builder()
                .nombreUniversidad(dto.getNombreUniversidad())
                .esPublica(dto.getEsPublica() != null ? dto.getEsPublica() : true)
                .build();
        return toDTO(universidadRepository.save(u));
    }

    @Override
    public UniversidadDTO actualizar(@NonNull Integer id, UniversidadDTO dto) {
        Universidad u = universidadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Universidad no encontrada con id: " + id));
        u.setNombreUniversidad(dto.getNombreUniversidad());
        u.setEsPublica(dto.getEsPublica());
        return toDTO(universidadRepository.save(u));
    }

    @Override
    public void eliminar(@NonNull Integer id) {
        if (!universidadRepository.existsById(id)) throw new RecursoNoEncontradoException("Universidad no encontrada con id: " + id);
        universidadRepository.deleteById(id);
    }

    private UniversidadDTO toDTO(Universidad u) {
        return UniversidadDTO.builder().idUniversidad(u.getIdUniversidad()).nombreUniversidad(u.getNombreUniversidad()).esPublica(u.getEsPublica()).build();
    }
}
