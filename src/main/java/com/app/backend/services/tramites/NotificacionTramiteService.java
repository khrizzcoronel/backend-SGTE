package com.app.backend.services.tramites;

// Contrato de eventos de notificacion ligados al ciclo de vida de solicitudes.
public interface NotificacionTramiteService {

    void notificarSolicitudCreada(Integer idSolicitud);

    void notificarPasoAprobado(Integer idSolicitud, Integer idPasoAprobado, Integer idAprobador, Integer idSiguientePaso);

    void notificarSolicitudRechazada(Integer idSolicitud, Integer idPasoRechazado, Integer idRechazador, String comentarios);

    void notificarSolicitudFinalizada(Integer idSolicitud, Integer idAprobadorFinal);
}
