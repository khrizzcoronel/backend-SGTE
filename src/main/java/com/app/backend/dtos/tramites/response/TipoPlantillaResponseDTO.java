package com.app.backend.dtos.tramites.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// DTO de salida para gestion administrativa de plantillas.
public class TipoPlantillaResponseDTO {
    private Integer idPlantilla;
    private String nombrePlantilla;
    private String descripcionPlantilla;
    private Integer idCategoria;
    private Integer idCarrera;
    private Integer idFlujo;
    private Integer diasEstimados;
    private Boolean estaActivo;
    private Boolean disponibleExternos;
}