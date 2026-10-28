package com.app.backend.services.tramites.impl;

import com.app.backend.dtos.tramites.request.AsignarPasoUsuarioRequestDTO;
import com.app.backend.dtos.tramites.request.EtapaRequestDTO;
import com.app.backend.dtos.tramites.request.FlujoTrabajoCompletoRequestDTO;
import com.app.backend.dtos.tramites.request.PasoFlujoCompletoRequestDTO;
import com.app.backend.dtos.tramites.response.EtapaProcesamientoResponseDTO;
import com.app.backend.dtos.tramites.response.FlujoTrabajoDetalleResponseDTO;
import com.app.backend.dtos.tramites.response.PasoFlujoDetalleResponseDTO;
import com.app.backend.entities.tramites.Etapa;
import com.app.backend.entities.tramites.FlujoTrabajo;
import com.app.backend.entities.tramites.PasoFlujo;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.sistema.RolRepository;
import com.app.backend.repositories.sistema.UsuarioRepository;
import com.app.backend.repositories.tramites.EtapaRepository;
import com.app.backend.repositories.tramites.FlujoTrabajoRepository;
import com.app.backend.repositories.tramites.PasoFlujoRepository;
import com.app.backend.services.tramites.GestionFlujoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
// Orquesta la gestion de flujos completos (flujo + pasos + responsables).
public class GestionFlujoServiceImpl implements GestionFlujoService {

    private final FlujoTrabajoRepository flujoTrabajoRepository;
    private final PasoFlujoRepository pasoFlujoRepository;
    private final EtapaRepository etapaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @Override
    @Transactional(readOnly = true)
    public List<FlujoTrabajoDetalleResponseDTO> listarFlujosCompletos() {
        return flujoTrabajoRepository.findAll().stream().map(this::toDetalle).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FlujoTrabajoDetalleResponseDTO obtenerFlujoCompleto(Integer idFlujo) {
        FlujoTrabajo flujo = flujoTrabajoRepository.findById(idFlujo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Flujo no encontrado con id: " + idFlujo));
        return toDetalle(flujo);
    }

    @Override
    public FlujoTrabajoDetalleResponseDTO crearFlujoCompleto(FlujoTrabajoCompletoRequestDTO request) {
        // Inserta primero el flujo y luego sus pasos para mantener integridad referencial.
        FlujoTrabajo flujo = FlujoTrabajo.builder()
                .nombreFlujo(request.getNombreFlujo())
                .descripcion(request.getDescripcionFlujo())
                .estaActivo(request.getEstaActivo() != null ? request.getEstaActivo() : true)
                .version(request.getVersion() != null ? request.getVersion() : 1)
                .build();

        if (request.getCreadoPorId() != null) {
            flujo.setCreadoPor(usuarioRepository.findById(request.getCreadoPorId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + request.getCreadoPorId())));
        }

        FlujoTrabajo flujoGuardado = flujoTrabajoRepository.save(flujo);
        List<PasoFlujo> pasos = new ArrayList<>();

        if (request.getPasos() != null) {
            for (PasoFlujoCompletoRequestDTO pasoRequest : request.getPasos()) {
                pasos.add(crearPaso(flujoGuardado, pasoRequest));
            }
        }

        if (!pasos.isEmpty()) {
            pasoFlujoRepository.saveAll(pasos);
        }

        return toDetalle(flujoGuardado);
    }

    @Override
    public PasoFlujoDetalleResponseDTO asignarUsuarioPaso(Integer idPaso, AsignarPasoUsuarioRequestDTO request) {
        PasoFlujo paso = pasoFlujoRepository.findById(idPaso)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paso no encontrado con id: " + idPaso));

        paso.setUsuarioEncargado(usuarioRepository.findById(request.getIdUsuarioEncargado())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + request.getIdUsuarioEncargado())));

        return toPasoDetalle(pasoFlujoRepository.save(paso));
    }

    private PasoFlujo crearPaso(FlujoTrabajo flujo, PasoFlujoCompletoRequestDTO request) {
        // Permite usar una etapa existente o crearla en linea dentro del alta del flujo.
        Etapa etapa = obtenerOCrearEtapa(request.getIdEtapa(), request.getEtapa());

        PasoFlujo paso = PasoFlujo.builder()
                .flujoTrabajo(flujo)
                .etapa(etapa)
                .ordenPaso(request.getOrdenPaso())
                .horasSla(request.getHorasSla())
                .build();

        if (request.getRolRequeridoId() != null) {
            paso.setRolRequerido(rolRepository.findById(request.getRolRequeridoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado: " + request.getRolRequeridoId())));
        }

        if (request.getIdUsuarioEncargado() != null) {
            paso.setUsuarioEncargado(usuarioRepository.findById(request.getIdUsuarioEncargado())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + request.getIdUsuarioEncargado())));
        }

        return paso;
    }

    private Etapa obtenerOCrearEtapa(Integer idEtapa, EtapaRequestDTO request) {
        if (idEtapa != null) {
            return etapaRepository.findById(idEtapa)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Etapa no encontrada: " + idEtapa));
        }

        if (request == null) {
            throw new IllegalArgumentException("Debes enviar idEtapa o la definición de una nueva etapa");
        }

        Etapa etapa = Etapa.builder()
                .nombreEtapa(request.getNombreEtapa())
                .descripcion(request.getDescripcionEtapa())
                .codigo(request.getCodigoEtapa())
                .build();
        return etapaRepository.save(etapa);
    }

    private FlujoTrabajoDetalleResponseDTO toDetalle(FlujoTrabajo flujo) {
        List<PasoFlujoDetalleResponseDTO> pasos = pasoFlujoRepository
                .findByFlujoTrabajoIdFlujoOrderByOrdenPasoAsc(flujo.getIdFlujo())
                .stream().map(this::toPasoDetalle).toList();

        return FlujoTrabajoDetalleResponseDTO.builder()
                .idFlujo(flujo.getIdFlujo())
                .nombreFlujo(flujo.getNombreFlujo())
                .descripcionFlujo(flujo.getDescripcion())
                .estaActivo(flujo.getEstaActivo())
                .version(flujo.getVersion())
                .creadoPorId(flujo.getCreadoPor() != null ? flujo.getCreadoPor().getIdUsuario() : null)
                .pasos(pasos)
                .build();
    }

    private PasoFlujoDetalleResponseDTO toPasoDetalle(PasoFlujo paso) {
        return PasoFlujoDetalleResponseDTO.builder()
                .idPaso(paso.getIdPaso())
                .ordenPaso(paso.getOrdenPaso())
                .horasSla(paso.getHorasSla())
                .rolRequeridoId(paso.getRolRequerido() != null ? paso.getRolRequerido().getIdRol() : null)
                .rolRequerido(paso.getRolRequerido() != null ? paso.getRolRequerido().getNombreRol() : null)
                .idUsuarioEncargado(paso.getUsuarioEncargado() != null ? paso.getUsuarioEncargado().getIdUsuario() : null)
                .usuarioEncargado(formatearUsuario(paso.getUsuarioEncargado()))
                .etapa(EtapaProcesamientoResponseDTO.builder()
                        .idEtapa(paso.getEtapa().getIdEtapa())
                        .nombreEtapa(paso.getEtapa().getNombreEtapa())
                        .descripcionEtapa(paso.getEtapa().getDescripcion())
                        .codigoEtapa(paso.getEtapa().getCodigo())
                        .build())
                .build();
    }

    private String formatearUsuario(com.app.backend.entities.sistema.Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        if (usuario.getCredencial() != null && usuario.getCredencial().getNombreUsuario() != null) {
            return usuario.getCredencial().getNombreUsuario();
        }
        String nombres = usuario.getNombres() != null ? usuario.getNombres() : "";
        String apellidos = usuario.getApellidos() != null ? usuario.getApellidos() : "";
        String nombreCompleto = (nombres + " " + apellidos).trim();
        return nombreCompleto.isEmpty() ? null : nombreCompleto;
    }
}