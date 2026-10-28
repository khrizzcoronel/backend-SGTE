package com.app.backend.services.tramites.impl;

import com.app.backend.dtos.tramites.response.PlazoPlantillaResponseDTO;
import com.app.backend.entities.tramites.VentanaRecepcion;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.tramites.VentanaRecepcionRepository;
import com.app.backend.repositories.tramites.PlantillaTramiteRepository;
import com.app.backend.services.tramites.PlazoTramiteService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
// Administra ventanas de recepcion y reglas de extension por plantilla.
public class PlazoTramiteServiceImpl implements PlazoTramiteService {

    private final VentanaRecepcionRepository ventanaRecepcionRepository;
    private final PlantillaTramiteRepository plantillaTramiteRepository;

    @Override @Transactional(readOnly = true)
    public List<PlazoPlantillaResponseDTO> listarPorPlantilla(@NonNull Integer idPlantilla) { return ventanaRecepcionRepository.findByPlantillaIdPlantilla(idPlantilla).stream().map(this::toDTO).toList(); }

    @Override @Transactional(readOnly = true)
    public PlazoPlantillaResponseDTO obtenerPorId(@NonNull Integer id) { return toDTO(ventanaRecepcionRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Ventana no encontrada con id: " + id))); }

    @Override
    public PlazoPlantillaResponseDTO crear(PlazoPlantillaResponseDTO dto) {
        VentanaRecepcion p = VentanaRecepcion.builder()
                .plantilla(plantillaTramiteRepository.findById(dto.getIdPlantilla()).orElseThrow(() -> new RecursoNoEncontradoException("Plantilla no encontrada: " + dto.getIdPlantilla())))
                .fechaApertura(dto.getFechaApertura()).fechaCierre(dto.getFechaCierre())
                .permiteExtension(dto.getPermiteExtension() != null ? dto.getPermiteExtension() : false)
                .diasMaxExtension(dto.getDiasMaxExtension() != null ? dto.getDiasMaxExtension() : 0).build();
        return toDTO(ventanaRecepcionRepository.save(p));
    }

    @Override
    public PlazoPlantillaResponseDTO actualizar(@NonNull Integer id, PlazoPlantillaResponseDTO dto) {
        VentanaRecepcion p = ventanaRecepcionRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Ventana no encontrada con id: " + id));
        p.setFechaApertura(dto.getFechaApertura()); p.setFechaCierre(dto.getFechaCierre()); p.setPermiteExtension(dto.getPermiteExtension()); p.setDiasMaxExtension(dto.getDiasMaxExtension());
        return toDTO(ventanaRecepcionRepository.save(p));
    }

    @Override
    public void eliminar(@NonNull Integer id) { if (!ventanaRecepcionRepository.existsById(id)) throw new RecursoNoEncontradoException("Ventana no encontrada con id: " + id); ventanaRecepcionRepository.deleteById(id); }

    private PlazoPlantillaResponseDTO toDTO(VentanaRecepcion p) { return PlazoPlantillaResponseDTO.builder().idPlazo(p.getIdVentana()).idPlantilla(p.getPlantilla().getIdPlantilla()).fechaApertura(p.getFechaApertura()).fechaCierre(p.getFechaCierre()).permiteExtension(p.getPermiteExtension()).diasMaxExtension(p.getDiasMaxExtension()).build(); }
}

