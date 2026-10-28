package com.app.backend.dtos.tramites.request;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Datos persistidos cuando una solicitud es rechazada en el flujo.
public class RechazoSolicitudRequestDTO {
    private Integer idRechazo;
    private Integer idSolicitud;
    private Integer idMotivo;
    private Integer rechazadoPorId;
    private String comentarios;
    private LocalDateTime fechaRechazo;
    private Boolean notificacionEnviada;
    private LocalDateTime fechaNotificacion;
}

