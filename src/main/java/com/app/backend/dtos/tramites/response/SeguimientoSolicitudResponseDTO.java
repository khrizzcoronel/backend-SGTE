package com.app.backend.dtos.tramites.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// DTO de seguimiento que representa una entrada del historial de solicitud.
public class SeguimientoSolicitudResponseDTO {
    private Integer idSeguimiento;
    private Integer idSolicitud;
    private Integer idPaso;
    private Integer idEtapa;
    private String estado;
    private Integer procesadoPorId;
    private String comentarios;
    private LocalDateTime fechaEntrada;
    private LocalDateTime fechaCompletado;
    private Boolean slaExcedido;
}

