package com.app.backend.services.academico.impl;

import com.app.backend.dtos.academico.response.FacultadDTO;
import com.app.backend.entities.academico.Facultad;
import com.app.backend.entities.academico.Universidad;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.academico.FacultadRepository;
import com.app.backend.repositories.academico.UniversidadRepository;
import com.app.backend.services.academico.FacultadService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
public class FacultadServiceImpl implements FacultadService {

    private final FacultadRepository facultadRepository;
    private final UniversidadRepository universidadRepository;

    @Override @Transactional(readOnly = true)
    public List<FacultadDTO> listarTodas() { return facultadRepository.findAll().stream().map(this::toDTO).toList(); }

    @Override @Transactional(readOnly = true)
    public List<FacultadDTO> listarPorUniversidad(@NonNull Integer idUniversidad) { return facultadRepository.findByUniversidadIdUniversidad(idUniversidad).stream().map(this::toDTO).toList(); }

    @Override @Transactional(readOnly = true)
    public FacultadDTO obtenerPorId(@NonNull Integer id) { return toDTO(facultadRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Facultad no encontrada con id: " + id))); }

    @Override
    public FacultadDTO crear(FacultadDTO dto) {
        Universidad u = universidadRepository.findById(dto.getIdUniversidad()).orElseThrow(() -> new RecursoNoEncontradoException("Universidad no encontrada: " + dto.getIdUniversidad()));
        Facultad f = Facultad.builder().nombreFacultad(dto.getNombreFacultad()).ubicacionOficina(dto.getUbicacionOficina()).emailFacultad(dto.getEmailFacultad()).universidad(u).build();
        return toDTO(facultadRepository.save(f));
    }

    @Override
    public FacultadDTO actualizar(@NonNull Integer id, FacultadDTO dto) {
        Facultad f = facultadRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Facultad no encontrada con id: " + id));
        f.setNombreFacultad(dto.getNombreFacultad());
        f.setUbicacionOficina(dto.getUbicacionOficina());
        f.setEmailFacultad(dto.getEmailFacultad());
        if (dto.getIdUniversidad() != null) {
            Universidad u = universidadRepository.findById(dto.getIdUniversidad()).orElseThrow(() -> new RecursoNoEncontradoException("Universidad no encontrada: " + dto.getIdUniversidad()));
            f.setUniversidad(u);
        }
        return toDTO(facultadRepository.save(f));
    }

    @Override
    public void eliminar(@NonNull Integer id) { if (!facultadRepository.existsById(id)) throw new RecursoNoEncontradoException("Facultad no encontrada con id: " + id); facultadRepository.deleteById(id); }

    private FacultadDTO toDTO(Facultad f) { return FacultadDTO.builder().idFacultad(f.getIdFacultad()).nombreFacultad(f.getNombreFacultad()).ubicacionOficina(f.getUbicacionOficina()).emailFacultad(f.getEmailFacultad()).idUniversidad(f.getUniversidad().getIdUniversidad()).build(); }
}
