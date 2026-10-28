package com.app.backend.services.academico.impl;

import com.app.backend.dtos.academico.response.CarreraDTO;
import com.app.backend.entities.academico.Carrera;
import com.app.backend.entities.academico.Facultad;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.academico.CarreraRepository;
import com.app.backend.repositories.academico.FacultadRepository;
import com.app.backend.services.academico.CarreraService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
public class CarreraServiceImpl implements CarreraService {

    private final CarreraRepository carreraRepository;
    private final FacultadRepository facultadRepository;

    @Override @Transactional(readOnly = true)
    public List<CarreraDTO> listarTodas() { return carreraRepository.findAll().stream().map(this::toDTO).toList(); }

    @Override @Transactional(readOnly = true)
    public List<CarreraDTO> listarPorFacultad(@NonNull Integer idFacultad) { return carreraRepository.findByFacultadIdFacultad(idFacultad).stream().map(this::toDTO).toList(); }

    @Override @Transactional(readOnly = true)
    public CarreraDTO obtenerPorId(@NonNull Integer id) { return toDTO(carreraRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada con id: " + id))); }

    @Override
    public CarreraDTO crear(CarreraDTO dto) {
        Facultad f = facultadRepository.findById(dto.getIdFacultad() ).orElseThrow(() -> new RecursoNoEncontradoException("Facultad no encontrada: " + dto.getIdFacultad()));
        Carrera c = Carrera.builder().nombreCarrera(dto.getNombreCarrera()).codigoCarrera(dto.getCodigoCarrera()).facultad(f).build();
        return toDTO(carreraRepository.save(c));
    }

    @Override
    public CarreraDTO actualizar(@NonNull Integer id, CarreraDTO dto) {
        Carrera c = carreraRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada con id: " + id));
        c.setNombreCarrera(dto.getNombreCarrera());
        c.setCodigoCarrera(dto.getCodigoCarrera());
        if (dto.getIdFacultad() != null) {
            Facultad f = facultadRepository.findById(dto.getIdFacultad()).orElseThrow(() -> new RecursoNoEncontradoException("Facultad no encontrada: " + dto.getIdFacultad()));
            c.setFacultad(f);
        }
        return toDTO(carreraRepository.save(c));
    }

    @Override
    public void eliminar(@NonNull Integer id) { if (!carreraRepository.existsById(id)) throw new RecursoNoEncontradoException("Carrera no encontrada con id: " + id); carreraRepository.deleteById(id); }

    private CarreraDTO toDTO(Carrera c) { return CarreraDTO.builder().idCarrera(c.getIdCarrera()).nombreCarrera(c.getNombreCarrera()).codigoCarrera(c.getCodigoCarrera()).idFacultad(c.getFacultad().getIdFacultad()).build(); }
}
