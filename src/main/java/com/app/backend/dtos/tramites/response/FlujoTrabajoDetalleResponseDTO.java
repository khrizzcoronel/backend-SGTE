package com.app.backend.dtos.tramites.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Resumen de flujo con su lista de pasos para gestion en coordinacion.
public class FlujoTrabajoDetalleResponseDTO {
    private Integer idFlujo;
    private String nombreFlujo;
    private String descripcionFlujo;
    private Boolean estaActivo;
    private Integer version;
    private Integer creadoPorId;
    private List<PasoFlujoDetalleResponseDTO> pasos;
}