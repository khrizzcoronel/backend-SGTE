package com.app.backend.services.tramites.impl;

import com.app.backend.dtos.tramites.response.DetallesPlantillaBaseResponseDTO;
import com.app.backend.dtos.tramites.response.DetallesPlantillaResponseDTO;
import com.app.backend.dtos.tramites.response.PasoFlujoPlantillaResponseDTO;
import com.app.backend.dtos.tramites.response.RequisitoPlantillaResponseDTO;
import com.app.backend.entities.sistema.Usuario;
import com.app.backend.entities.tramites.PasoFlujo;
import com.app.backend.entities.tramites.RequisitoPlantilla;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.tramites.PasoFlujoRepository;
import com.app.backend.repositories.tramites.PlantillaTramiteRepository;
import com.app.backend.repositories.tramites.RequisitoPlantillaRepository;
import com.app.backend.services.tramites.DetallesTramiteService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SuppressWarnings("")
// Construye vistas ricas de plantilla (pasos + requisitos + ventana) para UI.
public class DetallesTramiteServiceImpl implements DetallesTramiteService {

    private final PlantillaTramiteRepository plantillaTramiteRepository;
    private final PasoFlujoRepository pasoFlujoRepository;
    private final RequisitoPlantillaRepository requisitoPlantillaRepository;

        @Override
        public List<DetallesPlantillaResponseDTO> listarTodos() {
        return plantillaTramiteRepository.findAllDetallesBase()
            .stream()
            .map(this::buildDetalle)
            .toList();
        }

    @Override
    public List<DetallesPlantillaResponseDTO> listarPorCarrera(@NonNull Integer idCarrera) {
        return plantillaTramiteRepository.findAllDetallesBaseByCarrera(idCarrera)
            .stream()
            .map(this::buildDetalle)
            .toList();
    }

    @Override
    public List<DetallesPlantillaResponseDTO> listarPorFacultad(@NonNull Integer idFacultad) {
        return plantillaTramiteRepository.findAllDetallesBaseByFacultad(idFacultad)
            .stream()
            .map(this::buildDetalle)
            .toList();
    }

    @Override
    public DetallesPlantillaResponseDTO obtenerPorPlantilla(@NonNull Integer idPlantilla) {
        DetallesPlantillaBaseResponseDTO base = plantillaTramiteRepository.findDetalleBaseByIdPlantilla(idPlantilla)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plantilla no encontrada con id: " + idPlantilla));

        return buildDetalle(base);
        }

        private DetallesPlantillaResponseDTO buildDetalle(DetallesPlantillaBaseResponseDTO base) {
        // Completa los datos base con relaciones dinamicas de flujo y requisitos.

        List<PasoFlujoPlantillaResponseDTO> pasos = base.getIdFlujo() == null
                ? List.of()
                : pasoFlujoRepository.findByFlujoTrabajoIdFlujoOrderByOrdenPasoAsc(base.getIdFlujo())
                        .stream()
                        .map(this::toPasoFlujoTramiteDTO)
                        .toList();

        List<RequisitoPlantillaResponseDTO> requisitos = requisitoPlantillaRepository
            .findByPlantillaIdPlantillaOrderByNumeroOrdenAsc(base.getIdPlantilla())
                .stream()
                .map(this::toRequisitoTramiteDTO)
                .toList();

        return DetallesPlantillaResponseDTO.builder()
                .idPlantilla(base.getIdPlantilla())
                .nombrePlantilla(base.getNombrePlantilla())
                .descripcionPlantilla(base.getDescripcionPlantilla())
                .idCategoria(base.getIdCategoria())
                .categoria(base.getCategoria())
                .idCarrera(base.getIdCarrera())
                .carrera(base.getCarrera())
                .idFacultad(base.getIdFacultad())
                .facultad(base.getFacultad())
                .idFlujo(base.getIdFlujo())
                .nombreFlujo(base.getNombreFlujo())
                .descripcion(base.getDescripcion())
                .idUsuarioCreador(base.getIdUsuarioCreador())
                .usuarioCreador(base.getUsuarioCreador() != null ? base.getUsuarioCreador().trim() : null)
                .version(base.getVersion())
                .pasosPlantilla(pasos)
                .requisitosPlantilla(requisitos)
                .idVentana(base.getIdVentana())
                .fechaApertura(base.getFechaApertura())
                .fechaCierre(base.getFechaCierre())
                .permiteExtension(base.getPermiteExtension())
                .diasResolucionEstimados(base.getDiasResolucionEstimados())
                .estaActivo(base.getEstaActivo())
                .disponibleExternos(base.getDisponibleExternos())
                .build();
    }

    private PasoFlujoPlantillaResponseDTO toPasoFlujoTramiteDTO(PasoFlujo paso) {
        return PasoFlujoPlantillaResponseDTO.builder()
                .idPaso(paso.getIdPaso())
                .idFlujo(paso.getFlujoTrabajo().getIdFlujo())
                .ordenPaso(paso.getOrdenPaso())
                .idEtapa(paso.getEtapa().getIdEtapa())
                .codigoEtapa(paso.getEtapa().getCodigo())
                .nombreEtapa(paso.getEtapa().getNombreEtapa())
                .descripcionEtapa(paso.getEtapa().getDescripcion())
                .idRolRequerido(paso.getRolRequerido() != null ? paso.getRolRequerido().getIdRol() : null)
                .rolRequerido(paso.getRolRequerido() != null ? paso.getRolRequerido().getNombreRol() : null)
                .idUsuarioEncargado(paso.getUsuarioEncargado() != null ? paso.getUsuarioEncargado().getIdUsuario() : null)
                .usuarioEncargado(formatUsuario(paso.getUsuarioEncargado()))
                .horasSla(paso.getHorasSla())
                .build();
    }

    private RequisitoPlantillaResponseDTO toRequisitoTramiteDTO(RequisitoPlantilla requisito) {
        return RequisitoPlantillaResponseDTO.builder()
                .idRequisito(requisito.getIdRequisito())
                .idPlantilla(requisito.getPlantilla().getIdPlantilla())
                .nombreRequisito(requisito.getNombreRequisito())
                .descripcionRequisito(requisito.getDescripcionRequisito())
                .esObligatorio(requisito.getEsObligatorio())
                .tipoDocumento(requisito.getTipoDocumento())
                .tamanoMaxMb(requisito.getTamanoMaxMb())
                .extensionesPermitidas(requisito.getExtensionesPermitidas())
                .numeroOrden(requisito.getNumeroOrden())
                .build();
    }

    private String formatUsuario(Usuario usuario) {
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



