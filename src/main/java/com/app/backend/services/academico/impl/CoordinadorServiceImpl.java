package com.app.backend.services.academico.impl;

import com.app.backend.dtos.academico.response.CoordinadorDTO;
import com.app.backend.entities.academico.Coordinador;
import com.app.backend.entities.sistema.Usuario;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.academico.CarreraRepository;
import com.app.backend.repositories.academico.CoordinadorRepository;
import com.app.backend.repositories.sistema.UsuarioRepository;
import com.app.backend.services.academico.CoordinadorService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
public class CoordinadorServiceImpl implements CoordinadorService {

    private final CoordinadorRepository coordinadorRepository;
    private final UsuarioRepository usuarioRepository;
    private final CarreraRepository carreraRepository;

    @Override @Transactional(readOnly = true) public List<CoordinadorDTO> listarTodos() { return coordinadorRepository.findAll().stream().map(this::toDTO).toList(); }
    @Override @Transactional(readOnly = true) public List<CoordinadorDTO> listarPorCarrera(@NonNull Integer idCarrera) { return coordinadorRepository.findByCarreraIdCarrera(idCarrera).stream().map(this::toDTO).toList(); }
    @Override @Transactional(readOnly = true) public List<CoordinadorDTO> listarActivos() { return coordinadorRepository.findByEstaActivoTrue().stream().map(this::toDTO).toList(); }
    @Override @Transactional(readOnly = true) public CoordinadorDTO obtenerPorId(@NonNull Integer id) { return toDTO(coordinadorRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Coordinador no encontrado con id: " + id))); }

    @Override
    public CoordinadorDTO crear(CoordinadorDTO dto) {
        Usuario u = usuarioRepository.findById(dto.getIdUsuario()).orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + dto.getIdUsuario()));
        Coordinador c = Coordinador.builder().usuario(u).ubicacionOficina(dto.getUbicacionOficina()).horarioAtencion(dto.getHorarioAtencion()).estaActivo(dto.getEstaActivo() != null ? dto.getEstaActivo() : true).fechaNombramiento(dto.getFechaNombramiento()).idCoordinadorSga(dto.getIdCoordinadorSga()).build();
        if (dto.getIdCarrera() != null) { c.setCarrera(carreraRepository.findById(dto.getIdCarrera()).orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada: " + dto.getIdCarrera()))); }
        return toDTO(coordinadorRepository.save(c));
    }

    @Override
    public CoordinadorDTO actualizar(@NonNull Integer id, CoordinadorDTO dto) {
        Coordinador c = coordinadorRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Coordinador no encontrado con id: " + id));
        c.setUbicacionOficina(dto.getUbicacionOficina()); c.setHorarioAtencion(dto.getHorarioAtencion()); c.setEstaActivo(dto.getEstaActivo()); c.setFechaNombramiento(dto.getFechaNombramiento()); c.setIdCoordinadorSga(dto.getIdCoordinadorSga());
        if (dto.getIdCarrera() != null) { c.setCarrera(carreraRepository.findById(dto.getIdCarrera()).orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada: " + dto.getIdCarrera()))); }
        return toDTO(coordinadorRepository.save(c));
    }

    @Override public void eliminar(@NonNull Integer id) { if (!coordinadorRepository.existsById(id)) throw new RecursoNoEncontradoException("Coordinador no encontrado con id: " + id); coordinadorRepository.deleteById(id); }

    private CoordinadorDTO toDTO(Coordinador c) { return CoordinadorDTO.builder().idCoordinador(c.getIdCoordinador()).idUsuario(c.getUsuario().getIdUsuario()).idCarrera(c.getCarrera() != null ? c.getCarrera().getIdCarrera() : null).ubicacionOficina(c.getUbicacionOficina()).horarioAtencion(c.getHorarioAtencion()).estaActivo(c.getEstaActivo()).fechaNombramiento(c.getFechaNombramiento()).idCoordinadorSga(c.getIdCoordinadorSga()).build(); }
}
