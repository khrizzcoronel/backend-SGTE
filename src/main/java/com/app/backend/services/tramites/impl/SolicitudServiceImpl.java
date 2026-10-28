package com.app.backend.services.tramites.impl;

import com.app.backend.dtos.tramites.request.AccionPasoRequestDTO;
import com.app.backend.dtos.tramites.request.CrearSolicitudRequestDTO;
import com.app.backend.dtos.tramites.response.SolicitudDetalleResponseDTO;
import com.app.backend.dtos.tramites.response.SolicitudResponseDTO;
import com.app.backend.dtos.tramites.response.SolicitudesPlantillasVigentesRespuestaDTO;
import com.app.backend.entities.documentos.DocumentoAdjunto;
import com.app.backend.entities.tramites.HistorialSolicitud;
import com.app.backend.entities.tramites.MotivoRechazo;
import com.app.backend.entities.tramites.PasoFlujo;
import com.app.backend.entities.tramites.PlantillaTramite;
import com.app.backend.entities.tramites.Rechazo;
import com.app.backend.entities.tramites.Solicitud;
import com.app.backend.entities.sistema.Usuario;
import com.app.backend.entities.academico.Carrera;
import com.app.backend.exceptions.RecursoNoEncontradoException;
import com.app.backend.exceptions.SolicitudAdjuntosInvalidosException;
import com.app.backend.repositories.sistema.CredencialRepository;
import com.app.backend.repositories.academico.CarreraRepository;
import com.app.backend.repositories.documentos.DocumentoAdjuntoRepository;
import com.app.backend.repositories.sistema.UsuarioRepository;
import com.app.backend.repositories.tramites.HistorialSolicitudRepository;
import com.app.backend.repositories.tramites.MotivoRechazoRepository;
import com.app.backend.repositories.tramites.PasoFlujoRepository;
import com.app.backend.repositories.tramites.PlantillaTramiteRepository;
import com.app.backend.repositories.tramites.RechazoRepository;
import com.app.backend.repositories.tramites.RequisitoPlantillaRepository;
import com.app.backend.repositories.tramites.SolicitudRepository;
import com.app.backend.services.externos.IAlmacenamientoService;
import com.app.backend.services.externos.IJwtService;
import com.app.backend.services.tramites.NotificacionTramiteService;
import com.app.backend.services.tramites.SolicitudService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
// Servicio central del modulo de coordinacion para gestionar solicitudes de tramite.
// Aqui vive la logica de creacion, detalle, aprobacion/rechazo y notificaciones.
public class SolicitudServiceImpl implements SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final PlantillaTramiteRepository plantillaTramiteRepository;
    private final UsuarioRepository usuarioRepository;
    private final CredencialRepository credencialRepository;
    private final CarreraRepository carreraRepository;
    private final PasoFlujoRepository pasoFlujoRepository;
    private final RequisitoPlantillaRepository requisitoPlantillaRepository;
    private final DocumentoAdjuntoRepository documentoAdjuntoRepository;
    private final HistorialSolicitudRepository historialSolicitudRepository;
    private final RechazoRepository rechazoRepository;
    private final MotivoRechazoRepository motivoRechazoRepository;
    private final HttpServletRequest request;
    private final IJwtService jwtService;
    private final IAlmacenamientoService almacenamientoService;
    private final NotificacionTramiteService notificacionTramiteService;

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudResponseDTO> listarTodas() {
        return solicitudRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudResponseDTO> listarPorUsuario(@NonNull Integer idUsuario) {
        return solicitudRepository.findByUsuarioIdUsuario(idUsuario).stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudResponseDTO> listarPorEstado(String estado) {
        return solicitudRepository.findByEstadoActual(estado).stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitudResponseDTO obtenerPorId(@NonNull Integer id) {
        return toDTO(solicitudRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada con id: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitudResponseDTO obtenerPorCodigo(String codigo) {
        return toDTO(solicitudRepository.findByCodigoSolicitud(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada con cÃ³digo: " + codigo)));
    }

    @Override
    public SolicitudResponseDTO crear(SolicitudResponseDTO dto) {
        Solicitud s = Solicitud.builder()
                .codigoSolicitud(dto.getCodigoSolicitud())
                .plantilla(plantillaTramiteRepository.findById(dto.getIdPlantilla()).orElseThrow(
                        () -> new RecursoNoEncontradoException("Plantilla no encontrada: " + dto.getIdPlantilla())))
                .usuario(usuarioRepository.findById(dto.getIdUsuario()).orElseThrow(
                        () -> new RecursoNoEncontradoException("Usuario no encontrado: " + dto.getIdUsuario())))
                .detallesSolicitud(dto.getDetallesSolicitud())
                .prioridad(dto.getPrioridad() != null ? dto.getPrioridad() : "normal")
                .estadoActual(dto.getEstadoActual() != null ? dto.getEstadoActual() : "pendiente")
                .fechaEstimadaFin(dto.getFechaEstimadaFin())
                .build();
        if (dto.getIdCarrera() != null)
            s.setCarrera(carreraRepository.findById(dto.getIdCarrera()).orElseThrow(
                    () -> new RecursoNoEncontradoException("Carrera no encontrada: " + dto.getIdCarrera())));
        if (dto.getCreadoPorId() != null)
            s.setCreadoPor(usuarioRepository.findById(dto.getCreadoPorId()).orElseThrow(
                    () -> new RecursoNoEncontradoException("Usuario no encontrado: " + dto.getCreadoPorId())));
        if (dto.getPasoActualId() != null)
            s.setPasoActual(pasoFlujoRepository.findById(dto.getPasoActualId()).orElseThrow(
                    () -> new RecursoNoEncontradoException("Paso no encontrado: " + dto.getPasoActualId())));
        return toDTO(solicitudRepository.save(s));
    }

    @Override
    public SolicitudResponseDTO actualizar(@NonNull Integer id, SolicitudResponseDTO dto) {
        Solicitud s = solicitudRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada con id: " + id));
        s.setDetallesSolicitud(dto.getDetallesSolicitud());
        s.setPrioridad(dto.getPrioridad());
        s.setEstadoActual(dto.getEstadoActual());
        s.setFechaEstimadaFin(dto.getFechaEstimadaFin());
        s.setFechaRealFin(dto.getFechaRealFin());
        s.setResolucion(dto.getResolucion());
        if (dto.getPasoActualId() != null)
            s.setPasoActual(pasoFlujoRepository.findById(dto.getPasoActualId()).orElseThrow(
                    () -> new RecursoNoEncontradoException("Paso no encontrado: " + dto.getPasoActualId())));
        return toDTO(solicitudRepository.save(s));
    }

    @Override
    public void eliminar(@NonNull Integer id) {
        if (!solicitudRepository.existsById(id))
            throw new RecursoNoEncontradoException("Solicitud no encontrada con id: " + id);
        solicitudRepository.deleteById(id);
    }

    @Override
    public void crearSolicitudConDocumentos(CrearSolicitudRequestDTO dto, List<MultipartFile> archivos) {
        // Flujo principal de ingreso de una solicitud desde el portal del estudiante.
        // 1. Extraer idUsuario e idCarrera del JWT
        Integer idUsuario = extraerIdUsuarioAutenticado();
        Integer idCarrera = extraerIdCarreraDelToken();
        if (idUsuario == null) {
            throw new RuntimeException("No se encontró un usuario autenticado válido para crear la solicitud");
        }

        // 2. Obtener entidades necesarias
        PlantillaTramite plantilla = plantillaTramiteRepository.findById(dto.getIdPlantilla())
                .orElseThrow(() -> new RecursoNoEncontradoException("Plantilla no encontrada: " + dto.getIdPlantilla()));
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + idUsuario));

        Carrera carrera = null;
        if (idCarrera != null) {
            carrera = carreraRepository.findById(idCarrera).orElse(null);
        }

        // 3. Obtener el primer paso del flujo como paso actual
        PasoFlujo primerPaso = null;
        if (plantilla.getFlujoTrabajo() != null) {
            List<PasoFlujo> pasos = pasoFlujoRepository
                    .findByFlujoTrabajoIdFlujoOrderByOrdenPasoAsc(plantilla.getFlujoTrabajo().getIdFlujo());
            if (!pasos.isEmpty()) {
                primerPaso = pasos.get(0);
            }
        }

        // 4. Generar código de solicitud y crear la entidad
        String codigoSolicitud = generarCodigoSolicitud();

        Solicitud solicitud = Solicitud.builder()
                .codigoSolicitud(codigoSolicitud)
                .plantilla(plantilla)
                .usuario(usuario)
                .carrera(carrera)
                .creadoPor(usuario)
                .detallesSolicitud(dto.getDetallesSolicitud())
                .prioridad(dto.getPrioridad() != null ? dto.getPrioridad() : "Normal")
                .pasoActual(primerPaso)
                .estadoActual("pendiente")
                .fechaCreacion(LocalDateTime.now())
                .build();

        solicitud = solicitudRepository.save(solicitud);
        log.info("Solicitud creada con id: {}", solicitud.getIdSolicitud());

        // 5. Subir archivos a Azure y guardar documentos adjuntos
        if (archivos != null && !archivos.isEmpty()) {
            List<Integer> idsRequisitos = dto.getIdsRequisitos();
            if (idsRequisitos == null || idsRequisitos.size() != archivos.size()) {
                throw new SolicitudAdjuntosInvalidosException(
                        "La cantidad de idsRequisitos debe coincidir con la cantidad de archivos adjuntos");
            }

            for (int i = 0; i < archivos.size(); i++) {
                MultipartFile archivo = archivos.get(i);
                Integer idRequisito = idsRequisitos.get(i);

                String extension = obtenerExtension(archivo.getOriginalFilename());
                String nombreGenerado = String.format("req_%d_user_%d_%d%s",
                        idRequisito, idUsuario, System.currentTimeMillis(), extension);

                String urlArchivo = almacenamientoService.subirArchivos(archivo);

                DocumentoAdjunto documento = DocumentoAdjunto.builder()
                        .solicitud(solicitud)
                        .requisitoPlantilla(requisitoPlantillaRepository.findById(idRequisito).orElse(null))
                        .nombreArchivo(nombreGenerado)
                        .nombreOriginal(archivo.getOriginalFilename())
                        .rutaArchivo(urlArchivo)
                        .tamanoBytes(archivo.getSize())
                        .tipoMime(archivo.getContentType())
                        .subidoPor(usuario)
                        .fechaSubida(LocalDateTime.now())
                        .build();

                documentoAdjuntoRepository.save(documento);
                log.info("Documento adjunto guardado: {}", nombreGenerado);
            }
        }

        // Notificar al estudiante y al gestor del primer paso
        notificacionTramiteService.notificarSolicitudCreada(solicitud.getIdSolicitud());
    }

    private Integer extraerIdUsuarioAutenticado() {
        // Prioriza idUsuario del JWT y, como respaldo, intenta resolverlo del SecurityContext.
        String encaAuth = request.getHeader("Authorization");
        if (encaAuth != null && encaAuth.startsWith("Bearer ")) {
            try {
                String jwt = encaAuth.substring(7);
                Integer idUsuario = jwtService.extraerIdUsuario(jwt);
                if (idUsuario != null) {
                    return idUsuario;
                }
            } catch (Exception ignored) {
                // Se intenta el respaldo con el contexto de seguridad.
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return credencialRepository.findByNombreUsuario(userDetails.getUsername())
                    .map(credencial -> credencial.getUsuario().getIdUsuario())
                    .orElse(null);
        }

        return null;
    }

    private Integer extraerIdCarreraDelToken() {
        String encaAuth = request.getHeader("Authorization");
        if (encaAuth == null || !encaAuth.startsWith("Bearer ")) {
            return null;
        }

        try {
            String jwt = encaAuth.substring(7);
            return jwtService.extraerIdCarrera(jwt);
        } catch (Exception ignored) {
            return null;
        }
    }

    private SolicitudResponseDTO toDTO(Solicitud s) {
        return SolicitudResponseDTO.builder().idSolicitud(s.getIdSolicitud()).codigoSolicitud(s.getCodigoSolicitud())
                .idPlantilla(s.getPlantilla().getIdPlantilla()).idUsuario(s.getUsuario().getIdUsuario())
                .idCarrera(s.getCarrera() != null ? s.getCarrera().getIdCarrera() : null)
                .creadoPorId(s.getCreadoPor() != null ? s.getCreadoPor().getIdUsuario() : null)
                .detallesSolicitud(s.getDetallesSolicitud()).prioridad(s.getPrioridad())
                .pasoActualId(s.getPasoActual() != null ? s.getPasoActual().getIdPaso() : null)
                .estadoActual(s.getEstadoActual()).fechaCreacion(s.getFechaCreacion())
                .fechaEstimadaFin(s.getFechaEstimadaFin()).fechaRealFin(s.getFechaRealFin())
                .resolucion(s.getResolucion()).build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudesPlantillasVigentesRespuestaDTO> listarPlantillasVigente() {
        try {
            // Reconstruye la vista de "vigentes" unicamente con entidades JPA.
            Integer idUsuario = null;
            String encaAuth = request.getHeader("Authorization");
            if (encaAuth != null && encaAuth.startsWith("Bearer ")) {
                String jwt = encaAuth.substring(7);
                idUsuario = jwtService.extraerIdUsuario(jwt);
            }
            if (idUsuario == null) {
                return List.of();
            }

            List<Solicitud> solicitudes = solicitudRepository.findByUsuarioIdUsuario(idUsuario).stream()
                    .filter(this::esSolicitudVigente)
                    .toList();

            Map<Integer, Long> totalEtapasPorFlujo = new HashMap<>();
            for (Solicitud solicitud : solicitudes) {
                if (solicitud.getPlantilla() == null || solicitud.getPlantilla().getFlujoTrabajo() == null) {
                    continue;
                }
                Integer idFlujo = solicitud.getPlantilla().getFlujoTrabajo().getIdFlujo();
                totalEtapasPorFlujo.computeIfAbsent(idFlujo,
                        key -> (long) pasoFlujoRepository.findByFlujoTrabajoIdFlujoOrderByOrdenPasoAsc(key).size());
            }

            return solicitudes.stream()
                    .sorted((a, b) -> {
                        LocalDateTime fechaA = a.getFechaCreacion();
                        LocalDateTime fechaB = b.getFechaCreacion();
                        if (fechaA == null && fechaB == null) {
                            return 0;
                        }
                        if (fechaA == null) {
                            return 1;
                        }
                        if (fechaB == null) {
                            return -1;
                        }
                        return fechaB.compareTo(fechaA);
                    })
                    .map(s -> {
                        Integer etapaActual = s.getPasoActual() != null ? s.getPasoActual().getOrdenPaso() : null;
                        Long totalEtapas = null;
                        if (s.getPlantilla() != null && s.getPlantilla().getFlujoTrabajo() != null) {
                            totalEtapas = totalEtapasPorFlujo.get(s.getPlantilla().getFlujoTrabajo().getIdFlujo());
                        }

                        return new SolicitudesPlantillasVigentesRespuestaDTO(
                                s.getIdSolicitud(),
                                s.getCodigoSolicitud(),
                                s.getPlantilla() != null ? s.getPlantilla().getNombrePlantilla() : null,
                                s.getPlantilla() != null && s.getPlantilla().getCategoria() != null
                                        ? s.getPlantilla().getCategoria().getNombreCategoria()
                                        : null,
                                s.getPrioridad(),
                                s.getEstadoActual(),
                                toDate(s.getFechaCreacion()),
                                toDate(s.getFechaEstimadaFin()),
                                etapaActual,
                                totalEtapas);
                    })
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar las solicitudes de plantillas: " + e.getMessage());
        }
    }

    private boolean esSolicitudVigente(Solicitud solicitud) {
        if (solicitud.getEstadoActual() == null) {
            return true;
        }
        String estado = solicitud.getEstadoActual().toLowerCase();
        return !"finalizado".equals(estado) && !"rechazado".equals(estado);
    }

    private Date toDate(LocalDateTime fecha) {
        if (fecha == null) {
            return null;
        }
        return Date.from(fecha.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Date toDate(LocalDate fecha) {
        if (fecha == null) {
            return null;
        }
        return Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudDetalleResponseDTO> listarSolicitudesUsuarioAutenticado() {
        Integer idUsuario = extraerIdUsuarioAutenticado();
        if (idUsuario == null) {
            throw new RuntimeException("No se encontró un usuario autenticado");
        }

        List<Solicitud> solicitudes = solicitudRepository.findByUsuarioIdUsuario(idUsuario);
        return solicitudes.stream().map(this::toDetalleDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitudDetalleResponseDTO obtenerDetalleSolicitud(@NonNull Integer idSolicitud) {
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada con id: " + idSolicitud));
        return toDetalleDTO(solicitud);
    }

    private SolicitudDetalleResponseDTO toDetalleDTO(Solicitud s) {
        // Ensambla una vista completa para seguimiento (flujo, historial y adjuntos).
        // Calcular etapa actual, total y pasos del flujo
        int totalEtapas = 0;
        int etapaActual = 0;
        List<SolicitudDetalleResponseDTO.PasoFlujoItemDTO> pasosFlujoDTO = List.of();

        if (s.getPlantilla() != null && s.getPlantilla().getFlujoTrabajo() != null) {
            List<PasoFlujo> pasos = pasoFlujoRepository
                    .findByFlujoTrabajoIdFlujoOrderByOrdenPasoAsc(s.getPlantilla().getFlujoTrabajo().getIdFlujo());
            totalEtapas = pasos.size();
            if (s.getPasoActual() != null) {
                etapaActual = s.getPasoActual().getOrdenPaso();
            }

            pasosFlujoDTO = pasos.stream()
                    .map(p -> SolicitudDetalleResponseDTO.PasoFlujoItemDTO.builder()
                            .idPaso(p.getIdPaso())
                            .ordenPaso(p.getOrdenPaso())
                            .nombreEtapa(p.getEtapa() != null ? p.getEtapa().getNombreEtapa() : null)
                            .codigoEtapa(p.getEtapa() != null ? p.getEtapa().getCodigo() : null)
                            .descripcionEtapa(p.getEtapa() != null ? p.getEtapa().getDescripcion() : null)
                            .rolRequerido(p.getRolRequerido() != null ? p.getRolRequerido().getNombreRol() : null)
                            .idRolRequerido(p.getRolRequerido() != null ? p.getRolRequerido().getIdRol() : null)
                            .usuarioEncargado(p.getUsuarioEncargado() != null
                                    ? p.getUsuarioEncargado().getNombres() + " " + p.getUsuarioEncargado().getApellidos()
                                    : null)
                            .horasSla(p.getHorasSla())
                            .build())
                    .toList();
        }

        // Historial
        List<HistorialSolicitud> historiales = historialSolicitudRepository
                .findBySolicitudIdSolicitudOrderByFechaEntradaAsc(s.getIdSolicitud());

        List<SolicitudDetalleResponseDTO.HistorialItemDTO> historialDTOs = historiales.stream()
                .map(h -> SolicitudDetalleResponseDTO.HistorialItemDTO.builder()
                        .idHistorial(h.getIdHistorial())
                        .nombreEtapa(h.getEtapa() != null ? h.getEtapa().getNombreEtapa() : null)
                        .codigoEtapa(h.getEtapa() != null ? h.getEtapa().getCodigo() : null)
                        .estado(h.getEstado())
                        .tipoAccion(h.getTipoAccion())
                        .procesadoPor(h.getProcesadoPor() != null
                                ? h.getProcesadoPor().getNombres() + " " + h.getProcesadoPor().getApellidos()
                                : null)
                        .comentarios(h.getComentarios())
                        .fechaEntrada(h.getFechaEntrada())
                        .fechaCompletado(h.getFechaCompletado())
                        .slaExcedido(h.getSlaExcedido())
                        .ordenPaso(h.getPasoFlujo() != null ? h.getPasoFlujo().getOrdenPaso() : null)
                        .build())
                .toList();

        // Documentos adjuntos
        List<DocumentoAdjunto> documentos = documentoAdjuntoRepository
                .findBySolicitudIdSolicitud(s.getIdSolicitud());

        List<SolicitudDetalleResponseDTO.DocumentoItemDTO> documentoDTOs = documentos.stream()
                .map(d -> SolicitudDetalleResponseDTO.DocumentoItemDTO.builder()
                        .idDocumento(d.getIdDocumento())
                        .nombreOriginal(d.getNombreOriginal())
                        .nombreArchivo(d.getNombreArchivo())
                        .rutaArchivo(d.getRutaArchivo())
                        .tamanoBytes(d.getTamanoBytes())
                        .tipoMime(d.getTipoMime())
                        .esValido(d.getEsValido())
                        .fechaSubida(d.getFechaSubida())
                        .nombreRequisito(d.getRequisitoPlantilla() != null
                                ? d.getRequisitoPlantilla().getNombreRequisito()
                                : null)
                        .build())
                .toList();

        return SolicitudDetalleResponseDTO.builder()
                .idSolicitud(s.getIdSolicitud())
                .codigoSolicitud(s.getCodigoSolicitud())
                .nombrePlantilla(s.getPlantilla() != null ? s.getPlantilla().getNombrePlantilla() : null)
                .categoria(s.getPlantilla() != null && s.getPlantilla().getCategoria() != null
                        ? s.getPlantilla().getCategoria().getNombreCategoria() : null)
                .carrera(s.getCarrera() != null ? s.getCarrera().getNombreCarrera() : null)
                .nombreUsuario(s.getUsuario() != null
                        ? s.getUsuario().getNombres() + " " + s.getUsuario().getApellidos() : null)
                .prioridad(s.getPrioridad())
                .estadoActual(s.getEstadoActual())
                .detallesSolicitud(s.getDetallesSolicitud())
                .resolucion(s.getResolucion())
                .fechaCreacion(s.getFechaCreacion())
                .fechaEstimadaFin(s.getFechaEstimadaFin())
                .fechaRealFin(s.getFechaRealFin())
                .etapaActual(etapaActual)
                .totalEtapas(totalEtapas)
                .idPasoActual(s.getPasoActual() != null ? s.getPasoActual().getIdPaso() : null)
                .pasosFlujo(pasosFlujoDTO)
                .historial(historialDTOs)
                .documentos(documentoDTOs)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudDetalleResponseDTO> listarSolicitudesPorRol(String nombreRol) {
        // Obtener todas las solicitudes que no estén finalizadas ni rechazadas
        List<Solicitud> todas = solicitudRepository.findAll();

        return todas.stream()
                .filter(s -> {
                    if (s.getPlantilla() == null || s.getPlantilla().getFlujoTrabajo() == null) return false;
                    // Incluir si algún paso del flujo tiene el rol indicado
                    List<PasoFlujo> pasos = pasoFlujoRepository
                            .findByFlujoTrabajoIdFlujoOrderByOrdenPasoAsc(s.getPlantilla().getFlujoTrabajo().getIdFlujo());
                    return pasos.stream().anyMatch(p ->
                            p.getRolRequerido() != null &&
                            p.getRolRequerido().getNombreRol().equalsIgnoreCase(nombreRol));
                })
                .map(this::toDetalleDTO)
                .toList();
    }

    @Override
    public void aprobarPasoActual(AccionPasoRequestDTO dto) {
        // Cambia el estado de la solicitud al aprobar un paso y registra historial.
        Integer idUsuario = extraerIdUsuarioAutenticado();
        if (idUsuario == null) {
            throw new RuntimeException("No se encontró un usuario autenticado");
        }

        Solicitud solicitud = solicitudRepository.findById(dto.getIdSolicitud())
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada: " + dto.getIdSolicitud()));

        if ("finalizado".equals(solicitud.getEstadoActual()) || "rechazado".equals(solicitud.getEstadoActual())) {
            throw new IllegalArgumentException("La solicitud ya está en estado final: " + solicitud.getEstadoActual());
        }

        PasoFlujo pasoActual = solicitud.getPasoActual();
        if (pasoActual == null) {
            throw new IllegalArgumentException("La solicitud no tiene un paso actual asignado");
        }

        // Validar que el usuario tiene el rol requerido para este paso
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        validarRolUsuarioEnPaso(usuario, pasoActual);

        // Registrar historial del paso completado
        HistorialSolicitud historial = HistorialSolicitud.builder()
                .solicitud(solicitud)
                .pasoFlujo(pasoActual)
                .etapa(pasoActual.getEtapa())
                .estado("aprobado")
                .tipoAccion("aprobacion")
                .procesadoPor(usuario)
                .comentarios(dto.getComentarios())
                .fechaEntrada(solicitud.getFechaCreacion())
                .fechaCompletado(LocalDateTime.now())
                .slaExcedido(false)
                .build();
        historialSolicitudRepository.save(historial);

        // Buscar el siguiente paso
        List<PasoFlujo> pasos = pasoFlujoRepository
                .findByFlujoTrabajoIdFlujoOrderByOrdenPasoAsc(
                        solicitud.getPlantilla().getFlujoTrabajo().getIdFlujo());

        int indexActual = -1;
        for (int i = 0; i < pasos.size(); i++) {
            if (pasos.get(i).getIdPaso().equals(pasoActual.getIdPaso())) {
                indexActual = i;
                break;
            }
        }

        PasoFlujo siguientePaso = null;
        if (indexActual + 1 < pasos.size()) {
            // Avanzar al siguiente paso
            siguientePaso = pasos.get(indexActual + 1);
            solicitud.setPasoActual(siguientePaso);
            solicitud.setEstadoActual("en_proceso");
        } else {
            // No hay más pasos, finalizar
            solicitud.setPasoActual(null);
            solicitud.setEstadoActual("finalizado");
            solicitud.setFechaRealFin(LocalDateTime.now());
        }

        solicitudRepository.save(solicitud);
        log.info("Paso aprobado para solicitud {} por usuario {}", dto.getIdSolicitud(), idUsuario);

        // Notificaciones (email + WhatsApp + gestor del siguiente paso)
        if ("finalizado".equals(solicitud.getEstadoActual())) {
            notificacionTramiteService.notificarSolicitudFinalizada(solicitud.getIdSolicitud(), usuario.getIdUsuario());
        } else {
            notificacionTramiteService.notificarPasoAprobado(solicitud.getIdSolicitud(), pasoActual.getIdPaso(), usuario.getIdUsuario(), siguientePaso != null ? siguientePaso.getIdPaso() : null);
        }
    }

    @Override
    public void rechazarSolicitud(AccionPasoRequestDTO dto) {
        // Cierra la solicitud como rechazada y guarda trazabilidad de la decision.
        Integer idUsuario = extraerIdUsuarioAutenticado();
        if (idUsuario == null) {
            throw new RuntimeException("No se encontró un usuario autenticado");
        }

        Solicitud solicitud = solicitudRepository.findById(dto.getIdSolicitud())
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada: " + dto.getIdSolicitud()));

        if ("finalizado".equals(solicitud.getEstadoActual()) || "rechazado".equals(solicitud.getEstadoActual())) {
            throw new IllegalArgumentException("La solicitud ya está en estado final: " + solicitud.getEstadoActual());
        }

        PasoFlujo pasoActual = solicitud.getPasoActual();
        if (pasoActual == null) {
            throw new IllegalArgumentException("La solicitud no tiene un paso actual asignado");
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        validarRolUsuarioEnPaso(usuario, pasoActual);

        // Registrar historial
        HistorialSolicitud historial = HistorialSolicitud.builder()
                .solicitud(solicitud)
                .pasoFlujo(pasoActual)
                .etapa(pasoActual.getEtapa())
                .estado("rechazado")
                .tipoAccion("rechazo")
                .procesadoPor(usuario)
                .comentarios(dto.getComentarios())
                .fechaEntrada(solicitud.getFechaCreacion())
                .fechaCompletado(LocalDateTime.now())
                .slaExcedido(false)
                .build();
        historialSolicitudRepository.save(historial);

        // Registrar rechazo
        if (dto.getIdMotivo() != null) {
            MotivoRechazo motivo = motivoRechazoRepository.findById(dto.getIdMotivo()).orElse(null);
            if (motivo != null) {
                Rechazo rechazo = Rechazo.builder()
                        .solicitud(solicitud)
                        .motivoRechazo(motivo)
                        .rechazadoPor(usuario)
                        .comentarios(dto.getComentarios() != null ? dto.getComentarios() : "")
                        .fechaRechazo(LocalDateTime.now())
                        .notificacionEnviada(false)
                        .build();
                rechazoRepository.save(rechazo);
            }
        }

        solicitud.setEstadoActual("rechazado");
        solicitudRepository.save(solicitud);
        log.info("Solicitud {} rechazada por usuario {}", dto.getIdSolicitud(), idUsuario);

        // Notificaciones (email + WhatsApp al estudiante)
        notificacionTramiteService.notificarSolicitudRechazada(solicitud.getIdSolicitud(), pasoActual.getIdPaso(), usuario.getIdUsuario(), dto.getComentarios());
    }

    private void validarRolUsuarioEnPaso(Usuario usuario, PasoFlujo paso) {
        // El control de autorizacion por etapa se valida contra el rol embebido en JWT.
        if (paso.getRolRequerido() == null) return;

        Integer idRolDelToken = extraerIdRolDelToken();
        Integer idRolRequerido = paso.getRolRequerido().getIdRol();

        log.info("Validando rol - Usuario: {} (id: {}), rol del JWT: {}, rol requerido: {} (id: {})",
                usuario.getNombres(), usuario.getIdUsuario(), idRolDelToken,
                paso.getRolRequerido().getNombreRol(), idRolRequerido);

        boolean tieneRol = idRolDelToken != null && idRolDelToken.equals(idRolRequerido);

        if (!tieneRol) {
            throw new IllegalArgumentException(
                    "No tiene el rol requerido (" + paso.getRolRequerido().getNombreRol() + ") para gestionar este paso");
        }
    }

    private Integer extraerIdRolDelToken() {
        String encaAuth = request.getHeader("Authorization");
        if (encaAuth == null || !encaAuth.startsWith("Bearer ")) return null;
        try {
            String jwt = encaAuth.substring(7);
            return jwtService.extraerIdRol(jwt);
        } catch (Exception e) {
            return null;
        }
    }

    private String generarCodigoSolicitud() {
        // Formato esperado: SOL-AAAA-#####
        String prefijo = "SOL";
        String anio = String.valueOf(java.time.Year.now().getValue());
        long count = solicitudRepository.count() + 1;
        return String.format("%s-%s-%05d", prefijo, anio, count);
    }

    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains("."))
            return "";
        return nombreArchivo.substring(nombreArchivo.lastIndexOf("."));
    }

}


