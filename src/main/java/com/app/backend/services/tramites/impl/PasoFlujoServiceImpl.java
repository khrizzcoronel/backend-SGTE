package com.app.backend.services.tramites.impl;

import com.app.backend.dtos.tramites.response.PasoFlujoResponseDTO;
import com.app.backend.entities.tramites.PasoFlujo;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.tramites.FlujoTrabajoRepository;
import com.app.backend.repositories.tramites.EtapaRepository;
import com.app.backend.repositories.tramites.PasoFlujoRepository;
import com.app.backend.repositories.sistema.RolRepository;
import com.app.backend.repositories.sistema.UsuarioRepository;
import com.app.backend.services.tramites.PasoFlujoService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
// CRUD de pasos dentro de un flujo, incluyendo rol requerido y encargado.
public class PasoFlujoServiceImpl implements PasoFlujoService {

    private final PasoFlujoRepository pasoFlujoRepository;
    private final FlujoTrabajoRepository flujoTrabajoRepository;
    private final EtapaRepository etapaRepository;
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;

    @Override @Transactional(readOnly = true)
    public List<PasoFlujoResponseDTO> listarPorFlujo(@NonNull Integer idFlujo) { return pasoFlujoRepository.findByFlujoTrabajoIdFlujoOrderByOrdenPasoAsc(idFlujo).stream().map(this::toDTO).toList(); }

    @Override @Transactional(readOnly = true)
    public PasoFlujoResponseDTO obtenerPorId(@NonNull Integer id) { return toDTO(pasoFlujoRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Paso no encontrado con id: " + id))); }

    @Override
    public PasoFlujoResponseDTO crear(PasoFlujoResponseDTO dto) {
        PasoFlujo p = PasoFlujo.builder()
                .flujoTrabajo(flujoTrabajoRepository.findById(dto.getIdFlujo()).orElseThrow(() -> new RecursoNoEncontradoException("Flujo no encontrado: " + dto.getIdFlujo())))
                .etapa(etapaRepository.findById(dto.getIdEtapa()).orElseThrow(() -> new RecursoNoEncontradoException("Etapa no encontrada: " + dto.getIdEtapa())))
                .ordenPaso(dto.getOrdenPaso())
                .horasSla(dto.getHorasSla())
                .build();
        if (dto.getRolRequeridoId() != null) p.setRolRequerido(rolRepository.findById(dto.getRolRequeridoId()).orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado: " + dto.getRolRequeridoId())));
        if (dto.getIdUsuarioEncargado() != null) p.setUsuarioEncargado(usuarioRepository.findById(dto.getIdUsuarioEncargado()).orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + dto.getIdUsuarioEncargado())));
        return toDTO(pasoFlujoRepository.save(p));
    }

    @Override
    public PasoFlujoResponseDTO actualizar(@NonNull Integer id, PasoFlujoResponseDTO dto) {
        PasoFlujo p = pasoFlujoRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Paso no encontrado con id: " + id));
        p.setOrdenPaso(dto.getOrdenPaso()); p.setHorasSla(dto.getHorasSla());
        if (dto.getIdEtapa() != null) p.setEtapa(etapaRepository.findById(dto.getIdEtapa()).orElseThrow(() -> new RecursoNoEncontradoException("Etapa no encontrada: " + dto.getIdEtapa())));
        if (dto.getRolRequeridoId() != null) p.setRolRequerido(rolRepository.findById(dto.getRolRequeridoId()).orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado: " + dto.getRolRequeridoId())));
        if (dto.getIdUsuarioEncargado() != null) p.setUsuarioEncargado(usuarioRepository.findById(dto.getIdUsuarioEncargado()).orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + dto.getIdUsuarioEncargado())));
        return toDTO(pasoFlujoRepository.save(p));
    }

    @Override
    public void eliminar(@NonNull Integer id) { if (!pasoFlujoRepository.existsById(id)) throw new RecursoNoEncontradoException("Paso no encontrado con id: " + id); pasoFlujoRepository.deleteById(id); }

    private PasoFlujoResponseDTO toDTO(PasoFlujo p) {
        return PasoFlujoResponseDTO.builder().idPaso(p.getIdPaso()).idFlujo(p.getFlujoTrabajo().getIdFlujo()).idEtapa(p.getEtapa().getIdEtapa()).ordenPaso(p.getOrdenPaso()).rolRequeridoId(p.getRolRequerido() != null ? p.getRolRequerido().getIdRol() : null).idUsuarioEncargado(p.getUsuarioEncargado() != null ? p.getUsuarioEncargado().getIdUsuario() : null).horasSla(p.getHorasSla()).build();
    }
}

