package com.app.backend.dtos.tramites.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// DTO base de solicitud usado en operaciones CRUD generales.
public class SolicitudResponseDTO {
    private Integer idSolicitud;
    private String codigoSolicitud;
    private Integer idPlantilla;
    private Integer idUsuario;
    private Integer idCarrera;
    private Integer creadoPorId;
    private String detallesSolicitud;
    private String prioridad;
    private Integer pasoActualId;
    private String estadoActual;
    private LocalDateTime fechaCreacion;
    private LocalDate fechaEstimadaFin;
    private LocalDateTime fechaRealFin;
    private String resolucion;
}

