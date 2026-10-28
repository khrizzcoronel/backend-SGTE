package com.app.backend.dtos.tramites.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Payload de actualizacion de metadatos de plantilla existente.
public class PlantillaEditarRequestDTO {
    private String nombrePlantilla;
    private String descripcionPlantilla;
    private Integer idCategoria;
    private Integer idCarrera;
    private Integer idFlujo;
    private Integer diasEstimados;
    private Boolean estaActivo;
    private Boolean disponibleExternos;
}