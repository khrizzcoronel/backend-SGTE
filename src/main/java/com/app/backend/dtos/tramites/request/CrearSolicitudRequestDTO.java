package com.app.backend.dtos.tramites.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Payload de creacion de solicitud desde el frontend (con adjuntos opcionales).
public class CrearSolicitudRequestDTO {

    @NotNull(message = "El id de la plantilla es obligatorio")
    private Integer idPlantilla;
    private String detallesSolicitud;
    @Builder.Default
    private String prioridad = "Normal";
    @Builder.Default
    private Integer pasoActual = 1;

    /**
     * Lista de IDs de requisitos, uno por cada archivo adjunto, en el mismo orden.
     * Si no hay archivos, puede ser null o vacÃ­a.
     */
    private List<Integer> idsRequisitos;
}

