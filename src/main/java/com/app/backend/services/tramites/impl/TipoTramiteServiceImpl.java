package com.app.backend.services.tramites.impl;

import com.app.backend.dtos.tramites.request.PlantillaRequestDTO;
import com.app.backend.dtos.tramites.request.PlantillaEditarRequestDTO;
import com.app.backend.dtos.tramites.request.ActualizarRequisitosPlantillaRequestDTO;
import com.app.backend.dtos.tramites.request.RequisitoPlantillaRequestDTO;
import com.app.backend.dtos.tramites.response.PlantillaDTO;
import com.app.backend.dtos.tramites.response.PlantillaResponseDTO;
import com.app.backend.dtos.tramites.response.RequisitoPlantillaResponseDTO;
import com.app.backend.dtos.tramites.response.TipoPlantillaResponseDTO;
import com.app.backend.entities.tramites.RequisitoPlantilla;
import com.app.backend.entities.tramites.PlantillaTramite;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.repositories.tramites.CategoriaRepository;
import com.app.backend.repositories.tramites.FlujoTrabajoRepository;
import com.app.backend.repositories.tramites.PlantillaTramiteRepository;
import com.app.backend.repositories.academico.CarreraRepository;
import com.app.backend.repositories.academico.EstudianteRepository;
import com.app.backend.repositories.tramites.RechazoRepository;
import com.app.backend.repositories.tramites.RequisitoPlantillaRepository;
import com.app.backend.repositories.tramites.SolicitudRepository;
import com.app.backend.repositories.tramites.VentanaRecepcionRepository;
import com.app.backend.services.externos.IJwtService;
import com.app.backend.services.tramites.TipoTramiteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
// Gestion de plantillas: alta, edicion, requisitos y listado filtrado para estudiantes.
public class TipoTramiteServiceImpl implements TipoTramiteService {

    private final PlantillaTramiteRepository plantillaTramiteRepository;
    private final CategoriaRepository categoriaRepository;
    private final CarreraRepository carreraRepository;
    private final FlujoTrabajoRepository flujoTrabajoRepository;
    private final EstudianteRepository estudianteRepository;
    private final RequisitoPlantillaRepository requisitoPlantillaRepository;
    private final VentanaRecepcionRepository ventanaRecepcionRepository;
    private final SolicitudRepository solicitudRepository;
    private final RechazoRepository rechazoRepository;
    private final IJwtService jwtService;
    private final HttpServletRequest request;

    @Override
    @Transactional(readOnly = true)
    public List<TipoPlantillaResponseDTO> listarTodos() {
        return plantillaTramiteRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoPlantillaResponseDTO> listarActivos() {
        return plantillaTramiteRepository.findByEstaActivoTrue().stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoPlantillaResponseDTO> listarPorCategoria(@NonNull Integer idCategoria) {
        return plantillaTramiteRepository.findByCategoriaIdCategoria(idCategoria).stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TipoPlantillaResponseDTO obtenerPorId(@NonNull Integer id) {
        return toDTO(plantillaTramiteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plantilla no encontrada con id: " + id)));
    }

    @Override
    public TipoPlantillaResponseDTO crear(PlantillaRequestDTO dto) {
        // Crea la cabecera de plantilla y luego persiste requisitos asociados.
        PlantillaTramite t = PlantillaTramite.builder()
                .nombrePlantilla(dto.getNombrePlantilla())
                .descripcionPlantilla(dto.getDescripcionPlantilla())
                .diasResolucionEstimados(dto.getDiasEstimados() != null ? dto.getDiasEstimados() : 5)
                .estaActivo(dto.getEstaActivo() != null ? dto.getEstaActivo() : true)
                .disponibleExternos(dto.getDisponibleExternos() != null ? dto.getDisponibleExternos() : false)
                .build();
        if (dto.getIdCategoria() != null)
            t.setCategoria(categoriaRepository.findById(dto.getIdCategoria()).orElseThrow(
                    () -> new RecursoNoEncontradoException("CategorÃ­a no encontrada: " + dto.getIdCategoria())));
        if (dto.getIdCarrera() != null)
            t.setCarrera(carreraRepository.findById(dto.getIdCarrera()).orElseThrow(
                () -> new RecursoNoEncontradoException("Carrera no encontrada: " + dto.getIdCarrera())));
        if (dto.getIdFlujo() != null)
            t.setFlujoTrabajo(flujoTrabajoRepository.findById(dto.getIdFlujo())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Flujo no encontrado: " + dto.getIdFlujo())));

        PlantillaTramite plantillaGuardada = plantillaTramiteRepository.save(t);

        if (dto.getRequisitos() != null && !dto.getRequisitos().isEmpty()) {
            List<RequisitoPlantilla> requisitos = dto.getRequisitos().stream()
                    .map(requisito -> toEntity(requisito, plantillaGuardada))
                    .toList();
            requisitoPlantillaRepository.saveAll(requisitos);
        }

        return toDTO(plantillaGuardada);
    }

    @Override
    public TipoPlantillaResponseDTO actualizar(@NonNull Integer id, PlantillaEditarRequestDTO dto) {
        PlantillaTramite t = plantillaTramiteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plantilla no encontrada con id: " + id));
        if (dto.getNombrePlantilla() != null) t.setNombrePlantilla(dto.getNombrePlantilla());
        if (dto.getDescripcionPlantilla() != null) t.setDescripcionPlantilla(dto.getDescripcionPlantilla());
        if (dto.getDiasEstimados() != null) t.setDiasResolucionEstimados(dto.getDiasEstimados());
        if (dto.getEstaActivo() != null) t.setEstaActivo(dto.getEstaActivo());
        if (dto.getDisponibleExternos() != null) t.setDisponibleExternos(dto.getDisponibleExternos());
        if (dto.getIdCategoria() != null)
            t.setCategoria(categoriaRepository.findById(dto.getIdCategoria()).orElseThrow(
                    () -> new RecursoNoEncontradoException("CategorÃ­a no encontrada: " + dto.getIdCategoria())));
        if (dto.getIdCarrera() != null)
            t.setCarrera(carreraRepository.findById(dto.getIdCarrera()).orElseThrow(
                () -> new RecursoNoEncontradoException("Carrera no encontrada: " + dto.getIdCarrera())));
        if (dto.getIdFlujo() != null)
            t.setFlujoTrabajo(flujoTrabajoRepository.findById(dto.getIdFlujo())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Flujo no encontrado: " + dto.getIdFlujo())));
        return toDTO(plantillaTramiteRepository.save(t));
    }

    @Override
    public TipoPlantillaResponseDTO actualizarRequisitos(@NonNull Integer id, ActualizarRequisitosPlantillaRequestDTO dto) {
        PlantillaTramite plantilla = plantillaTramiteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plantilla no encontrada con id: " + id));

        requisitoPlantillaRepository.deleteByPlantillaIdPlantilla(id);

        if (dto.getRequisitos() != null && !dto.getRequisitos().isEmpty()) {
            List<RequisitoPlantilla> requisitos = dto.getRequisitos().stream()
                    .map(requisito -> toEntity(requisito, plantilla))
                    .toList();
            requisitoPlantillaRepository.saveAll(requisitos);
        }

        return toDTO(plantilla);
    }

    @Override
    public void eliminar(@NonNull Integer id) {
        if (!plantillaTramiteRepository.existsById(id))
            throw new RecursoNoEncontradoException("Plantilla no encontrada con id: " + id);
        plantillaTramiteRepository.deleteById(id);
    }

    @Override
    public void eliminarCompleto(@NonNull Integer id) {
        // Eliminacion funcional: limpia rechazos, solicitudes, ventanas y requisitos antes de borrar plantilla.
        PlantillaTramite plantilla = plantillaTramiteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plantilla no encontrada con id: " + id));

        List<Integer> solicitudesIds = solicitudRepository.findByPlantillaIdPlantilla(id).stream()
                .map(solicitud -> solicitud.getIdSolicitud())
                .toList();

        for (Integer solicitudId : solicitudesIds) {
            rechazoRepository.deleteAll(rechazoRepository.findBySolicitudIdSolicitud(solicitudId));
        }

        solicitudRepository.deleteAllByPlantillaIdPlantilla(id);
        ventanaRecepcionRepository.deleteAll(ventanaRecepcionRepository.findByPlantillaIdPlantilla(id));

        if (plantilla.getRequisitos() != null && !plantilla.getRequisitos().isEmpty()) {
            requisitoPlantillaRepository.deleteAll(plantilla.getRequisitos());
        }

        plantillaTramiteRepository.delete(plantilla);
    }

    private TipoPlantillaResponseDTO toDTO(PlantillaTramite t) {
        return TipoPlantillaResponseDTO.builder()
                .idPlantilla(t.getIdPlantilla())
                .nombrePlantilla(t.getNombrePlantilla())
                .descripcionPlantilla(t.getDescripcionPlantilla())
                .idCategoria(t.getCategoria() != null ? t.getCategoria().getIdCategoria() : null)
                .idCarrera(t.getCarrera() != null ? t.getCarrera().getIdCarrera() : null)
                .idFlujo(t.getFlujoTrabajo() != null ? t.getFlujoTrabajo().getIdFlujo() : null)
                .diasEstimados(t.getDiasResolucionEstimados())
                .estaActivo(t.getEstaActivo())
                .disponibleExternos(t.getDisponibleExternos())
                .build();
    }

    private RequisitoPlantilla toEntity(RequisitoPlantillaRequestDTO dto, PlantillaTramite plantilla) {
        return RequisitoPlantilla.builder()
                .plantilla(plantilla)
                .nombreRequisito(dto.getNombreRequisito())
                .descripcionRequisito(dto.getDescripcionRequisito())
                .esObligatorio(dto.getEsObligatorio() != null ? dto.getEsObligatorio() : true)
                .tipoDocumento(dto.getTipoDocumento())
                .tamanoMaxMb(dto.getTamanoMaxMb() != null ? dto.getTamanoMaxMb() : 10)
                .extensionesPermitidas(dto.getExtensionesPermitidas() != null ? dto.getExtensionesPermitidas() : "pdf,jpg,png")
                .numeroOrden(dto.getNumeroOrden() != null ? dto.getNumeroOrden() : 0)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlantillaResponseDTO> listarPlantillas(String categoria, Boolean activo,
            String busqueda) {
        // Aplica filtros de negocio usando contexto del JWT (externo/interno y carrera).
        Boolean esExternoCalculado = null;
        Integer idCarreraCalculado = null;

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String jwt = authHeader.substring(7);
                Integer idUsuario = jwtService.extraerIdUsuario(jwt);
                idCarreraCalculado = jwtService.extraerIdCarrera(jwt);

                if (idUsuario != null) {
                    var estudianteOpt = estudianteRepository.findByUsuarioIdUsuario(idUsuario);
                    if (estudianteOpt.isPresent()) {
                        esExternoCalculado = estudianteOpt.get().getEsExterno();
                    }
                }
            } catch (Exception e) {
                System.out.println("Error al procesar JWT en listarPlantillas: " + e.getMessage());
            }
        }

        Map<Integer, List<PlantillaDTO>> agrupado = plantillaTramiteRepository
                .listarPlantillas(categoria, activo, busqueda, esExternoCalculado, idCarreraCalculado)
            .stream().collect(Collectors.groupingBy(PlantillaDTO::getIdplantilla));

        return agrupado.entrySet().stream().map(entry -> {
            PlantillaDTO base = entry.getValue().get(0);

            PlantillaResponseDTO plantilla = new PlantillaResponseDTO();
            plantilla.setIdplantilla(base.getIdplantilla());
            plantilla.setNombreplantilla(base.getNombreplantilla());
            plantilla.setDescripcionplantilla(base.getDescripcionplantilla());
            plantilla.setNombrecategoria(base.getNombrecategoria());
            plantilla.setDiasresolucionestimados(base.getDiasresolucionestimados());
            plantilla.setEstaactivo(base.getEstaactivo());
            plantilla.setDisponiblesexternos(base.getDisponiblesexternos());
            plantilla.setPasos(base.getPasos());

            List<RequisitoPlantillaResponseDTO> requisitos = entry.getValue().stream()
                    .filter(req -> req.getNombrerequisito() != null)
                    .map(req -> {
                        RequisitoPlantillaResponseDTO r = new RequisitoPlantillaResponseDTO();
                        r.setNombreRequisito(req.getNombrerequisito());
                        r.setDescripcionRequisito(req.getDescripcionrequisito());
                        r.setEsObligatorio(req.getEsobligatorio());
                        r.setTipoDocumento(req.getTipodocumento());
                        r.setExtensionesPermitidas(req.getExtensionespermitidas());
                        r.setNumeroOrden(req.getNumeroorden());
                        return r;
                    }).collect(Collectors.toList());

            plantilla.setRequisitos(requisitos);
            return plantilla;
        }).collect(Collectors.toList());
    }
}

