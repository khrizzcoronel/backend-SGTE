package com.app.backend.services.tramites.impl;

import com.app.backend.entities.sistema.ConfiguracionUsuario;
import com.app.backend.entities.sistema.Notificacion;
import com.app.backend.entities.sistema.TipoNotificacion;
import com.app.backend.entities.sistema.Usuario;
import com.app.backend.entities.tramites.PasoFlujo;
import com.app.backend.entities.tramites.Solicitud;
import com.app.backend.repositories.sistema.ConfiguracionUsuarioRepository;
import com.app.backend.repositories.sistema.NotificacionRepository;
import com.app.backend.repositories.sistema.TipoNotificacionRepository;
import com.app.backend.repositories.sistema.UsuarioRepository;
import com.app.backend.repositories.tramites.PasoFlujoRepository;
import com.app.backend.repositories.tramites.SolicitudRepository;
import com.app.backend.services.externos.ICorreoService;
import com.app.backend.services.externos.IWhatsAppService;
import com.app.backend.services.externos.PlantillaCorreoTramite;
import com.app.backend.services.tramites.NotificacionTramiteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
// Orquesta notificaciones del flujo de tramites por correo, WhatsApp y registro interno.
public class NotificacionTramiteServiceImpl implements NotificacionTramiteService {

    private final ICorreoService correoService;
    private final IWhatsAppService whatsAppService;
    private final UsuarioRepository usuarioRepository;
    private final SolicitudRepository solicitudRepository;
    private final PasoFlujoRepository pasoFlujoRepository;
    private final NotificacionRepository notificacionRepository;
    private final TipoNotificacionRepository tipoNotificacionRepository;
    private final ConfiguracionUsuarioRepository configuracionUsuarioRepository;

    // ========================
    // SOLICITUD CREADA
    // ========================
    @Async
    @Transactional
    @Override
    public void notificarSolicitudCreada(Integer idSolicitud) {
        // Evento inicial: confirma al estudiante y alerta al primer gestor del flujo.
        try {
            if (idSolicitud == null) return;
            Solicitud solicitud = solicitudRepository.findById(idSolicitud).orElse(null);
            if (solicitud == null) return;

            Usuario estudiante = solicitud.getUsuario();
            String nombreEstudiante = nombreCompleto(estudiante);
            String codigo = solicitud.getCodigoSolicitud();
            String plantilla = solicitud.getPlantilla().getNombrePlantilla();
            String prioridad = solicitud.getPrioridad();

            String titulo = "Solicitud registrada - " + codigo;
            String mensaje = "Tu solicitud " + plantilla + " ha sido registrada exitosamente. Prioridad: " + prioridad;

            // Enviar por canales habilitados del estudiante
            enviarPorCanalesHabilitados(estudiante,
                    "Solicitud Registrada - " + codigo,
                    PlantillaCorreoTramite.solicitudCreada(nombreEstudiante, codigo, plantilla, prioridad),
                    String.format("SGTE UTEQ\n\nHola %s, tu solicitud *%s* (%s) ha sido registrada exitosamente.\n\nEstado: Pendiente\nPrioridad: %s\n\nRecibirás notificaciones cuando avance.",
                            nombreEstudiante, codigo, plantilla, prioridad));

            // Persistir en BD
            persistirNotificacion(estudiante, solicitud, "SOL_CREADA", titulo, mensaje);

            // Notificar al gestor del primer paso
            PasoFlujo primerPaso = solicitud.getPasoActual();
            if (primerPaso != null && primerPaso.getRolRequerido() != null) {
                notificarGestoresDePaso(primerPaso, solicitud, nombreEstudiante);
            }

            log.info("Notificaciones enviadas para solicitud creada: {}", codigo);
        } catch (Exception e) {
            log.error("Error al enviar notificaciones de solicitud creada: {}", e.getMessage(), e);
        }
    }

    // ========================
    // PASO APROBADO
    // ========================
    @Async
    @Transactional
    @Override
    public void notificarPasoAprobado(Integer idSolicitud, Integer idPasoAprobado,
                                       Integer idAprobador, Integer idSiguientePaso) {
        // Evento intermedio/final: informa avance de etapa y posible finalizacion.
        try {
            if (idSolicitud == null || idPasoAprobado == null || idAprobador == null) return;
            Solicitud solicitud = solicitudRepository.findById(idSolicitud).orElse(null);
            PasoFlujo pasoAprobado = pasoFlujoRepository.findById(idPasoAprobado).orElse(null);
            Usuario aprobador = usuarioRepository.findById(idAprobador).orElse(null);
            PasoFlujo siguientePaso = idSiguientePaso != null ? pasoFlujoRepository.findById(idSiguientePaso).orElse(null) : null;

            if (solicitud == null || pasoAprobado == null || aprobador == null) return;

            Usuario estudiante = solicitud.getUsuario();
            String nombreEstudiante = nombreCompleto(estudiante);
            String nombreAprobador = nombreCompleto(aprobador);
            String codigo = solicitud.getCodigoSolicitud();
            String plantilla = solicitud.getPlantilla().getNombrePlantilla();
            String etapaAprobada = pasoAprobado.getEtapa() != null
                    ? pasoAprobado.getEtapa().getNombreEtapa() : "Paso " + pasoAprobado.getOrdenPaso();
            String siguienteEtapa = siguientePaso != null && siguientePaso.getEtapa() != null
                    ? siguientePaso.getEtapa().getNombreEtapa() : null;

            // Mensaje WhatsApp
            String msgWa = siguientePaso != null
                    ? String.format("SGTE UTEQ\n\nHola %s, la etapa *%s* de tu solicitud *%s* fue APROBADA por %s.\n\nSiguiente etapa: %s",
                    nombreEstudiante, etapaAprobada, codigo, nombreAprobador, siguienteEtapa)
                    : String.format("SGTE UTEQ\n\nHola %s, tu solicitud *%s* ha sido FINALIZADA exitosamente. Todas las etapas fueron aprobadas.",
                    nombreEstudiante, codigo);

            // Enviar por canales habilitados
            enviarPorCanalesHabilitados(estudiante,
                    "Etapa Aprobada - " + codigo,
                    PlantillaCorreoTramite.pasoAprobado(nombreEstudiante, codigo, plantilla,
                            etapaAprobada, nombreAprobador, siguienteEtapa),
                    msgWa);

            // Persistir en BD
            String msgPersistir = siguientePaso != null
                    ? "La etapa \"" + etapaAprobada + "\" fue aprobada por " + nombreAprobador + ". Siguiente: " + siguienteEtapa
                    : "Tu solicitud ha sido finalizada exitosamente. Todas las etapas fueron aprobadas.";
            persistirNotificacion(estudiante, solicitud, "SOL_APROBADA",
                    "Etapa aprobada - " + codigo, msgPersistir);

            // Si hay siguiente paso, notificar a los gestores
            if (siguientePaso != null && siguientePaso.getRolRequerido() != null) {
                notificarGestoresDePaso(siguientePaso, solicitud, nombreEstudiante);
            }

            log.info("Notificaciones enviadas para paso aprobado: {} - {}", codigo, etapaAprobada);
        } catch (Exception e) {
            log.error("Error al enviar notificaciones de paso aprobado: {}", e.getMessage(), e);
        }
    }

    // ========================
    // SOLICITUD RECHAZADA
    // ========================
    @Async
    @Transactional
    @Override
    public void notificarSolicitudRechazada(Integer idSolicitud, Integer idPasoRechazado,
                                             Integer idRechazador, String comentarios) {
        try {
            if (idSolicitud == null || idPasoRechazado == null || idRechazador == null) return;
            Solicitud solicitud = solicitudRepository.findById(idSolicitud).orElse(null);
            PasoFlujo pasoRechazado = pasoFlujoRepository.findById(idPasoRechazado).orElse(null);
            Usuario rechazador = usuarioRepository.findById(idRechazador).orElse(null);

            if (solicitud == null || pasoRechazado == null || rechazador == null) return;

            Usuario estudiante = solicitud.getUsuario();
            String nombreEstudiante = nombreCompleto(estudiante);
            String nombreRechazador = nombreCompleto(rechazador);
            String codigo = solicitud.getCodigoSolicitud();
            String plantilla = solicitud.getPlantilla().getNombrePlantilla();
            String etapa = pasoRechazado.getEtapa() != null
                    ? pasoRechazado.getEtapa().getNombreEtapa() : "Paso " + pasoRechazado.getOrdenPaso();

            String msgComentario = (comentarios != null && !comentarios.isBlank())
                    ? "\n\nObservación: " + comentarios : "";

            // Enviar por canales habilitados
            enviarPorCanalesHabilitados(estudiante,
                    "Solicitud Rechazada - " + codigo,
                    PlantillaCorreoTramite.solicitudRechazada(nombreEstudiante, codigo, plantilla,
                            etapa, nombreRechazador, comentarios),
                    String.format("SGTE UTEQ\n\nHola %s, lamentamos informarte que tu solicitud *%s* (%s) fue RECHAZADA en la etapa \"%s\" por %s.%s\n\nPuedes acercarte a coordinación para más información.",
                            nombreEstudiante, codigo, plantilla, etapa, nombreRechazador, msgComentario));

            // Persistir en BD
            String msgRechazo = "Tu solicitud " + plantilla + " fue rechazada en la etapa \"" + etapa + "\" por " + nombreRechazador + ".";
            if (comentarios != null && !comentarios.isBlank()) msgRechazo += " Observación: " + comentarios;
            persistirNotificacion(estudiante, solicitud, "SOL_RECHAZADA",
                    "Solicitud rechazada - " + codigo, msgRechazo);

            log.info("Notificaciones enviadas para solicitud rechazada: {}", codigo);
        } catch (Exception e) {
            log.error("Error al enviar notificaciones de solicitud rechazada: {}", e.getMessage(), e);
        }
    }

    // ========================
    // SOLICITUD FINALIZADA
    // ========================
    @Async
    @Transactional
    @Override
    public void notificarSolicitudFinalizada(Integer idSolicitud, Integer idAprobadorFinal) {
        try {
            if (idSolicitud == null) return;
            Solicitud solicitud = solicitudRepository.findById(idSolicitud).orElse(null);
            if (solicitud == null) return;

            Usuario estudiante = solicitud.getUsuario();
            String nombreEstudiante = nombreCompleto(estudiante);
            String codigo = solicitud.getCodigoSolicitud();
            String plantilla = solicitud.getPlantilla().getNombrePlantilla();

            // Enviar por canales habilitados
            enviarPorCanalesHabilitados(estudiante,
                    "Solicitud Finalizada - " + codigo,
                    PlantillaCorreoTramite.solicitudFinalizada(nombreEstudiante, codigo, plantilla),
                    String.format("SGTE UTEQ\n\n¡Felicidades %s! Tu solicitud *%s* (%s) ha sido FINALIZADA exitosamente.\n\nTodas las etapas del flujo de aprobación fueron completadas. Ingresa al sistema para ver la resolución.",
                            nombreEstudiante, codigo, plantilla));

            // Persistir en BD
            persistirNotificacion(estudiante, solicitud, "CAMBIO_ESTADO",
                    "Solicitud finalizada - " + codigo,
                    "¡Felicidades! Tu solicitud " + plantilla + " ha sido finalizada exitosamente.");

            log.info("Notificaciones enviadas para solicitud finalizada: {}", codigo);
        } catch (Exception e) {
            log.error("Error al enviar notificaciones de solicitud finalizada: {}", e.getMessage(), e);
        }
    }

    // ========================
    // HELPERS PRIVADOS
    // ========================

    private void enviarPorCanalesHabilitados(Usuario usuario, String asuntoCorreo, String cuerpoCorreo, String mensajeWhatsApp) {
        // Respeta preferencias del usuario y aplica defaults si no existe configuracion.
        boolean enviarEmail = true;
        boolean enviarWhatsApp = true; // Por defecto enviar si tiene teléfono

        try {
            ConfiguracionUsuario config = configuracionUsuarioRepository
                    .findByUsuarioIdUsuario(usuario.getIdUsuario()).orElse(null);
            if (config != null) {
                enviarEmail = Boolean.TRUE.equals(config.getNotificarEmail());
                enviarWhatsApp = Boolean.TRUE.equals(config.getNotificarWhatsapp());
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener configuración del usuario {}, usando valores por defecto", usuario.getIdUsuario());
        }

        if (enviarEmail) {
            enviarCorreoSeguro(obtenerCorreo(usuario), asuntoCorreo, cuerpoCorreo);
        }

        if (enviarWhatsApp) {
            enviarWhatsAppSeguro(usuario.getTelefono(), mensajeWhatsApp);
        }
    }

    private void notificarGestoresDePaso(PasoFlujo paso, Solicitud solicitud, String nombreEstudiante) {
        if (paso.getRolRequerido() == null) return;

        String codigo = solicitud.getCodigoSolicitud();
        String plantilla = solicitud.getPlantilla().getNombrePlantilla();
        String etapa = paso.getEtapa() != null ? paso.getEtapa().getNombreEtapa() : "Paso " + paso.getOrdenPaso();

        if (paso.getUsuarioEncargado() != null) {
            Usuario gestor = paso.getUsuarioEncargado();
            notificarGestor(gestor, solicitud, codigo, plantilla, nombreEstudiante, etapa);
            return;
        }

        List<Usuario> gestores = usuarioRepository.findByRoles_IdRol(paso.getRolRequerido().getIdRol());
        for (Usuario gestor : gestores) {
            notificarGestor(gestor, solicitud, codigo, plantilla, nombreEstudiante, etapa);
        }
    }

    private void notificarGestor(Usuario gestor, Solicitud solicitud, String codigo, String plantilla,
                                  String nombreEstudiante, String etapa) {
        String nombreGestor = nombreCompleto(gestor);

        enviarPorCanalesHabilitados(gestor,
                "Acción Requerida - " + codigo,
                PlantillaCorreoTramite.notificacionGestor(nombreGestor, codigo, plantilla,
                        nombreEstudiante, etapa, "Revisar y aprobar/rechazar"),
                String.format("SGTE UTEQ\n\nHola %s, tienes una solicitud pendiente de gestión:\n\nCódigo: *%s*\nTrámite: %s\nEstudiante: %s\nEtapa: %s\n\nIngresa al sistema para revisar.",
                        nombreGestor, codigo, plantilla, nombreEstudiante, etapa));

        persistirNotificacion(gestor, solicitud, "SOL_EN_REVISION",
                "Acción requerida - " + codigo,
                "Solicitud " + plantilla + " del estudiante " + nombreEstudiante + " requiere tu gestión en la etapa \"" + etapa + "\".");
    }

    private void enviarCorreoSeguro(String correo, String asunto, String cuerpo) {
        if (correo == null || correo.isBlank()) return;
        try {
            correoService.sendEmailAsync(correo, asunto, cuerpo);
        } catch (Exception e) {
            log.error("Error al enviar correo a {}: {}", correo, e.getMessage());
        }
    }

    private void enviarWhatsAppSeguro(String telefono, String mensaje) {
        if (telefono == null || telefono.isBlank()) return;
        try {
            whatsAppService.enviarMensajeAsync(telefono, mensaje);
        } catch (Exception e) {
            log.error("Error al enviar WhatsApp a {}: {}", telefono, e.getMessage());
        }
    }

    private String nombreCompleto(Usuario u) {
        if (u == null) return "Usuario";
        return (u.getNombres() + " " + u.getApellidos()).trim();
    }

    @SuppressWarnings("null")
    private void persistirNotificacion(Usuario usuario, Solicitud solicitud, String codigoTipo, String titulo, String mensaje) {
        // Guarda la notificacion en BD para bandeja interna y trazabilidad.
        try {
            TipoNotificacion tipo = tipoNotificacionRepository.findByCodigoTipo(codigoTipo).orElse(null);
            if (tipo == null) {
                log.warn("Tipo de notificación '{}' no encontrado en BD, se omite persistencia", codigoTipo);
                return;
            }

            String canalesEnviados = determinarCanalesEnviados(usuario);

            Notificacion notificacion = Notificacion.builder()
                    .tipoNotificacion(tipo)
                    .usuario(usuario)
                    .solicitud(solicitud)
                    .titulo(titulo)
                    .mensaje(mensaje)
                    .canal(canalesEnviados)
                    .estaLeida(false)
                    .estaEnviada(true)
                    .build();
            notificacionRepository.save(notificacion);
        } catch (Exception e) {
            log.error("Error al persistir notificación para usuario {}: {}", usuario.getIdUsuario(), e.getMessage());
        }
    }

    private String determinarCanalesEnviados(Usuario usuario) {
        try {
            ConfiguracionUsuario config = configuracionUsuarioRepository
                    .findByUsuarioIdUsuario(usuario.getIdUsuario()).orElse(null);
            if (config == null) return "CORREO";

            StringBuilder canales = new StringBuilder();
            if (Boolean.TRUE.equals(config.getNotificarEmail())) canales.append("CORREO");
            if (Boolean.TRUE.equals(config.getNotificarWhatsapp())) {
                if (canales.length() > 0) canales.append(", ");
                canales.append("WHATSAPP");
            }
            return canales.length() > 0 ? canales.toString() : "NINGUNO";
        } catch (Exception e) {
            return "CORREO";
        }
    }

    private String obtenerCorreo(Usuario u) {
        if (u == null) return null;
        if (u.getCorreoInstitucional() != null && !u.getCorreoInstitucional().isBlank()) {
            return u.getCorreoInstitucional();
        }
        return u.getCorreoPersonal();
    }
}
