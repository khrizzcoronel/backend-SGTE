package com.app.backend.services.academico.impl;

import com.app.backend.dtos.academico.response.DecanoDTO;
import com.app.backend.entities.academico.Decano;
import com.app.backend.entities.sistema.Usuario;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.academico.DecanoRepository;
import com.app.backend.repositories.academico.FacultadRepository;
import com.app.backend.repositories.sistema.UsuarioRepository;
import com.app.backend.services.academico.DecanoService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
public class DecanoServiceImpl implements DecanoService {

    private final DecanoRepository decanoRepository;
    private final UsuarioRepository usuarioRepository;
    private final FacultadRepository facultadRepository;

    @Override @Transactional(readOnly = true) public List<DecanoDTO> listarTodos() { return decanoRepository.findAll().stream().map(this::toDTO).toList(); }
    @Override @Transactional(readOnly = true) public List<DecanoDTO> listarActivos() { return decanoRepository.findByEstaActivoTrue().stream().map(this::toDTO).toList(); }
    @Override @Transactional(readOnly = true) public DecanoDTO obtenerPorId(@NonNull Integer id) { return toDTO(decanoRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Decano no encontrado con id: " + id))); }

    @Override
    public DecanoDTO crear(DecanoDTO dto) {
        Usuario u = usuarioRepository.findById(dto.getIdUsuario()).orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + dto.getIdUsuario()));
        Decano d = Decano.builder().usuario(u).fechaNombramiento(dto.getFechaNombramiento()).estaActivo(dto.getEstaActivo() != null ? dto.getEstaActivo() : true).idDecanoSga(dto.getIdDecanoSga()).build();
        if (dto.getIdFacultad() != null) { d.setFacultad(facultadRepository.findById(dto.getIdFacultad()).orElseThrow(() -> new RecursoNoEncontradoException("Facultad no encontrada: " + dto.getIdFacultad()))); }
        return toDTO(decanoRepository.save(d));
    }

    @Override
    public DecanoDTO actualizar(@NonNull Integer id, DecanoDTO dto) {
        Decano d = decanoRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Decano no encontrado con id: " + id));
        d.setFechaNombramiento(dto.getFechaNombramiento()); d.setEstaActivo(dto.getEstaActivo()); d.setIdDecanoSga(dto.getIdDecanoSga());
        if (dto.getIdFacultad() != null) { d.setFacultad(facultadRepository.findById(dto.getIdFacultad()).orElseThrow(() -> new RecursoNoEncontradoException("Facultad no encontrada: " + dto.getIdFacultad()))); }
        return toDTO(decanoRepository.save(d));
    }

    @Override public void eliminar(@NonNull Integer id) { if (!decanoRepository.existsById(id)) throw new RecursoNoEncontradoException("Decano no encontrado con id: " + id); decanoRepository.deleteById(id); }

    private DecanoDTO toDTO(Decano d) { return DecanoDTO.builder().idDecano(d.getIdDecano()).idUsuario(d.getUsuario().getIdUsuario()).idFacultad(d.getFacultad() != null ? d.getFacultad().getIdFacultad() : null).fechaNombramiento(d.getFechaNombramiento()).estaActivo(d.getEstaActivo()).idDecanoSga(d.getIdDecanoSga()).build(); }
}
