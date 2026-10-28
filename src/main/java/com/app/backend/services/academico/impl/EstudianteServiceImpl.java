package com.app.backend.services.academico.impl;

import com.app.backend.dtos.academico.response.EstudianteDTO;
import com.app.backend.entities.academico.Carrera;
import com.app.backend.entities.academico.Estudiante;
import com.app.backend.entities.sistema.Usuario;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.academico.CarreraRepository;
import com.app.backend.repositories.academico.EstudianteRepository;
import com.app.backend.repositories.academico.PeriodoRepository;
import com.app.backend.repositories.sistema.UsuarioRepository;
import com.app.backend.services.academico.EstudianteService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final UsuarioRepository usuarioRepository;
    private final CarreraRepository carreraRepository;
    private final PeriodoRepository periodoRepository;

    @Override @Transactional(readOnly = true)
    public List<EstudianteDTO> listarTodos() { return estudianteRepository.findAll().stream().map(this::toDTO).toList(); }

    @Override @Transactional(readOnly = true)
    public List<EstudianteDTO> listarPorCarrera(@NonNull Integer idCarrera) { return estudianteRepository.findByCarreraIdCarrera(idCarrera).stream().map(this::toDTO).toList(); }

    @Override @Transactional(readOnly = true)
    public EstudianteDTO obtenerPorId(@NonNull Integer id) { return toDTO(estudianteRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con id: " + id))); }

    @Override @Transactional(readOnly = true)
    public EstudianteDTO obtenerPorUsuario(@NonNull Integer idUsuario) { return toDTO(estudianteRepository.findByUsuarioIdUsuario(idUsuario).orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado para usuario: " + idUsuario))); }

    @Override
    public EstudianteDTO crear(EstudianteDTO dto) {
        Usuario u = usuarioRepository.findById(dto.getIdUsuario()).orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + dto.getIdUsuario()));
        Carrera c = carreraRepository.findById(dto.getIdCarrera()).orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada: " + dto.getIdCarrera()));
        Estudiante e = Estudiante.builder().usuario(u).carrera(c).paralelo(dto.getParalelo()).estadoAcademico(dto.getEstadoAcademico()).fechaMatricula(dto.getFechaMatricula()).esExterno(dto.getEsExterno()).idEstudianteSga(dto.getIdEstudianteSga()).build();
        if (dto.getIdPeriodo() != null) { e.setPeriodo(periodoRepository.findById(dto.getIdPeriodo()).orElseThrow(() -> new RecursoNoEncontradoException("Periodo no encontrado: " + dto.getIdPeriodo()))); }
        return toDTO(estudianteRepository.save(e));
    }

    @Override
    public EstudianteDTO actualizar(@NonNull Integer id, EstudianteDTO dto) {
        Estudiante e = estudianteRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con id: " + id));
        e.setParalelo(dto.getParalelo()); e.setEstadoAcademico(dto.getEstadoAcademico()); e.setFechaMatricula(dto.getFechaMatricula()); e.setEsExterno(dto.getEsExterno()); e.setIdEstudianteSga(dto.getIdEstudianteSga());
        if (dto.getIdCarrera() != null) { e.setCarrera(carreraRepository.findById(dto.getIdCarrera()).orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada: " + dto.getIdCarrera()))); }
        if (dto.getIdPeriodo() != null) { e.setPeriodo(periodoRepository.findById(dto.getIdPeriodo()).orElseThrow(() -> new RecursoNoEncontradoException("Periodo no encontrado: " + dto.getIdPeriodo()))); }
        return toDTO(estudianteRepository.save(e));
    }

    @Override
    public void eliminar(@NonNull Integer id) { if (!estudianteRepository.existsById(id)) throw new RecursoNoEncontradoException("Estudiante no encontrado con id: " + id); estudianteRepository.deleteById(id); }

    private EstudianteDTO toDTO(Estudiante e) {
        return EstudianteDTO.builder().idEstudiante(e.getIdEstudiante()).idUsuario(e.getUsuario().getIdUsuario()).idCarrera(e.getCarrera().getIdCarrera()).idPeriodo(e.getPeriodo() != null ? e.getPeriodo().getIdPeriodo() : null).paralelo(e.getParalelo()).estadoAcademico(e.getEstadoAcademico()).fechaMatricula(e.getFechaMatricula()).esExterno(e.getEsExterno()).idEstudianteSga(e.getIdEstudianteSga()).ultimaSincronizacion(e.getUltimaSincronizacion()).build();
    }
}
